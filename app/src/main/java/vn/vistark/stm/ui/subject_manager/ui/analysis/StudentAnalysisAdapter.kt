package vn.vistark.stm.ui.subject_manager.ui.analysis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.stm.R
import vn.vistark.stm.data.model.StudentObj

class StudentAnalysisAdapter(val students: ArrayList<StudentObj>) :
    RecyclerView.Adapter<StudentAnalysisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAnalysisViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_analysis_layout, parent, false)
        return StudentAnalysisViewHolder(v)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(holder: StudentAnalysisViewHolder, position: Int) {
        val student = students[position]
        holder.bind(student)
    }

}