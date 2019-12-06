package vn.vistark.stm.utils

import android.content.Context
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import vn.vistark.stm.R

class Loading() {
    companion object {
        fun create(context: AppCompatActivity): AlertDialog {
            val v = LayoutInflater.from(context).inflate(R.layout.loading_layout, null, false)
            val dialog = AlertDialog.Builder(context).create()
            dialog.setView(v)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(false)
            return dialog
        }

    }

    class Progress(val context: AppCompatActivity) {
        var dialog: AlertDialog
        var pbProcessingProgress: ProgressBar
        var tvStatusProcessing: TextView

        init {
            val v =
                LayoutInflater.from(context).inflate(R.layout.loading_progress_layout, null, false)
            dialog = AlertDialog.Builder(context).create()
            dialog.setView(v)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(false)
            ///
            pbProcessingProgress = v.findViewById(R.id.pbProcessingProgress)
            tvStatusProcessing = v.findViewById(R.id.tvStatusProcessing)
            //
            updateProgress(0)
            updateStatus("Please wait")
        }

        fun show() {
            dialog.show()
        }

        fun dissmis() {
            dialog.dismiss()
        }

        fun setMax(max: Int) {
            pbProcessingProgress.max = max
        }

        fun updateProgress(progress: Int) {
            pbProcessingProgress.post {
                pbProcessingProgress.progress = progress
            }
        }

        fun updateStatus(status: String) {
            tvStatusProcessing.post {
                tvStatusProcessing.text =
                    "${(pbProcessingProgress.progress * 100 / pbProcessingProgress.max)}% ($status)"
            }
        }
    }
}