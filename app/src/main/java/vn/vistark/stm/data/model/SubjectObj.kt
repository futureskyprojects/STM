package vn.vistark.stm.data.model

import java.io.Serializable

class SubjectObj(
    val id: Long,
    val name: String,
    val description: String
) : Serializable {
    companion object {
        const val ARG_NAME = "SUBJECT_OBJECT"
        const val TB_NAME = "tb_subject"
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
    }
}