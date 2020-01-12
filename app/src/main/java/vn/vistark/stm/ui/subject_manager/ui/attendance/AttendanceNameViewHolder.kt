package vn.vistark.stm.ui.subject_manager.ui.attendance

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.ui.subject_manager.ui.attendance.scan.ScanResultActivity

class AttendanceNameViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val cvRoot: CardView = v.findViewById(R.id.cvRoot)
    private val tvAttendanceTitle: TextView = v.findViewById(R.id.tvAttendanceTitle)

    fun bind(name: String) {
        tvAttendanceTitle.text = name
        cvRoot.setOnClickListener {
            Bus.CURRENT_ATTENDANCE_NAME = name
            cvRoot.context.startActivity(
                Intent(cvRoot.context, ScanResultActivity::class.java)
            )
        }
    }
}