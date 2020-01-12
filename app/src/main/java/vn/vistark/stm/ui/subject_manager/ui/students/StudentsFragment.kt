package vn.vistark.stm.ui.subject_manager.ui.students

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.dmoral.toasty.Toasty
import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.data.db.AttendanceDB
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.StudentObj
import vn.vistark.stm.data.model.SubjectStudentsObj
import vn.vistark.stm.ui.main_fragments.student_fragment.StudentAdapter

class StudentsFragment : Fragment() {
    lateinit var rvStudentsInSubject: RecyclerView
    lateinit var fabAddNewStudentToSubject: FloatingActionButton

    var students = ArrayList<StudentObj>()
    lateinit var studentAdapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_subject_students, container, false)
        initViews(root)
        initRecyclerView()
        initFabAddButton()
        return root
    }

    private fun initRecyclerView() {
        val ss = SubjectStudentsDB(context!!).getBySubject(Bus.SELECTED_SUBJECT)
        val studentDb = StudentDB(context!!)
        ss.forEach {
            val studentObj = studentDb.getStudent(it.studentId)
            if (studentObj != null) {
                var isHave = false
                for (st in students) {
                    if (st.id == studentObj.id)
                        isHave = true
                }
                if (!isHave)
                    students.add((studentObj))
            }
        }
        studentAdapter = StudentAdapter(students, null)

        with(rvStudentsInSubject) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = studentAdapter
        }

        studentAdapter.onItemClick = {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Remove this student?")
            dialog.setMessage("You can not recovery. Are you sure?")
                .setPositiveButton("Yes") { d, w ->
                    val ssDb = SubjectStudentsDB(context!!)
                    val ssObj = ssDb.get(Bus.SELECTED_SUBJECT, it.id)
                    if (ssObj != null) {
                        val attDb = AttendanceDB(context!!)
                        val atts = attDb.getBySsId(ssObj.ssId)
                        if (atts.size > 0) {
                            for (att in atts) {
                                attDb.remove(att)
                            }
                        }

                        if (ssDb.remove(ssObj)) {
                            for (i in 0..students.size) {
                                if (students[i].id == ssObj.studentId) {
                                    students.removeAt(i)
                                    break
                                }
                            }
                        }

                        Toasty.success(context!!, "Successful", Toasty.LENGTH_SHORT, false).show()
                        studentAdapter.notifyDataSetChanged()
                        return@setPositiveButton
                    }
                    Toasty.error(context!!, "Error, please try again.", Toasty.LENGTH_SHORT, false)
                        .show()
                }
                .setNegativeButton("No") { d, w ->
                    d.cancel()
                }
            dialog.create()
            dialog.show()
        }
    }

    private fun initFabAddButton() {
        fabAddNewStudentToSubject.setOnClickListener {
            val v = LayoutInflater.from(context).inflate(R.layout.select_layout, null, false)
            val dialog = AlertDialog.Builder(context).create()
            dialog.setView(v)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
            // Find view
            v.findViewById<ImageView>(R.id.ivCloseBtn).setOnClickListener {
                dialog.dismiss()
            }
            val rvSelect: RecyclerView = v.findViewById(R.id.rvSelect)
            val getStudents = ArrayList<StudentObj>()
            // Init data for select
            StudentDB(context!!).getAllStudent().forEach loop1@{
                students.forEach loop2@{ std ->
                    if (it.id == std.id)
                        return@loop1
                }
                getStudents.add(it)
            }
            if (getStudents.size <= 0) {
                dialog.dismiss()
                Toasty.info(
                    context!!,
                    "All student are in this subject",
                    Toasty.LENGTH_SHORT,
                    false
                ).show()
            }
            val stzAdapter = StudentAdapter(getStudents, null)
            //// Continue rv
            with(rvSelect) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = stzAdapter
            }
            // Init event
            stzAdapter.onItemClick = {
                if (SubjectStudentsDB(context!!).add(
                        SubjectStudentsObj(
                            System.currentTimeMillis(),
                            Bus.SELECTED_SUBJECT,
                            it.id
                        )
                    )
                ) {
                    Toasty.success(
                        context!!,
                        "Add student to this subject success",
                        Toasty.LENGTH_SHORT,
                        false
                    ).show()

                    getStudents.remove(it)
                    stzAdapter.notifyDataSetChanged()

                    students.add(it)
                    studentAdapter.notifyDataSetChanged()

                    if (getStudents.size <= 0)
                        dialog.dismiss()
                } else {
                    Toasty.error(
                        context!!,
                        "Add student to this subject fail",
                        Toasty.LENGTH_SHORT,
                        false
                    ).show()
                }
            }
        }
    }

    private fun initViews(v: View) {
        rvStudentsInSubject = v.findViewById(R.id.rvStudentsInSubject)
        fabAddNewStudentToSubject = v.findViewById(R.id.fabAddNewStudentToSubject)
    }
}