package vn.vistark.stm.ui.subject_manager.ui.attendance

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.data.db.AttendanceDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.AttendanceObj
import vn.vistark.stm.ui.subject_manager.ui.attendance.scan.ScanResultActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AttendanceFragment : Fragment() {
    lateinit var rvAttendances: RecyclerView
    lateinit var fabAddNewAttendance: FloatingActionButton

    var attendanceNames = ArrayList<String>()
    lateinit var nameAdapter: AttendanceNameAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_attendance, container, false)
        initViews(root)
        initRecyclerView()
        initFabButton()
        return root
    }

    private fun initFabButton() {
        fabAddNewAttendance.setOnClickListener {
            val calendar = Calendar.getInstance()
            var name = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                    "${calendar.get(Calendar.MONTH) + 1}/" +
                    "${calendar.get(Calendar.YEAR)}"
            for (i in 1..100) {
                val temp = "$name - ($i)"
                if (!attendanceNames.contains(temp)) {
                    name = temp
                    break
                }
            }
            Bus.CURRENT_ATTENDANCE_NAME = name
            attendanceNames.add(name)
            nameAdapter.notifyDataSetChanged()
            // From AttendanceNameViewHolder
            val intent = Intent(context, ScanResultActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        AttendanceDB(context!!).getAllName().forEach {
            val temp = AttendanceDB(context!!).getByName(it)
            if (temp.size > 0) {
                val temp2 = SubjectStudentsDB(context!!).get(temp[0].ssId)
                if (temp2 != null && temp2.subjectId == Bus.SELECTED_SUBJECT) {
                    attendanceNames.add(it)
                }
            }
        }
        nameAdapter = AttendanceNameAdapter(attendanceNames)

        rvAttendances.layoutManager = LinearLayoutManager(context)
        rvAttendances.setHasFixedSize(true)
        rvAttendances.adapter = nameAdapter
    }

    private fun initViews(v: View) {
        rvAttendances = v.findViewById(R.id.rvAttendances)
        fabAddNewAttendance = v.findViewById(R.id.fabNewAttendance)
    }


}