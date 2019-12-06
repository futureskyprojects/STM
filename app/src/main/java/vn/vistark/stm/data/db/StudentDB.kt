package vn.vistark.stm.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import vn.vistark.stm.data.model.StudentObj

class StudentDB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "student_db"
        const val DB_VERSION = 1
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE IF NOT EXISTS ${StudentObj.TB_NAME}(" +
                "${StudentObj.ID} INTEGER PRIMARY KEY," +
                "${StudentObj.CLASS_ID} INTEGER," +
                "${StudentObj.NAME} TEXT," +
                "${StudentObj.INFO} TEXT" +
                ");"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${StudentObj.TB_NAME}")
        onCreate(db)
    }

    fun addStudent(studentObj: StudentObj): Boolean {
        val values = ContentValues()
        values.put(StudentObj.ID, studentObj.id)
        values.put(StudentObj.CLASS_ID, studentObj.classId)
        values.put(StudentObj.NAME, studentObj.name)
        values.put(StudentObj.INFO, studentObj.info)

        val db = this.writableDatabase
        val res = db.insert(StudentObj.TB_NAME, null, values)
        db.close()
        return res != (-1).toLong()
    }

    fun updateStudent(studentObj: StudentObj): Boolean {
        val values = ContentValues()
        values.put(StudentObj.ID, studentObj.id)
        values.put(StudentObj.CLASS_ID, studentObj.classId)
        values.put(StudentObj.NAME, studentObj.name)
        values.put(StudentObj.INFO, studentObj.info)

        val db = this.writableDatabase
        val res = db.update(
            StudentObj.TB_NAME,
            values,
            "${StudentObj.ID}=?",
            arrayOf(studentObj.id.toString())
        )
        db.close()
        return res != -1
    }

    fun deleteStudent(studentObj: StudentObj): Boolean {
        val db = this.writableDatabase
        val res =
            db.delete(StudentObj.TB_NAME, "${StudentObj.ID}=?", arrayOf(studentObj.id.toString()))
        db.close()
        return res != -1
    }

    fun deleteStudentInClass(classId: Int): Boolean {
        val db = this.writableDatabase
        val res =
            db.delete(StudentObj.TB_NAME, "${StudentObj.CLASS_ID}=?", arrayOf(classId.toString()))
        db.close()
        return res != -1
    }

    fun getStudent(studentId: Long): StudentObj? {
        val db = this.readableDatabase
        var studentObj: StudentObj? = null
        val cursor =
            db.rawQuery(
                "SELECT * FROM ${StudentObj.TB_NAME} WHERE ${StudentObj.ID} = $studentId;",
                null
            )
        if (cursor != null) {
            cursor.moveToFirst()
            studentObj = StudentObj(
                cursor.getLong(0),
                cursor.getLong(1),
                cursor.getString(2),
                cursor.getString(3)
            )
        }
        return studentObj
    }

    fun getAllStudent(): ArrayList<StudentObj> {
        val db = this.readableDatabase
        val arr = ArrayList<StudentObj>()
        val cursor = db.rawQuery("SELECT * FROM ${StudentObj.TB_NAME};", emptyArray<String>())
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val std = StudentObj(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getString(3)
                )
                arr.add(std)
            }
        }
        cursor.close()
        db.close()
        return arr
    }
}