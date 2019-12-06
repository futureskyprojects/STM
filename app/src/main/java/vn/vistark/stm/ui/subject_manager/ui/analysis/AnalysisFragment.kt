package vn.vistark.stm.ui.subject_manager.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.StudentObj

class AnalysisFragment : Fragment() {
    lateinit var rvStudentAnalysis: RecyclerView
    var students = ArrayList<StudentObj>()
    lateinit var studentAnalysisAdapter: StudentAnalysisAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_analysis, container, false)
        initViews(root)
        initRecyclerView()
        return root
    }

    private fun initRecyclerView() {
        SubjectStudentsDB(context!!).getBySubject(Bus.SELECTED_SUBJECT).forEach {
            val temp = StudentDB(context!!).getStudent(it.studentId)
            if (temp != null) {
                students.add(temp)
            }
        }
        studentAnalysisAdapter = StudentAnalysisAdapter(students)

        rvStudentAnalysis.layoutManager = LinearLayoutManager(context)
        rvStudentAnalysis.setHasFixedSize(true)

        rvStudentAnalysis.adapter = studentAnalysisAdapter
    }

    private fun initViews(v: View) {
        rvStudentAnalysis = v.findViewById(R.id.rvStudentAnalysis)
    }
}