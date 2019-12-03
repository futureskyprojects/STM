package vn.vistark.stm.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import vn.vistark.stm.data.model.SubjectStudentsObj

class SubjectStudentsDB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "db_ss"
        const val DB_VERSION = 1
    }

    init {
        val subjectStudentsObj = SubjectStudentsObj(1, 1, 1)
        add(subjectStudentsObj)
        delete(subjectStudentsObj)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${SubjectStudentsObj.TB_NAME} (" +
                    "${SubjectStudentsObj.SS_ID} INTEGER NOT NULL PRIMARY KEY," +
                    "${SubjectStudentsObj.SUBJECT_ID} INTEGER NOT NULL," +
                    "${SubjectStudentsObj.STUDENT_ID} INTEGER NOT NULL" +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${SubjectStudentsObj.TB_NAME};")
        onCreate(db)
    }

    fun add(subjectStudentsObj: SubjectStudentsObj): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SubjectStudentsObj.SS_ID, subjectStudentsObj.ssId)
        values.put(SubjectStudentsObj.SUBJECT_ID, subjectStudentsObj.subjectId)
        values.put(SubjectStudentsObj.STUDENT_ID, subjectStudentsObj.studentId)
        val res = db.insert(SubjectStudentsObj.TB_NAME, null, values)
        db.close()
        return res != (-1).toLong()
    }

    fun update(subjectStudentsObj: SubjectStudentsObj): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SubjectStudentsObj.SS_ID, subjectStudentsObj.ssId)
        values.put(SubjectStudentsObj.SUBJECT_ID, subjectStudentsObj.subjectId)
        values.put(SubjectStudentsObj.STUDENT_ID, subjectStudentsObj.studentId)
        val res = db.update(
            SubjectStudentsObj.TB_NAME,
            values,
            "${SubjectStudentsObj.SUBJECT_ID} = ? AND ${SubjectStudentsObj.STUDENT_ID} = ?",
            arrayOf(
                subjectStudentsObj.subjectId.toString(),
                subjectStudentsObj.studentId.toString()
            )
        )
        db.close()
        return res != -1
    }

    fun getAll(): ArrayList<SubjectStudentsObj> {
        val subjectStudentsObjArr: ArrayList<SubjectStudentsObj> = ArrayList()
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${SubjectStudentsObj.TB_NAME};", null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val subjectStudentsObj =
                    SubjectStudentsObj(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2))
                subjectStudentsObjArr.add(subjectStudentsObj)
            }
        }
        return subjectStudentsObjArr
    }

    fun get(id: Long): SubjectStudentsObj? {
        var subjectStudentsObj: SubjectStudentsObj? = null
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${SubjectStudentsObj.TB_NAME} WHERE ${SubjectStudentsObj.SS_ID}=$id",
            null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                subjectStudentsObj = SubjectStudentsObj(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getLong(2)
                )
            }
        }
        return subjectStudentsObj
    }

    fun getBySubject(subjectId: Long): ArrayList<SubjectStudentsObj> {
        val subjectStudentsObjArr: ArrayList<SubjectStudentsObj> = ArrayList()
        val db = this.writableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${SubjectStudentsObj.TB_NAME} WHERE ${SubjectStudentsObj.SUBJECT_ID}=${subjectId};",
            null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val subjectStudentsObj =
                    SubjectStudentsObj(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2))
                subjectStudentsObjArr.add(subjectStudentsObj)
            }
        }
        return subjectStudentsObjArr
    }

    fun delete(subjectStudentsObj: SubjectStudentsObj): Boolean {
        val db = this.writableDatabase
        val res = db.delete(
            SubjectStudentsObj.TB_NAME,
            "${SubjectStudentsObj.SUBJECT_ID} = ? AND ${SubjectStudentsObj.STUDENT_ID} = ?",
            arrayOf(
                subjectStudentsObj.subjectId.toString(),
                subjectStudentsObj.studentId.toString()
            )
        )
        db.close()
        return res != -1
    }


}