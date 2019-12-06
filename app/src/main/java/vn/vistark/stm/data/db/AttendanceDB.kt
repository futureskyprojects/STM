package vn.vistark.stm.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import vn.vistark.stm.data.model.AttendanceObj

class AttendanceDB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "db_attendance"
        const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE ${AttendanceObj.TB_NAME} (" +
                    "${AttendanceObj.ID} INTEGER NOT NULL PRIMARY KEY," +
                    "${AttendanceObj.SS_ID} INTEGER," +
                    "${AttendanceObj.NAME} TEXT," +
                    "${AttendanceObj.IS_ATTENDANCE} INTEGER" +
                    ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${AttendanceObj.TB_NAME};")
        onCreate(db)
    }

    fun getAllName(): ArrayList<String> {
        val names = ArrayList<String>()
        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT ${AttendanceObj.NAME} FROM ${AttendanceObj.TB_NAME};",
            null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val s = cursor.getString(0)
                if (!names.contains(s))
                    names.add(s)
            }
        }
        return names
    }

    fun getByName(name: String): ArrayList<AttendanceObj> {
        val arr = ArrayList<AttendanceObj>()
        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM ${AttendanceObj.TB_NAME} WHERE ${AttendanceObj.NAME} = '$name'",
            null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val attendanceObj = AttendanceObj(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getInt(3) != 0
                )
                arr.add(attendanceObj)
            }
        }
        return arr
    }

    fun getBySsId(ssId: Long): ArrayList<AttendanceObj> {
        val arr = ArrayList<AttendanceObj>()
        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM ${AttendanceObj.TB_NAME} WHERE ${AttendanceObj.SS_ID} = $ssId;",
            null
        )
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val attendanceObj = AttendanceObj(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getInt(3) != 0
                )
                arr.add(attendanceObj)
            }
        }
        return arr
    }

    fun add(attendanceObj: AttendanceObj): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(AttendanceObj.ID, attendanceObj.id)
        values.put(AttendanceObj.SS_ID, attendanceObj.ssId)
        values.put(AttendanceObj.NAME, attendanceObj.name)
        values.put(AttendanceObj.IS_ATTENDANCE, attendanceObj.isAttendance)

        val res = db.insert(AttendanceObj.TB_NAME, null, values)
        db.close()
        return res != (-1).toLong()
    }

    fun update(attendanceObj: AttendanceObj): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(AttendanceObj.ID, attendanceObj.id)
        values.put(AttendanceObj.SS_ID, attendanceObj.ssId)
        values.put(AttendanceObj.NAME, attendanceObj.name)
        values.put(AttendanceObj.IS_ATTENDANCE, attendanceObj.isAttendance)

        val res = db.update(
            AttendanceObj.TB_NAME,
            values,
            "${AttendanceObj.ID}=?",
            arrayOf(attendanceObj.id.toString())
        )
        db.close()
        return res != -1
    }

    fun delete(attendanceObj: AttendanceObj): Boolean {
        val db = this.writableDatabase
        val res = db.delete(
            AttendanceObj.TB_NAME, "${AttendanceObj.ID}=?",
            arrayOf(attendanceObj.id.toString())
        )
        db.close()
        return res != -1
    }
}