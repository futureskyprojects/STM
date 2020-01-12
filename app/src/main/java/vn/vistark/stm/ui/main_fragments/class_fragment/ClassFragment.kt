package vn.vistark.stm.ui.main_fragments.class_fragment


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_class.*

import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.data.db.AttendanceDB
import vn.vistark.stm.data.db.ClassDB
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.ClassObj
import vn.vistark.stm.data.model.StudentObj
import vn.vistark.stm.data.model.SubjectObj
import vn.vistark.stm.data.model.SubjectStudentsObj
import vn.vistark.stm.ui.main_fragments.BaseFrg

class ClassFragment : Fragment(), BaseFrg {
    var classes = ArrayList<ClassObj>()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ClassAdater
    lateinit var classEmpty: TextView
    lateinit var fabAddNewClass: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_class, container, false)
        initViews(v)
        initAdapter()
        initRecyclerView()
        initFabAddNewClassBtn()
        return v
    }

    private fun initFabAddNewClassBtn() {
        fabAddNewClass.setOnClickListener {
            val v = LayoutInflater.from(context!!).inflate(R.layout.add_class, null, false)
            val dialog = AlertDialog.Builder(context).create()
            dialog.setView(v)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            // For view
            val edtClassName: EditText = v.findViewById(R.id.edtClassName)
            val edtDescription: EditText = v.findViewById(R.id.edtDescription)
            v.findViewById<ImageView>(R.id.ivCloseBtn).setOnClickListener {
                dialog.dismiss()
            }
            v.findViewById<Button>(R.id.btnConfirmAdd).setOnClickListener {
                val className = edtClassName.text.toString()
                val description = edtDescription.text.toString()
                if (className.isEmpty() || className.isBlank()) {
                    Toasty.warning(context!!, "Please input class name", Toasty.LENGTH_SHORT, false)
                        .show()
                } else {
                    val timestamp = System.currentTimeMillis()
                    val classObj = ClassObj(timestamp, className, description)
                    if (ClassDB(context!!).addClassObj(classObj)) {
                        Toasty.success(
                            context!!,
                            "Add class named [$className] success",
                            Toast.LENGTH_SHORT,
                            false
                        ).show()
                        classes.add(classObj)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toasty.error(
                            context!!,
                            "Add class named [$className] fail",
                            Toast.LENGTH_SHORT,
                            false
                        ).show()
                    }
                    dialog.dismiss()
                }
            }

            //
            dialog.show()
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private fun initAdapter() {
        classes = ClassDB(context!!).getAllClassObj()
        adapter = ClassAdater(classes, classEmpty)

        adapter.onItemClick = {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Remove this student?")
            dialog.setMessage("You can not recovery. Are you sure?")
                .setPositiveButton("Yes") { d, w ->
                    // data base
                    val attDb = AttendanceDB(context!!)
                    val ssDb = SubjectStudentsDB(context!!)
                    val stDb = StudentDB(context!!)

                    for (st in stDb.getAllStudent()) {
                        for (ss in ssDb.getAll()) {
                            if (ss.studentId == st.id) {
                                for (atts in attDb.getBySsId(ss.ssId)) {
                                    attDb.remove(atts)
                                }
                                ssDb.remove(ss)
                            }
                        }
                        stDb.deleteStudent(st)
                    }

                    ClassDB(context!!).deleteClassObj(it)
                    classes.remove(it)
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton("No") { d, w ->
                    d.cancel()
                }
            dialog.create()
            dialog.show()
        }
    }

    private fun initViews(v: View) {
        classEmpty = v.findViewById(R.id.tvClassEmpty)
        recyclerView = v.findViewById(R.id.rvClasses)
        fabAddNewClass = v.findViewById(R.id.fabAddNewClass)
    }

    companion object {
        fun newInstance(): Fragment {
            return ClassFragment()
        }
    }

    override fun getBaseName(): String {
        return "Classes"
    }
}
