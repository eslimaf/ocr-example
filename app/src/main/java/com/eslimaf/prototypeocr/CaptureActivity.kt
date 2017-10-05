package com.eslimaf.prototypeocr

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Size
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.widget.Toast
import com.google.android.gms.vision.text.TextRecognizer
import java.lang.Integer.signum
import java.util.*

class CaptureActivity : AppCompatActivity() {
    private val TAG: String = "CaptureActivity"
    private lateinit var mTextureView: TextureView
    private lateinit var mPreviewSize: Size
    private lateinit var mCameraId: String

    private val mTextureViewListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            setupCamera(width, height)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)
        mTextureView = findViewById(R.id.cameraTextureView)
    }

    override fun onResume() {
        super.onResume()
        if (mTextureView.isAvailable) {
            TODO("not implemented")
        } else {
            mTextureView.surfaceTextureListener = mTextureViewListener
        }
    }

    private fun setupCamera(width: Int, height: Int) {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        for (cameraId: String in cameraManager.cameraIdList) {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
            if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)
                    == CameraCharacteristics.LENS_FACING_FRONT) {
                continue
            }
            val streamMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            mPreviewSize = getPreferredPreviewSize(streamMap
                    .getOutputSizes(SurfaceTexture::class.java), width, height)
            mCameraId = cameraId
            return
        }
    }


    private fun getPreferredPreviewSize(mapSizes: Array<out Size>, width: Int, height: Int): Size {
        val collectorSizes = mutableListOf<Size>()
        for (option in mapSizes) {
            if (width > height) {
                if (option.width > width && option.height > height) {
                    collectorSizes.add(option)
                }
            } else {
                if (option.width > height && option.height > width) {
                    collectorSizes.add(option)
                }
            }
        }

        if (collectorSizes.size > 0) {
            return Collections.min(collectorSizes) { lhs, rhs
                -> signum(lhs.width * lhs.height - rhs.width * rhs.height) }
        }
        return mapSizes[0]
    }

    fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
        val textRecognizer: TextRecognizer = TextRecognizer.Builder(applicationContext).build();

        if (!textRecognizer.isOperational) {
            Log.w(TAG, "Detector dependencies are not available")
            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            val hasLowStorage = registerReceiver(null, lowStorageFilter) != null
            if (hasLowStorage) {
                Toast.makeText(this, "Low storage error", Toast.LENGTH_LONG).show()
                Log.w(TAG, "Low storage error")
            }
        }
    }
}
