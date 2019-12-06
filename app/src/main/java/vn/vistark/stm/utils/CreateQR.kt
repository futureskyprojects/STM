package vn.vistark.stm.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AlertDialog
import es.dmoral.toasty.Toasty
import vn.vistark.stm.data.db.ClassDB
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.model.ClassObj
import vn.vistark.stm.data.model.StudentObj
import vn.vistark.stm.ui.subject_manager.SubjectManager
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class CreateQR(private val activity: SubjectManager) :
    AsyncTask<Unit, Unit, Boolean>() {
    private var processingDialog: Loading.Progress = Loading.Progress(activity)
    var currentSubjectDir = ""
    private var classes = ArrayList<ClassObj>()
    private var students = ArrayList<StudentObj>()

    fun download(imageUrl: String): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url
                .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        processingDialog.show()
        currentSubjectDir =
            "${activity.externalCacheDir}"
        classes = ClassDB(activity).getAllClassObj()
        students = StudentDB(activity).getAllStudent()
    }

    override fun doInBackground(vararg params: Unit?): Boolean {
        val total = students.size
        var sum = 0
        classes.forEach { classObj ->
            val currentPath =
                "$currentSubjectDir${File.separator}${classObj.name}_${classObj.id}"
            val f = File(currentPath)
            if (!f.exists()) {
                f.mkdir()
            }
            students.forEach { studentObj ->
                if (studentObj.classId == classObj.id) {
                    sum++
                    processingDialog.updateProgress((sum * 100 / total))
                    processingDialog.updateStatus("$sum/$total Students")
                    if (f.exists()) {
                        val stdFile =
                            "$currentPath${File.separator}${studentObj.name}_${studentObj.id}.png"
                        val str = QRLink.generate(classObj.id, studentObj.id)
                        val bitmap = download(str)
                        if (bitmap != null) {
                            try {
                                val f2 = File(stdFile)
                                if (f2.exists()) {
                                    f2.delete()
                                }
                                val fo = FileOutputStream(f2)
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fo)
                                fo.flush()
                                fo.close()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

        }
        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        processingDialog.dissmis()
        if (result!!) {
            val alertDialog = AlertDialog.Builder(activity)
            alertDialog.setTitle("SUCCESS")
            alertDialog.setMessage("Success generate QR code for student, do you want to open FOLDER contains them?")
            alertDialog.setPositiveButton("Open now") { dialog, _ ->
                dialog.dismiss()
                Folder.openFolder(activity, currentSubjectDir)
            }
            alertDialog.setNegativeButton("Close", null)
            Toasty.success(activity, "Saved in $currentSubjectDir", Toasty.LENGTH_SHORT, false)
                .show()
            alertDialog.show()
        } else {
            Toasty.error(
                activity,
                "Have problem when processing, please try again!",
                Toasty.LENGTH_SHORT,
                false
            ).show()
        }
    }
}