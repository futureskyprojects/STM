package vn.vistark.stm.data.model

class SubjectStudentsObj(val ssId: Long, val subjectId: Long, val studentId: Long) {
    companion object {
        const val ARG_NAME = "SUBJECT_STUDENT_OBJECT"
        const val TB_NAME = "tb_subjectstudents"
        const val SS_ID = "ss_id"
        const val SUBJECT_ID = "subject_id"
        const val STUDENT_ID = "student_id"
    }
}