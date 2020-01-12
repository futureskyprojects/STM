package vn.vistark.stm.ui.subject_manager.ui.attendance

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty
import vn.vistark.stm.R
import vn.vistark.stm.data.db.AttendanceDB

class AttendanceNameAdapter(private val names: ArrayList<String>) :
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
        val attName = names[position]
        holder.bind(attName)
        holder.cvRoot.setOnLongClickListener {
            val confirmDialog = AlertDialog.Builder(holder.cvRoot.context)
            confirmDialog.setTitle("Delete this attendance?")
            confirmDialog.setMessage("You can not recovery this task. Are you sure?")
            confirmDialog.setPositiveButton("Yes") { d, w ->
                val db = AttendanceDB(holder.cvRoot.context)
                val attObj = db.getByName(attName)
                if (attObj.size > 0) {
                    var isFullSuccess = true
                    for (att in attObj) {
                        if (!db.remove(att))
                            isFullSuccess = false
                    }
                    var msg = "Successful"
                    if (!isFullSuccess) {
                        msg = "Some record can't remove safety! Please try again"
                    }

                    names.removeAt(position)
                    this.notifyDataSetChanged()

                    Toasty.success(
                        holder.cvRoot.context,
                        msg,
                        Toasty.LENGTH_SHORT,
                        false
                    ).show()
                    return@setPositiveButton
                }
                Toasty.error(
                    holder.cvRoot.context,
                    "Can not remove. Please try again!",
                    Toasty.LENGTH_SHORT,
                    false
                ).show()
            }
            confirmDialog.setNegativeButton("No") { d, w ->
                d.dismiss()
            }
            confirmDialog.create()
            confirmDialog.show()
            return@setOnLongClickListener true
        }
    }

}