package vn.vistark.stm.ui.subject_manager.ui.attendance.scan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.Result
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_scan.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import vn.vistark.stm.R
import vn.vistark.stm.data.Bus
import vn.vistark.stm.data.db.AttendanceDB
import vn.vistark.stm.data.db.StudentDB
import vn.vistark.stm.data.db.SubjectStudentsDB
import vn.vistark.stm.data.model.AttendanceObj
import vn.vistark.stm.data.model.StudentObj
import vn.vistark.stm.utils.QRLink


class ScanResultActivity : AppCompatActivity() {
    companion object {
        val SCANNED_RESULT_CODE = 1234
    }

    var pressedMillis = System.currentTimeMillis()
    var attendances = ArrayList<AttendanceObj>()
    var students = ArrayList<StudentObj>()
    lateinit var scanAdapter: ScanAttendanceAdapter

    val CAMERA_PERMISSION = 1998

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val apiVersion = Build.VERSION.SDK_INT
        if (apiVersion >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission()
            }
        }

        initBaseEvent()
        initTookAttendanceRecyclerView()
        initScanButton()
        initApplyButton()
    }

    private fun initApplyButton() {
        fabApply.setOnClickListener {
            students.forEach {
                val subjectStudent = SubjectStudentsDB(this).get(Bus.SELECTED_SUBJECT, it.id)
                if (subjectStudent != null) {
                    val tempAttendanceObj = AttendanceObj(
                        System.currentTimeMillis(),
                        subjectStudent.ssId,
                        Bus.CURRENT_ATTENDANCE_NAME,
                        false
                    )
                    for (attendance in attendances) {
                        if (attendance.ssId == tempAttendanceObj.ssId && attendance.name == tempAttendanceObj.name) {
                            return@forEach
                        }
                    }
                    attendances.add(tempAttendanceObj)
                    scanAdapter.notifyDataSetChanged()
                }
            }
            /// Save to database
            val attendanceDB = AttendanceDB(this)
            var count = 0;
            attendances.forEach {
                if (attendanceDB.add(it)) count++
            }
            Toasty.success(this, "Saved $count/${attendances.size}", Toasty.LENGTH_SHORT, false)
                .show()
            finish()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION
        )
    }

    private fun initScanButton() {
        fabScan.setOnClickListener {
            startActivityForResult(
                Intent(this, Scan::class.java),
                SCANNED_RESULT_CODE
            )
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - pressedMillis > 1000) {
            Toasty.warning(
                this, "One more to back. If back, all data scanned will gone.",
                Toasty.LENGTH_SHORT, false
            ).show()
        } else {
            finish()
        }
        pressedMillis = System.currentTimeMillis()
    }

    private fun initBaseEvent() {
        tvTitle.text = "SCAN STUDENT's QR"
        ivBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initTookAttendanceRecyclerView() {
        for (subjectStudentsObj in SubjectStudentsDB(this).getBySubject(Bus.SELECTED_SUBJECT)) {
            val student = StudentDB(this).getStudent(subjectStudentsObj.studentId)
            if (student != null)
                students.add(student)
        }
        //--------------------
        attendances = AttendanceDB(this).getByName(Bus.CURRENT_ATTENDANCE_NAME)
        scanAdapter = ScanAttendanceAdapter(attendances)

        rvAttendances.layoutManager = LinearLayoutManager(this)
        rvAttendances.setHasFixedSize(true)
        rvAttendances.adapter = scanAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION && grantResults.isNotEmpty()) {
            val cameraAccepted =
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (cameraAccepted) {
                Toasty.success(this, "Permission granted success!", Toasty.LENGTH_SHORT, false)
                    .show()
            } else {
                Toasty.error(this, "Permission granted fail, Exit now!", Toasty.LENGTH_SHORT, false)
                    .show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SCANNED_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val myRes = QRLink.decode(this, data.getStringExtra(Scan.STUDENT_ID)!!)
                    if (myRes.studentObj == null) {
                        Toasty.error(this, "This student not available", Toasty.LENGTH_SHORT, false)
                            .show()
                    } else if (myRes.classObj == null) {
                        Toasty.error(
                            this,
                            "Class of this student is not exists",
                            Toasty.LENGTH_SHORT,
                            false
                        ).show()
                    } else {
                        val studentSubjectObj =
                            SubjectStudentsDB(this).get(Bus.SELECTED_SUBJECT, myRes.studentObj!!.id)
                        if (studentSubjectObj != null) {
                            val tempAttendanceObj = AttendanceObj(
                                System.currentTimeMillis(),
                                studentSubjectObj.ssId,
                                Bus.CURRENT_ATTENDANCE_NAME,
                                true
                            )
                            for (attendance in attendances) {
                                if (attendance.ssId == tempAttendanceObj.ssId && attendance.name == tempAttendanceObj.name) {
                                    Toasty.warning(
                                        this,
                                        "This student was attendance",
                                        Toasty.LENGTH_SHORT,
                                        false
                                    ).show()
                                    return
                                }
                            }
                            attendances.add(tempAttendanceObj)
                            // Update result here
                            scanAdapter.notifyDataSetChanged()
                            // Announcement
                            Toasty.success(
                                this,
                                "Student ${myRes.studentObj.name} (${myRes.classObj?.name}) took attendance"
                                , Toasty.LENGTH_SHORT, false
                            ).show()
                        } else {
                            Toasty.error(
                                this,
                                "Sorry, this subject not contain this student",
                                Toasty.LENGTH_SHORT,
                                false
                            ).show()
                        }
                    }
                } else {
                    Toasty.error(this, "Can not get info", Toasty.LENGTH_SHORT, false).show()
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toasty.error(this, "Scan fail", Toasty.LENGTH_SHORT, false).show()
            }
        }
    }
}
