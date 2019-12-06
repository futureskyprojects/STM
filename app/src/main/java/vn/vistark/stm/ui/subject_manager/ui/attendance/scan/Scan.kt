package vn.vistark.stm.ui.subject_manager.ui.attendance.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class Scan : AppCompatActivity(), ZXingScannerView.ResultHandler {
    companion object {
        const val STUDENT_ID = "STUDENT_ID"
    }

    lateinit var scannerView: ZXingScannerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
        scannerView.setResultHandler(this)
        scannerView.startCamera()
        scannerView.setAutoFocus(true)
    }

    override fun handleResult(res: Result?) {
        scannerView.removeAllViews()
        scannerView.stopCamera()
        if (res == null) {
            val returnIntent = Intent()
            setResult(Activity.RESULT_CANCELED, returnIntent)
            finish()
        } else {
            val returnIntent = Intent()
            returnIntent.putExtra(STUDENT_ID, res.text)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        scannerView.removeAllViews()
        scannerView.stopCamera()
    }
}