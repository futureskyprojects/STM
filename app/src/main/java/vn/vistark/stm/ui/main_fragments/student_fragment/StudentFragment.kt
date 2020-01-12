package vn.vistark.stm.ui.main_fragments.student_fragment


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.dmoral.toasty.Toasty

import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.data.db.AttendanceDB
import vn.vistark.stm.data.db.ClassDB
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.ClassObj
import vn.vistark.stm.data.model.StudentObj
import vn.vistark.stm.ui.main_fragments.BaseFrg
import vn.vistark.stm.ui.main_fragments.class_fragment.ClassAdater

class StudentFragment : Fragment(), BaseFrg {
    lateinit var fabAddNewButton: FloatingActionButton
    lateinit var rvStudents: RecyclerView
    lateinit var tvStudentEmpty: TextView
    lateinit var tvSelectClass: TextView

    var students = ArrayList<StudentObj>()
    lateinit var adapter: StudentAdapter

    var selectedClassObj: ClassObj? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_student, container, false)
        initViews(v)
        initSelectedClassEvent()
        initFabAddNewButtonClick()
        return v
    }

    private fun initFabAddNewButtonClick() {
        fabAddNewButton.setOnClickListener {
            if (selectedClassObj == null) {
                Toasty.warning(context!!, "You must select class first", Toasty.LENGTH_SHORT, false)
                    .show()
            } else {
                val v = LayoutInflater.from(context).inflate(R.layout.add_student, null, false)
                val dialog = AlertDialog.Builder(context).create()

                dialog.setView(v)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.show()
                // Init view

                val edtStudentName: EditText = v.findViewById(R.id.edtStudentName)
                val edtStudentInfo: EditText = v.findViewById(R.id.edtStudentInfo)
                v.findViewById<ImageView>(R.id.ivCloseBtn).setOnClickListener {
                    dialog.dismiss()
                }
                v.findViewById<Button>(R.id.btnConfirmAdd).setOnClickListener {
                    val stdName = edtStudentName.text.toString()
                    val stdInfo = edtStudentInfo.text.toString()

                    if (stdName.isBlank() || stdName.isEmpty()) {
                        Toasty.warning(context!!, "Please input student name", Toasty.LENGTH_SHORT)
                            .show()
                    } else {
                        val idx = System.currentTimeMillis()
                        val stdObj =
                            StudentObj(idx, selectedClassObj!!.id, stdName, stdInfo)

                        if (StudentDB(context!!).addStudent(stdObj)) {
                            Toasty.success(
                                context!!,
                                "Add new Student success",
                                Toasty.LENGTH_SHORT,
                                false
                            ).show()
                            students.add(stdObj)
                            adapter.notifyDataSetChanged()
                        } else {
                            Toasty.error(
                                context!!,
                                "Add new Student fail",
                                Toasty.LENGTH_SHORT,
                                false
                            )
                                .show()
                        }
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun initSelectedClassEvent() {
        tvSelectClass.setOnClickListener {
            val classes = ClassDB(context!!).getAllClassObj()
            if (classes.size <= 0) {
                Toasty.warning(
                    context!!,
                    "You don't have any class now",
                    Toasty.LENGTH_SHORT,
                    false
                )
                    .show()
            } else {
                val v = LayoutInflater.from(context).inflate(R.layout.select_layout, null, false)
                val dialog = AlertDialog.Builder(context).create()
                dialog.setView(v)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                // Init Adapter
                val classAdapter = ClassAdater(classes, null)
                classAdapter.onItemClick = {
                    selectedClassObj = it
                    dialog.dismiss()
                    // Show fabButton for add
                    fabAddNewButton.show()
                    // Load class again
                    initRecyclerView(it.id)
                    // Relpace text show
                    tvSelectClass.text = it.name
                }

                val rvOptionsClass: RecyclerView = v.findViewById(R.id.rvSelect)
                rvOptionsClass.layoutManager = LinearLayoutManager(context)
                rvOptionsClass.setHasFixedSize(true)
                rvOptionsClass.adapter = classAdapter
                //
                v.findViewById<ImageView>(R.id.ivCloseBtn).setOnClickListener {
                    dialog.dismiss()
                }
                /////
                dialog.show()
            }
        }
    }

    private fun initRecyclerView(classId: Long) {
        val temp = StudentDB(context!!).getAllStudent()
        students.clear()

        temp.forEach {
            if (it.classId == classId) {
                students.add((it))
            }
        }

        if (students.size <= 0) {
            Toasty.info(
                context!!,
                "Don't have any student in this class",
                Toasty.LENGTH_SHORT,
                false
            )
                .show()
        }
        adapter = StudentAdapter(students, tvStudentEmpty)

        rvStudents.layoutManager = LinearLayoutManager(context)
        rvStudents.setHasFixedSize(true)

        rvStudents.adapter = adapter

        adapter.onItemClick = {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Remove this student?")
            dialog.setMessage("You can not recovery. Are you sure?")
                .setPositiveButton("Yes") { d, w ->
                    if (StudentDB(context!!).deleteStudent(it)) {
                        students.remove(it)
                        adapter.notifyDataSetChanged()
                        Toasty.error(context!!, "Successful", Toasty.LENGTH_SHORT, false).show()
                    } else {
                        Toasty.error(
                            context!!,
                            "Error, please try again.",
                            Toasty.LENGTH_SHORT,
                            false
                        ).show()
                    }
                }
                .setNegativeButton("No") { d, w ->
                    d.cancel()
                }
            dialog.create()
            dialog.show()
        }

    }

    private fun initViews(v: View) {
        fabAddNewButton = v.findViewById(R.id.fabAddNewStudent)
        rvStudents = v.findViewById(R.id.rvStudents)
        tvStudentEmpty = v.findViewById(R.id.tvStudentEmpty)
        tvSelectClass = v.findViewById(R.id.tvSelectedClassName)
    }

    override fun getBaseName(): String {
        return "Students"
    }

    companion object {
        fun newInstance(): StudentFragment {
            return StudentFragment()
        }
    }

}
