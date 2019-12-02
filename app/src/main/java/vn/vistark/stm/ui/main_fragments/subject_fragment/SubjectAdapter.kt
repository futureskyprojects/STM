package vn.vistark.stm.ui.main_fragments.subject_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.stm.R
import vn.vistark.stm.data.model.SubjectObj

class SubjectAdapter(val subjects: ArrayList<SubjectObj>, val tvSubjectEmpty: TextView) :
    RecyclerView.Adapter<SubjectViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.subject_item, parent, false)
        return SubjectViewHolder(v)
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjects[position]
        holder.bind(subject)
        if (subjects.size <= 0) {
            tvSubjectEmpty.visibility = View.VISIBLE
        } else {
            tvSubjectEmpty.visibility = View.GONE
        }
    }

}