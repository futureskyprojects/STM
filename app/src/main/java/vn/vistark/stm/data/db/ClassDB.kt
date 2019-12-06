package vn.vistark.stm.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import vn.vistark.stm.data.model.ClassObj

class ClassDB(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "class_db"
        const val DB_VERSION = 1

    }


    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE IF NOT EXISTS ${ClassObj.TB_NAME}(" +
                "${ClassObj.ID} INTEGER PRIMARY KEY," +
                "${ClassObj.NAME} TEXT," +
                "${ClassObj.DESCRIPTION} TEXT" +
                ");"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${ClassObj.TB_NAME};")
        onCreate(db)
    }

    fun addClassObj(classObj: ClassObj): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ClassObj.ID, classObj.id)
        values.put(ClassObj.NAME, classObj.name)
        values.put(ClassObj.DESCRIPTION, classObj.description)
        val isSuccess = db.insert(ClassObj.TB_NAME, null, values)
        db.close()
        return isSuccess != (-1).toLong()
    }

    fun updateClassObj(classObj: ClassObj): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ClassObj.ID, classObj.id)
        values.put(ClassObj.NAME, classObj.name)
        values.put(ClassObj.DESCRIPTION, classObj.description)
        val isSuccess =
            db.update(ClassObj.TB_NAME, values, "${ClassObj.ID}=?", arrayOf(classObj.id.toString()))
        db.close()
        return isSuccess != -1
    }

    fun getAllClassObj(): ArrayList<ClassObj> {
        val classObjs = ArrayList<ClassObj>()
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM ${ClassObj.TB_NAME};", emptyArray<String>())
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val classObj = ClassObj(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                classObjs.add(classObj)
            }
        }
        cursor.close()
        db.close()
        return classObjs
    }

    fun deleteClassObj(classObj: ClassObj): Boolean {
        val db = this.writableDatabase
        val isSuccess =
            db.delete(ClassObj.TB_NAME, "${ClassObj.ID}=?", arrayOf(classObj.id.toString()))
        db.close()
        return isSuccess != -1
    }
}