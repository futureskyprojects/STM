package vn.vistark.stm.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import vn.vistark.stm.data.model.SubjectObj

class SubjectDB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "subject_db"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS ${SubjectObj.TB_NAME} (" +
                    "${SubjectObj.ID} INTEGER NOT NULL PRIMARY KEY," +
                    "${SubjectObj.NAME} TEXT," +
                    "${SubjectObj.DESCRIPTION} TEXT" +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${SubjectObj.TB_NAME};")
        onCreate(db)
    }

    fun addSubject(subjectObj: SubjectObj): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SubjectObj.ID, subjectObj.id)
        values.put(SubjectObj.NAME, subjectObj.name)
        values.put(SubjectObj.DESCRIPTION, subjectObj.description)

        val res = db.insert(SubjectObj.TB_NAME, null, values)
        db.close()
        return res != (-1).toLong()
    }

    fun deleteSubject(subjectObj: SubjectObj): Boolean {
        val db = this.writableDatabase
        val res =
            db.delete(SubjectObj.TB_NAME, "${SubjectObj.ID}=?", arrayOf(subjectObj.id.toString()))
        return res != -1
    }

    fun updateSubject(subjectObj: SubjectObj): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(SubjectObj.ID, subjectObj.id)
        values.put(SubjectObj.NAME, subjectObj.name)
        values.put(SubjectObj.DESCRIPTION, subjectObj.description)

        val res = db.update(
            SubjectObj.TB_NAME,
            values,
            "${SubjectObj.ID}=?",
            arrayOf(subjectObj.id.toString())
        )

        return res != -1
    }

    fun getSubject(id: Long): SubjectObj? {
        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM ${SubjectObj.TB_NAME} WHERE ${SubjectObj.ID}=$id;",
            emptyArray<String>()
        )
        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val subject = SubjectObj(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                return subject
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return null
    }


    fun getAllSubjects(): ArrayList<SubjectObj> {
        val db = this.readableDatabase
        val subjects = ArrayList<SubjectObj>()

        val cursor = db.rawQuery("SELECT * FROM ${SubjectObj.TB_NAME};", emptyArray<String>())
        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val subject = SubjectObj(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                subjects.add(subject)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return subjects
    }
}