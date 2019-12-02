package vn.vistark.stm.data.model

class StudentObj(
    val id: Int,
    var classId: Int,
    var name: String,
    var info: String
) {
    companion object {
        const val TB_NAME = "tb_student"
        const val ID = "id"
        const val CLASS_ID = "class_id"
        const val NAME = "name"
        const val INFO = "info"
    }
}