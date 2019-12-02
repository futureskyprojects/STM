package vn.vistark.stm.data.model

class ClassObj() {
    var id: Int = -1
    var name: String = ""
    var description: String = ""

    constructor(id: Int, name: String, description: String) : this() {
        this.id = id
        this.name = name
        this.description = description
    }

    companion object {
        const val TB_NAME = "tb_class"
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
    }
}