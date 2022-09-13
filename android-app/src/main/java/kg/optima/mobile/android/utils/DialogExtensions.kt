package kg.optima.mobile.android.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kg.optima.mobile.R

fun Context.showExitAlertDialog(
    title: String?,
    subtitle: String?,
    btnExitText: String?,
    btnCancelText: String?,
    actionExit: () -> Unit
) {
    val layoutInflater = LayoutInflater.from(this)
    val view: View = layoutInflater.inflate(R.layout.exit_dialog, null)
    val alertDialog = AlertDialog.Builder(this)
        .setCancelable(false)
        .setView(view).create()

    val buttonExit = view.findViewById<Button>(R.id.btn_exit)
    val buttonCancel = view.findViewById<TextView>(R.id.text_view_cancel)
    val titleText = view.findViewById<TextView>(R.id.title_dialog)
    val subtitleText = view.findViewById<TextView>(R.id.subtitle_dialog)

    buttonExit.text = btnExitText
    buttonCancel.text = btnCancelText
    subtitleText.text = subtitle
    titleText.text = title

    buttonExit.setOnClickListener {
        actionExit.invoke()
    }

    buttonCancel.setOnClickListener {
        alertDialog.dismiss()
    }
    val window = alertDialog.window
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    alertDialog.show()
}