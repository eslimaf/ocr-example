package com.eslimaf.prototypeocr

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.widget.Toast
import com.google.android.gms.vision.text.TextRecognizer

class CaptureActivity : AppCompatActivity() {
    val TAG: String = "CaptureActivity";
    lateinit var mCameraSurfaceView: SurfaceView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)


    }

    fun createCameraSource(autoFocus: Boolean, useFlash: Boolean) {
        val textRecognizer: TextRecognizer = TextRecognizer.Builder(applicationContext).build();

        if (!textRecognizer.isOperational) {
            Log.w(TAG, "Detector dependencies are not available");
            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            val hasLowStorage = registerReceiver(null, lowStorageFilter) != null;
            if (hasLowStorage) {
                Toast.makeText(this, "Low storage error", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Low storage error");
            }
        }
    }
}
