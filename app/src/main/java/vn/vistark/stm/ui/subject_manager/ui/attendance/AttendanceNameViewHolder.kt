package vn.vistark.stm.ui.subject_manager.ui.attendance

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.attendance_layout.view.*
import vn.vistark.stm.R

class AttendanceNameViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val cvRoot: CardView = v.findViewById(R.id.cvRoot)
    val tvAttendanceTitle: TextView = v.findViewById(R.id.tvAttendanceTitle)

    fun bind(name: String) {
        tvAttendanceTitle.text = name
    }
}