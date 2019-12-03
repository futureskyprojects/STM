package vn.vistark.stm.ui.subject_manager.ui.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.stm.R

class AttendanceNameAdapter(val names: ArrayList<String>) :
    RecyclerView.Adapter<AttendanceNameViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceNameViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.attendance_layout, parent, false)
        return AttendanceNameViewHolder(v)
    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onBindViewHolder(holder: AttendanceNameViewHolder, position: Int) {
        holder.bind(names[position])
    }

}