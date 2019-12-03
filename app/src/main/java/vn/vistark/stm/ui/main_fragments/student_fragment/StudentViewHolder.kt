package vn.vistark.stm.ui.main_fragments.student_fragment

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.student_obj_layout.view.*
import vn.vistark.stm.R
import vn.vistark.stm.data.db.ClassDB
import vn.vistark.stm.data.model.StudentObj

class StudentViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val cvRoot: CardView = v.findViewById(R.id.cvRoot)
    val civStudentAvatar: CircleImageView = v.findViewById(R.id.civStudentAvatar)
    val tvStudentName: TextView = v.findViewById(R.id.tvStudentName)
    val tvStudentClassName: TextView = v.findViewById(R.id.tvStudentClassName)

    fun bind(studentObj: StudentObj) {
        tvStudentName.text = studentObj.name
        ClassDB(tvStudentClassName.context).getAllClassObj().forEach {
            if (it.id == studentObj.classId) {
                tvStudentClassName.text = it.name
            }
        }
    }
}