package vn.vistark.stm.ui.subject_manager.ui.analysis

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.data.db.AttendanceDB
import vn.vistark.stm.data.db.ClassDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.AttendanceObj
import vn.vistark.stm.data.model.StudentObj

class StudentAnalysisViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val tvClassName: TextView = v.findViewById(R.id.tvClassName)
    val civStudentAvatar: CircleImageView = v.findViewById(R.id.civStudentAvatar)
    val tvStudentName: TextView = v.findViewById(R.id.tvStudentName)
    val tvStudentAttendanceStatus: TextView = v.findViewById(R.id.tvStudentAttendanceStatus)

    fun bind(student: StudentObj) {
        tvClassName.text = "Undefined"
        ClassDB(tvClassName.context).getAllClassObj().forEach {
            if (it.id == student.classId)
                tvClassName.text = it.name
        }
        tvStudentName.text = student.name

        val ssObj = SubjectStudentsDB(tvStudentAttendanceStatus.context).get(
            Bus.SELECTED_SUBJECT,
            student.id
        )
        if (ssObj != null) {
            val attendances = AttendanceDB(tvStudentAttendanceStatus.context).getBySsId(ssObj.ssId)
            var ok = 0
            attendances.forEach {
                if (it.isAttendance)
                    ok++
            }
            tvStudentAttendanceStatus.text =
                "Attendance $ok/${attendances.size} (Miss ${if (attendances.size <= 0) 0 else (attendances.size - ok) * 100 / attendances.size}%)"
        } else {
            tvStudentAttendanceStatus.text = "Undefined"
        }
    }
}