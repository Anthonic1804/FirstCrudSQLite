package com.home.crudsqlite

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private var btnAdd: Button? = null
    private var etName: EditText? = null
    private var etEmail: EditText? = null
    private var rvItemList: RecyclerView? = null
    private var tvNoRecords: TextView? = null
    private var etNameUpdate: EditText? = null
    private var etEmailUpdate: EditText? = null
    private var tvUpdate: TextView? = null
    private var tvCancel: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdd = findViewById(R.id.btnAdd)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        rvItemList = findViewById(R.id.rvItemsList)
        tvNoRecords = findViewById(R.id.tvNoRecords)

        btnAdd!!.setOnClickListener {
            addRecord()
        }

        setupListofDataIntoRecyclerView()
    }

    //FUNCION PARA INSERTAR UN REGISTRO
    private fun addRecord(){
        val name = etName!!.text.toString()
        val email = etEmail!!.text.toString()

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(name.isNotEmpty() && email.isNotEmpty()){
            val status = databaseHandler.addEmployee(EmpModelClass(0, name, email))
            if(status > -1){
                Toast.makeText(applicationContext, "Registro Almacenado", Toast.LENGTH_SHORT).show()

                etName!!.text.clear()
                etEmail!!.text.clear()

                setupListofDataIntoRecyclerView()
            }
        }else{
            Toast.makeText(
                applicationContext,
                "Error al Almacenar el Registro",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //FUNCION PARA CARGAR EL RECYCLER VIEW
    private fun setupListofDataIntoRecyclerView() {
        if(getItemsList().size > 0){
            rvItemList!!.visibility = View.VISIBLE
            tvNoRecords!!.visibility = View.GONE

            rvItemList!!.layoutManager = LinearLayoutManager(this)
            val itemAdapter = ItemAdapter(this, getItemsList())
            rvItemList!!.adapter = itemAdapter
        }
    }

    //FUNCION PARA OBTENER LOS DATOS DE LA TABLA EMPLEADOS
    fun getItemsList(): ArrayList<EmpModelClass>{
        val databaseHandler = DatabaseHandler(this)
        val empList: ArrayList<EmpModelClass> = databaseHandler.viewEmployee()

        return empList
    }

    //FUNCION PARA ACTUALIZAR EL REGISTRO
    fun updateRecordDialog(empModelClass: EmpModelClass){
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        updateDialog.setContentView(R.layout.dialog_update)
         etNameUpdate = updateDialog.findViewById<EditText>(R.id.etNameUpdate)
         etEmailUpdate = updateDialog.findViewById<EditText>(R.id.etEmailUpdate)
         tvUpdate = updateDialog.findViewById(R.id.tvUpdate)
         tvCancel = updateDialog.findViewById(R.id.tvCancel)

         etNameUpdate!!.setText(empModelClass.name)
         etEmailUpdate!!.setText(empModelClass.email)

        tvUpdate!!.setOnClickListener {
            val name = etNameUpdate!!.text.toString()
            val email = etEmailUpdate!!.text.toString()

            val databaseHandler = DatabaseHandler(this)

            if(name.isNotEmpty() && email.isNotEmpty()){
                val status = databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email))
                if(status > -1){
                    Toast.makeText(applicationContext, "Datos Actualizados", Toast.LENGTH_SHORT).show()
                    setupListofDataIntoRecyclerView()
                    updateDialog.dismiss()
                }else{
                    Toast.makeText(
                        applicationContext,
                        "Error al Actualizar el Registro",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        tvCancel!!.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

    //FUNCION PARA ELIMINAR UN REGISTRO
    fun deleteRecordDialog(empModelClass: EmpModelClass){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Registro")
        builder.setMessage("Esta Segudo de Eliminar este Registro ${empModelClass.name}")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){
            dialogInterface, which->

            val databaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "",""))
            if(status > -1){
                Toast.makeText(applicationContext, "Registro Eliminado", Toast.LENGTH_SHORT).show()
                setupListofDataIntoRecyclerView()
            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No"){
            dialogInterface, which->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}