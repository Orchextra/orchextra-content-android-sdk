package com.gigigo.orchextra.wrapper


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.gigigo.orchextra.ocmsdk.R
import com.gigigo.orchextra.wrapper.OxManager.ScanCodeListener
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class ScannerActivity : AppCompatActivity(), ZBarScannerView.ResultHandler {

  private val handler = Handler()
  private lateinit var scannerView: ZBarScannerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.ox_activity_scanner)

    scannerView = ZBarScannerView(this)
    val contentFrame = findViewById<ViewGroup>(R.id.content_frame)
    contentFrame.addView(scannerView)
  }

  override fun handleResult(rawResult: Result) {
    Log.d(TAG, "Result: " + rawResult.contents)
    scanCodeListener?.onCodeScan(rawResult.contents)
    finish()
    handler.postDelayed({ scannerView.resumeCameraPreview(this@ScannerActivity) }, 2000)
  }

  public override fun onResume() {
    super.onResume()
    scannerView.setResultHandler(this)

    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
      scannerView.startCamera()
    } else {
      requestPermission()
    }
  }

  private fun requestPermission() {
    if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
        Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
        finish()
      } else {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
            PERMISSIONS_REQUEST)
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
      grantResults: IntArray) {

    when (requestCode) {
      PERMISSIONS_REQUEST -> {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          scannerView.startCamera()
        } else {
          Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
          finish()
        }
      }
    }
  }

  public override fun onPause() {
    super.onPause()
    scannerView.stopCamera()
  }

  override fun onDestroy() {
    super.onDestroy()
    scanCodeListener = null
  }

  companion object Navigator {
    private const val TAG = "ScannerActivity"
    private const val PERMISSIONS_REQUEST = 0x132
    private var scanCodeListener: ScanCodeListener? = null

    fun open(context: Context, scanCodeListener: ScanCodeListener) {
      this.scanCodeListener = scanCodeListener
      val intent = Intent(context, ScannerActivity::class.java)
      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
      context.startActivity(intent)
    }
  }
}