package com.example.fyp;

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.*
import com.example.fyp.ml.LiteModelMovenetSingleposeLightningTfliteFloat164
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class PoseDetectionActivity : AppCompatActivity() {

    val paint = Paint()
    lateinit var imageProcessor: ImageProcessor
    lateinit var model: LiteModelMovenetSingleposeLightningTfliteFloat164
    lateinit var bitmap: Bitmap
    lateinit var imageView: ImageView
    lateinit var statusTextView: TextView
    lateinit var handler: Handler
    lateinit var handlerThread: HandlerThread
    lateinit var textureView: TextureView
    lateinit var cameraManager: CameraManager
    lateinit var actionSpinner: Spinner

    var jumpingJackCount = 0
    var wavingHandCount = 0
    var squatCount = 0
    var isOpen = false // 用于追踪 Jumping Jack 的状态
    var isSquatting = false // 用于追踪 Squat 的状态
    var lastWristX = 0f // 上一帧右手腕的 X 坐标
    var isHandWaving = false // 用于追踪挥手状态
    var selectedAction = "Jumping Jack" // 默认检测 Jumping Jack

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pose_detection)
        get_permissions()

        // 初始化组件
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR))
            .build()
        model = LiteModelMovenetSingleposeLightningTfliteFloat164.newInstance(this)
        imageView = findViewById(R.id.imageView)
        textureView = findViewById(R.id.textureView)
        statusTextView = TextView(this).apply {
            text = "Jumping Jacks: 0"
            textSize = 24f
            setTextColor(Color.WHITE)
        }
        addContentView(statusTextView, RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ))

        // 动作选择下拉菜单
        actionSpinner = Spinner(this).apply {
            val actions = arrayOf("Jumping Jack", "Waving Hand", "Squat")
            adapter = ArrayAdapter(this@PoseDetectionActivity, android.R.layout.simple_spinner_dropdown_item, actions)
        }
        addContentView(actionSpinner, RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 50
        })

        // 下拉菜单监听器
        actionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAction = actionSpinner.selectedItem.toString()
                when (selectedAction) {
                    "Jumping Jack" -> statusTextView.text = "Jumping Jacks: $jumpingJackCount"
                    "Waving Hand" -> statusTextView.text = "Waving Hand: $wavingHandCount"
                    "Squat" -> statusTextView.text = "Squats: $squatCount"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        handlerThread = HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)

        paint.color = Color.YELLOW

        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                open_camera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                bitmap = textureView.bitmap!!
                var tensorImage = TensorImage(DataType.UINT8)
                tensorImage.load(bitmap)
                tensorImage = imageProcessor.process(tensorImage)

                val inputFeature0 = TensorBuffer.createFixedSize(
                    intArrayOf(1, 192, 192, 3),
                    DataType.UINT8
                )
                inputFeature0.loadBuffer(tensorImage.buffer)

                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                var canvas = Canvas(mutable)
                var h = bitmap.height
                var w = bitmap.width
                var x = 0

                Log.d("output__", outputFeature0.size.toString())
                var headY = 0f
                var hipY = 0f
                var leftKneeY = 0f
                var rightKneeY = 0f
                var leftWristY = 0f
                var rightWristY = 0f
                var leftAnkleX = 0f
                var rightAnkleX = 0f
                var rightWristX = 0f // 右手腕 X 坐标

                while (x <= 49) {
                    if (outputFeature0[x + 2] > 0.45) {
                        val keyPointX = outputFeature0[x + 1] * w
                        val keyPointY = outputFeature0[x] * h
                        canvas.drawCircle(keyPointX, keyPointY, 10f, paint)

                        // 检测头部 (第0点为头部)
                        if (x / 3 == 0) {
                            headY = keyPointY
                        }

                        // 检测臀部 (第11点为臀部)
                        if (x / 3 == 11) {
                            hipY = keyPointY
                        }

                        // 检测左膝盖 (第13点为左膝盖)
                        if (x / 3 == 13) {
                            leftKneeY = keyPointY
                        }

                        // 检测右膝盖 (第14点为右膝盖)
                        if (x / 3 == 14) {
                            rightKneeY = keyPointY
                        }

                        // 检测右手腕
                        if (x / 3 == 10) {
                            rightWristY = keyPointY
                            rightWristX = keyPointX
                        }
                    }
                    x += 3
                }

                // 根据选择的动作执行检测逻辑
                when (selectedAction) {
                    "Jumping Jack" -> {
                        val handsRaised = (leftWristY < h / 3 && rightWristY < h / 3) // 双手举高
                        val feetApart = (Math.abs(leftAnkleX - rightAnkleX) > w / 3)  // 双脚分开
                        val feetTogether = (Math.abs(leftAnkleX - rightAnkleX) < w / 6) // 双脚并拢

                        if (!isOpen && handsRaised && feetApart) {
                            isOpen = true
                        } else if (isOpen && !handsRaised && feetTogether) {
                            isOpen = false
                            jumpingJackCount++
                            runOnUiThread {
                                statusTextView.text = "Jumping Jacks: $jumpingJackCount"
                            }
                        }
                    }

                    "Waving Hand" -> {
                        val handMovement = Math.abs(rightWristX - lastWristX) // 右手腕 X 坐标变化
                        if (!isHandWaving && handMovement > w / 10) { // 如果摆动幅度超过阈值
                            isHandWaving = true
                        } else if (isHandWaving && handMovement < w / 20) { // 如果停止摆动
                            isHandWaving = false
                            wavingHandCount++
                            runOnUiThread {
                                statusTextView.text = "Waving Hand: $wavingHandCount"
                            }
                        }
                        lastWristX = rightWristX // 更新上一帧的右手腕 X 坐标
                    }

                    "Squat" -> {
                        val isDown = (hipY > (leftKneeY + rightKneeY) / 2) // 臀部低于膝盖
                        val isUp = (hipY < (leftKneeY + rightKneeY) / 2) // 臀部高于膝盖

                        if (!isSquatting && isDown) {
                            isSquatting = true
                        } else if (isSquatting && isUp) {
                            isSquatting = false
                            squatCount++
                            runOnUiThread {
                                statusTextView.text = "Squats: $squatCount"
                            }
                        }
                    }
                }

                imageView.setImageBitmap(mutable)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        model.close()
    }

    @SuppressLint("MissingPermission")
    fun open_camera() {
        cameraManager.openCamera(cameraManager.cameraIdList[0], object : CameraDevice.StateCallback() {
            override fun onOpened(p0: CameraDevice) {
                var captureRequest = p0.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                var surface = Surface(textureView.surfaceTexture)
                captureRequest.addTarget(surface)
                p0.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(p0: CameraCaptureSession) {
                        p0.setRepeatingRequest(captureRequest.build(), null, null)
                    }

                    override fun onConfigureFailed(p0: CameraCaptureSession) {}
                }, handler)
            }

            override fun onDisconnected(p0: CameraDevice) {}

            override fun onError(p0: CameraDevice, p1: Int) {}
        }, handler)
    }

    fun get_permissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) get_permissions()
    }
}