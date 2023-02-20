package com.home.crudsqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EmployeeDatabase"
        private const val TABLE_CONTACTS = "EmployeeTable"

        //CAMPOS DE LA TABLE EMPLOYEETABLE
        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

    //CREANDO LA TABLA EMPLOYEE
    override fun onCreate(db: SQLiteDatabase?) {
        val createContactsTable = (
                "CREATE TABLE $TABLE_CONTACTS (" +
                        "$KEY_ID INTEGER PRIMARY KEY," +
                        "$KEY_NAME TEXT," +
                        "$KEY_EMAIL TEXT)"
                )
        db?.execSQL(createContactsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(
            "DROP TABLE IF EXISTS $TABLE_CONTACTS"
        )
        onCreate(db)
    }

    //FUNCION PARA INSERTAR EMPLEADO
    fun addEmployee(emp: EmpModelClass): Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.email)

        //INSERTANDO DATOS
        val success = db.insert(TABLE_CONTACTS, null, contentValues)

        db.close()
        return success
    }

    //FUNCION PARA LEER LA INFORMACION DE LA TABLA
    fun viewEmployee(): ArrayList<EmpModelClass>{
        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()

        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e:SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0)
                name = cursor.getString(1)
                email = cursor.getString(2)

                val emp = EmpModelClass(
                    id = id,
                    name = name,
                    email = email
                )
                empList.add(emp)
            }while (cursor.moveToNext())
        }
        return empList
    }

    //FUNCION PARA ACTUALIZAR
    fun updateEmployee(emp:EmpModelClass): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)

        //ACTUALIZANDO
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + emp.id, null)
        db.close()
        return success
    }

    //FUNCION PARA ELIMINAR
    fun deleteEmployee(emp:EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)

        //ELIMINANDO
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + emp.id, null)
        db.close()
        return success
    }

}