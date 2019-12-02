package vn.vistark.stm.data.db

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
        val sql = "CREATE TABLE ${StudentObj.TB_NAME}(" +
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
}