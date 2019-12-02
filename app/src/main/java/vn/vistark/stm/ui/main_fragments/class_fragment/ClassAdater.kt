package vn.vistark.stm.ui.main_fragments.class_fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.stm.R
import vn.vistark.stm.data.model.ClassObj

class ClassAdater(
    private val classes: ArrayList<ClassObj>,
    private val tvClassEmpty: TextView?
) : RecyclerView.Adapter<ClassViewHolder>() {

    var onItemClick: ((ClassObj) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.class_item_layout, parent, false)
        return ClassViewHolder(v)
    }

    override fun getItemCount(): Int {
        return classes.size
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classObj = classes[position]
        holder.bind(classObj)

        holder.lnRoot.setOnClickListener {
            onItemClick?.invoke(classObj)
        }

        if (classes.size > 0) {
            tvClassEmpty?.visibility = View.GONE
        } else {
            tvClassEmpty?.visibility = View.VISIBLE
        }
    }

}