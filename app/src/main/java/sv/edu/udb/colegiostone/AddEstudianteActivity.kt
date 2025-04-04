package sv.edu.udb.colegiostone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sv.edu.udb.colegiostone.modelos.Estudiante
import sv.edu.udb.colegiostone.utils.Accion
import sv.edu.udb.colegiostone.utils.MateriaHelper
import sv.edu.udb.colegiostone.utils.ToastHelper

class AddEstudianteActivity : AppCompatActivity() {
    // Componentes
    private lateinit var lbBienvenida : TextView
    private lateinit var txtNombre : EditText
    private lateinit var txtApellido : EditText
    private lateinit var txtGrado : EditText
    //private lateinit var txtMateria : EditText
    private lateinit var spMateria : Spinner
    private lateinit var txtNotaFinal : EditText
    private lateinit var btnAceptar : Button
    private lateinit var btnCancelar : Button
    // Base de datos
    private lateinit var db : DatabaseReference
    // Control
    private var id : String = ""
    private var accion : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_estudiante)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Inicializar()
    }

    private fun Inicializar(){
        // Componentes
        txtNombre = findViewById(R.id.txtNombreEstudiante)
        txtApellido = findViewById(R.id.txtApellidoEstudiante)
        txtGrado = findViewById(R.id.txtGradoEstudiante)
        spMateria = findViewById(R.id.spMateriaEstudiante)
        txtNotaFinal = findViewById(R.id.txtNotaFinalEstudiante)
        btnAceptar = findViewById(R.id.btnAceptar)
        btnCancelar = findViewById(R.id.btnCancelar)
        lbBienvenida = findViewById<TextView>(R.id.lbBienvenida)

        // Generales
        btnAceptar.setOnClickListener { Guardar() }
        btnCancelar.setOnClickListener { RegresarInicioEstudiante() }

        // Spinner materias
        ArrayAdapter.createFromResource(
            this,
            R.array.materias,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spMateria.adapter = adapter
        }

        // Datos enviados en el intent
        ObtenerDatosIntent()

        // Si la accion es editar
        if(accion == Accion.editar){
            lbBienvenida.setText("Editando nota")
        }
    }

    private fun ObtenerDatosIntent(){
        val datos : Bundle? = intent.extras
        if(datos != null){
            // Accion que realizara
            if(!datos.getString("accion").isNullOrEmpty()){
                accion = datos.getString("accion").toString()
            }

            // Id del registro si tiene
            if(!datos.getString("id").isNullOrEmpty()){
                id = datos.getString("id").toString()
            }

            // Valores que son enviados para escribirse en los txt
            if(!datos.getString("nombre").isNullOrEmpty()){
                txtNombre.setText(datos.getString("nombre").toString())
            }

            if(!datos.getString("apellido").isNullOrEmpty()){
                txtApellido.setText(datos.getString("apellido").toString())
            }

            txtGrado.setText(datos.getInt("grado").toString())

            if(!datos.getString("materia").isNullOrEmpty()){
                val materia : String = datos.getString("materia").toString()
                spMateria.setSelection(MateriaHelper.ObtenerIndiceMateria(materia))
            }

            if(!datos.getDouble("notaFinal").isNaN()){
                txtNotaFinal.setText(datos.getDouble("notaFinal").toString())
            }
        }
    }

    private fun Guardar(){
        val nombre = txtNombre.text.toString()
        val apellido = txtApellido.text.toString()
        val gradoText = txtGrado.text.toString()
        val materia = spMateria.selectedItem.toString()
        val notaFinalText = txtNotaFinal.text.toString()

        if(nombre.isNullOrEmpty()){
            ToastHelper.ToastSimple(this, "Debe ingresar un nombre")
            return
        }

        if(apellido.isNullOrEmpty()){
            ToastHelper.ToastSimple(this, "Debe ingresar un apellido")
            return
        }

        if(gradoText.isNullOrEmpty()){
            ToastHelper.ToastSimple(this, "Debe ingresar un grado")
            return
        }
        else if(gradoText.toIntOrNull() == null){
            ToastHelper.ToastSimple(this, "Debe ingresar un grado válido")
            return
        }

        val grado = gradoText.toInt()

        if(grado < 1 || grado > 9){
            ToastHelper.ToastSimple(this, "Debe ingresar un grado entre 1° y 9°")
            return
        }

        if(materia.isNullOrEmpty()){
            ToastHelper.ToastSimple(this, "Debe ingresar una materia")
            return
        }

        if(notaFinalText.isNullOrEmpty()){
            ToastHelper.ToastSimple(this, "Debe ingresar una nota Final")
            return
        }
        else if(notaFinalText.toDoubleOrNull() == null){
            ToastHelper.ToastSimple(this, "Debe ingresar una nota final válida")
            return
        }

        val notaFinal : Double = notaFinalText.toDouble()

        if(notaFinal < 0 || notaFinal > 10){
            ToastHelper.ToastSimple(this, "Debe ingresar una nota final entre 0 y 10")
            return
        }

        db = FirebaseDatabase.getInstance().getReference(Accion.estudiantes)

        val estudiante = Estudiante(nombre, apellido, grado, materia, notaFinal)

        when(accion){
            Accion.agregar -> {
                val newKey = db.push().key
                if(newKey != null){
                    db.child(newKey).setValue(estudiante).addOnSuccessListener {
                        Toast.makeText(this, "Se guardó exitosamente!!", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "Error al generar una clave", Toast.LENGTH_LONG).show()
                }
            }
            Accion.editar -> {
                if(id.isNotEmpty()){
                    val registro_actualizar = hashMapOf<String, Any>(
                        id to estudiante.toMap()
                    )

                    db.updateChildren(registro_actualizar).addOnSuccessListener {
                        Toast.makeText(this, "Se actualizó exitosamente!!", Toast.LENGTH_LONG).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this, "No se encontro el id del registro", Toast.LENGTH_LONG).show()
                }
            }
        }

        RegresarInicioEstudiante()
    }

    private fun RegresarInicioEstudiante(){
        finish()
    }
}