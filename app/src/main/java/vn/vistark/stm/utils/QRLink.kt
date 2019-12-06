package vn.vistark.stm.utils

import android.content.Context
import vn.vistark.stm.data.db.ClassDB
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.model.ClassObj
import vn.vistark.stm.data.model.StudentObj
import java.net.URLEncoder

class QRLink {
    class QRResult(
        val classObj: ClassObj?,
        val studentObj: StudentObj?
    )

    companion object {
        fun generate(classId: Long, studentId: Long): String {
            return "https://chart.googleapis.com/chart?cht=qr&chl=${URLEncoder.encode(
                classId.toString() + "_" + studentId.toString(),
                "UTF-8"
            )}&chs=512x512"
        }

        fun decode(context: Context, inp: String): QRResult {
            val arr = inp.split("_")
            if (arr.size == 2) {
                var classObj: ClassObj? = null
                var studentObj: StudentObj? = StudentDB(context).getStudent(arr[1].toLong())

                ClassDB(context).getAllClassObj().forEach {
                    if (it.id == arr[0].toLong())
                        classObj = it
                }
                if (classObj != null && studentObj != null) {
                    return QRResult(classObj, studentObj)
                }
            }
            return QRResult(null, null)
        }
    }
}