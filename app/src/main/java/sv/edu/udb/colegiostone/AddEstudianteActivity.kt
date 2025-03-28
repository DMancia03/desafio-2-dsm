package sv.edu.udb.colegiostone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sv.edu.udb.colegiostone.modelos.Estudiante
import sv.edu.udb.colegiostone.utils.Accion

class AddEstudianteActivity : AppCompatActivity() {
    // Componentes
    private lateinit var txtNombre : EditText
    private lateinit var txtApellido : EditText
    private lateinit var txtGrado : EditText
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
        btnAceptar = findViewById(R.id.btnAceptar)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Generales
        btnAceptar.setOnClickListener { Guardar() }
        btnCancelar.setOnClickListener { RegresarInicioEstudiante() }

        // Control
        val datos : Bundle? = intent.extras
        if(datos != null){
            accion = datos.getString("accion").toString()
            id = datos.getString("id").toString()
        }
    }

    private fun Guardar(){
        val nombre = txtNombre.text.toString()
        val apellido = txtApellido.text.toString()
        val grado = txtGrado.text.toString()

        db = FirebaseDatabase.getInstance().getReference("estudiantes")

        val estudiante = Estudiante(nombre, apellido, grado)

        if(accion == Accion.agregar){
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

        RegresarInicioEstudiante()
    }

    private fun RegresarInicioEstudiante(){
        finish()
    }
}