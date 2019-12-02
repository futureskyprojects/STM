package vn.vistark.stm.ui.main_fragments.student_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.stm.R
import vn.vistark.stm.data.model.StudentObj

class StudentAdapter(
    val students: ArrayList<StudentObj>,
    val tvStudentEmpty: TextView
) :
    RecyclerView.Adapter<StudentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.student_obj_layout, parent, false)
        return StudentViewHolder(v)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val studentObj = students[position]
        holder.bind(studentObj)
        if (students.size > 0) {
            tvStudentEmpty.visibility = View.GONE
        } else {
            tvStudentEmpty.visibility = View.VISIBLE
        }
    }

}