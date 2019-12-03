package vn.vistark.stm.data.model

class AttendanceObj(
    val id: Long,
    val ssId: Long,
    val name: String,
    val isAttendance: Boolean
) {
    companion object {
        const val ARG_NAME = "ATTENDANCE_OBJECT"
        const val TB_NAME = "tb_attendance"
        const val ID = "id"
        const val SS_ID = "ss_id"
        const val NAME = "name"
        const val IS_ATTENDANCE = "is_attendance"
    }
}