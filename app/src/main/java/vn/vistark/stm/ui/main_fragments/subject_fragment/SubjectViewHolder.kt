package vn.vistark.stm.ui.main_fragments.subject_fragment

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.subject_item.view.*
import vn.vistark.stm.R
import vn.vistark.stm.data.model.SubjectObj
import vn.vistark.stm.ui.subject_manager.SubjectManager

class SubjectViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val tvSubjectName: TextView = v.findViewById(R.id.tvSubjectName)

    fun bind(subjectObj: SubjectObj) {
        tvSubjectName.text = subjectObj.name
        tvSubjectName.setOnClickListener {
            val intent = Intent(
                tvSubjectName.context,
                SubjectManager::class.java
            )
            intent.putExtra(SubjectObj.ARG_NAME, subjectObj)
            tvSubjectName.context.startActivity(
                intent
            )
        }
    }
}