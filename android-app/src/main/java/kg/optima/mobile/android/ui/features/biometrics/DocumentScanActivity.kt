package kg.optima.mobile.android.ui.features.biometrics

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kg.optima.mobile.R
import kg.optima.mobile.android.utils.saveFile
import kg.optima.mobile.android.utils.showExitAlertDialog
import kg.optima.mobile.databinding.ActivityDocumentScanBinding
import kz.verigram.veridoc.sdk.dependency.ICameraCaptureListener
import kz.verigram.veridoc.sdk.model.DocumentType
import kz.verigram.veridoc.sdk.model.Language
import kz.verigram.veridoc.sdk.model.RecognitionMode
import kz.verigram.verilive.sdk.LivenessInitializer

class DocumentScanActivity : AppCompatActivity(), ICameraCaptureListener {

    private val tag = DocumentScanActivity::class.java.simpleName
    private lateinit var binding: ActivityDocumentScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            showDialog()
        }
        setupCameraCaptureComponent()
        setBtnContinueClickListener()
    }

    override fun onSuccessCallback(result: HashMap<String, String>) {
        saveDocumentContent(content = result)
        binding.btnContinue.isVisible = true
        binding.textSuccess.isVisible = true
        Log.d(tag, result.size.toString())
    }

    override fun onLogEventCallback(result: HashMap<String, String>) {
        result.entries.forEach { (key, value) ->
            Log.d(tag, " key-> $key value-> $value ")
        }
    }

    override fun onErrorCallback(result: HashMap<String, String>) {
        Log.d(tag, result.size.toString())
    }

    override fun onResume() {
        super.onResume()
        binding.cameraCaptureDoc.start()
    }

    override fun onPause() {
        super.onPause()
        binding.cameraCaptureDoc.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.cameraCaptureDoc.destroy()
    }

    override fun onBackPressed() {
        showDialog()
    }

    private fun setBtnContinueClickListener() {
        binding.btnContinue.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                LivenessInitializer.init()
                startActivity(Intent(this, LivenessActivity::class.java))
            }, 200)
        }
    }

    private fun saveDocumentContent(content: HashMap<String, String>?) {
        content?.let { data ->
            saveFile(filename = "VERIFIED_DOC_FILE", data = data)
        }
    }

    private fun setupCameraCaptureComponent() {
        binding.cameraCaptureDoc.apply {
            setLanguage(Language.RU)
            setRecognitionMode(RecognitionMode.TWO_SIDED_DOCUMENT)
            setDocumentType(DocumentType.KG_ID)
            setIsGlareCheckNeeded(true)
        }
    }

    private fun showDialog() {
        showExitAlertDialog(
            title = getString(R.string.are_u_sure_exit),
            subtitle = getString(R.string.identification_is_not_finished),
            btnExitText = getString(R.string.exit),
            btnCancelText = getString(R.string.cancel)
        ) {
            // TODO Переход куда-то
        }
    }
}