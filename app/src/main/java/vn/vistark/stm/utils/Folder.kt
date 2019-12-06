package vn.vistark.stm.utils

import android.content.Context
import android.content.Intent
import android.net.Uri


class Folder {
    companion object {
        fun openFolder(context: Context, path: String) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            val uri: Uri = Uri.parse(path)
            intent.setDataAndType(uri, "resource/folder")
            context.startActivity(Intent.createChooser(intent, "Open QR Folder"))
        }
    }
}