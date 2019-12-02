package vn.vistark.stm.ui.main_fragments.subject_fragment


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
import vn.vistark.stm.data.db.SubjectDB
import vn.vistark.stm.data.model.SubjectObj
import vn.vistark.stm.ui.main_fragments.BaseFrg

class SubjectFragment : Fragment(), BaseFrg {
    lateinit var fabAddNewSubject: FloatingActionButton
    lateinit var rvSubjects: RecyclerView
    lateinit var tvEmptySubject: TextView

    var subjects = ArrayList<SubjectObj>()
    lateinit var adapter: SubjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_subject, container, false)
        initViews(v)
        initRecyvlerView()
        initFabAdd()
        return v
    }

    private fun initFabAdd() {
        fabAddNewSubject.setOnClickListener {
            val v = LayoutInflater.from(context).inflate(R.layout.add_subject, null, false)
            val dialog = AlertDialog.Builder(context!!).create()
            dialog.setView(v)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
            // Init view
            val edtSubjectName: EditText = v.findViewById(R.id.edtSubjectName)
            val edtSubjectDescription: EditText = v.findViewById(R.id.edtSubjectDescription)
            v.findViewById<ImageView>(R.id.ivCloseBtn).setOnClickListener {
                dialog.dismiss()
            }

            v.findViewById<Button>(R.id.btnConfirmAdd).setOnClickListener {
                val subjectName = edtSubjectName.text.toString()
                val subjectDes = edtSubjectDescription.text.toString()

                if (subjectName.isEmpty() || subjectName.isBlank()) {
                    Toasty.warning(
                        context!!,
                        "Please input subject name",
                        Toasty.LENGTH_SHORT,
                        false
                    ).show()
                    dialog.dismiss()
                } else {
                    val idx = System.currentTimeMillis()
                    val subject = SubjectObj(idx, subjectName, subjectDes)
                    if (SubjectDB(context!!).addSubject(subject)) {
                        Toasty.success(
                            context!!,
                            "Add new subject success",
                            Toasty.LENGTH_SHORT,
                            false
                        ).show()
                        subjects.add(subject)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toasty.error(
                            context!!,
                            "Add new subject fail",
                            Toasty.LENGTH_SHORT,
                            false
                        ).show()
                    }
                    dialog.dismiss()
                }
            }
        }
    }

    private fun initRecyvlerView() {
        subjects = SubjectDB(context!!).getAllSubjects()
        adapter = SubjectAdapter(subjects, tvEmptySubject)

        rvSubjects.layoutManager = LinearLayoutManager(context)
        rvSubjects.setHasFixedSize(true)

        rvSubjects.adapter = adapter
    }

    private fun initViews(v: View) {
        fabAddNewSubject = v.findViewById(R.id.fabAddNewSubject)
        rvSubjects = v.findViewById(R.id.rvSubjects)
        tvEmptySubject = v.findViewById(R.id.tvSubjectEmpty)
    }


    override fun getBaseName(): String {
        return "Subjects"
    }

    companion object {
        fun newInstance(): SubjectFragment {
            return SubjectFragment()
        }
    }
}
