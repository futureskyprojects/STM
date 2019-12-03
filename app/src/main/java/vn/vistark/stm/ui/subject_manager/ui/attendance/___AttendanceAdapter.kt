package vn.vistark.stm.ui.subject_manager.ui.attendance

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.stm.R
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.AttendanceObj
import vn.vistark.stm.data.model.StudentObj
import vn.vistark.stm.ui.main_fragments.student_fragment.StudentViewHolder

class ___AttendanceAdapter(val attendances: ArrayList<AttendanceObj>) :
    RecyclerView.Adapter<StudentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.student_obj_layout, parent, false)
        return StudentViewHolder(v)
    }

    override fun getItemCount(): Int {
        return attendances.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val attendanceObj = attendances[position]
        val subjectStudentsObj = SubjectStudentsDB(holder.cvRoot.context).get(attendanceObj.ssId)
        if (subjectStudentsObj != null) {
            val studentObj =
                StudentDB(holder.cvRoot.context).getStudent(subjectStudentsObj.studentId)
            holder.bind(studentObj!!)
            if (!attendanceObj.isAttendance) {
                holder.tvStudentName.setTextColor(Color.RED)
            }
        } else {
            println("Ignore")
        }
    }

}