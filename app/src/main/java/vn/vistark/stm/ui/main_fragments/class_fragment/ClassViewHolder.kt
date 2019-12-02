package vn.vistark.stm.ui.main_fragments.class_fragment

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.class_item_layout.view.*
import vn.vistark.stm.R
import vn.vistark.stm.data.model.ClassObj

class ClassViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    val lnRoot: LinearLayout = v.findViewById(R.id.lnRoot)
    val tvFirstChar: TextView = v.findViewById(R.id.tvFirstChar)
    val tvClassName: TextView = v.findViewById(R.id.tvClassName)

    fun bind(classObj: ClassObj) {
        tvClassName.text = classObj.name
        tvFirstChar.text = classObj.name
    }
}