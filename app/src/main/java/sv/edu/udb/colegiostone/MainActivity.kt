package sv.edu.udb.colegiostone

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import sv.edu.udb.colegiostone.modelos.Estudiante
import sv.edu.udb.colegiostone.utils.Accion
import sv.edu.udb.colegiostone.utils.ToastHelper

class MainActivity : AppCompatActivity() {
    var consulta : Query = refEstudiante.orderByChild("nombre")

    private var estudiantes : MutableList<Estudiante>? = null

    lateinit var lista_estudiantes : ListView

    private lateinit var btnAgregar : Button
    private lateinit var btnCerrarSesion : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Inicializar()
    }

    private fun Inicializar(){
        // Boton de crear
        btnAgregar = findViewById(R.id.btnAgregarEstudiante)
        btnAgregar.setOnClickListener {
            val intent_add = Intent(this, AddEstudianteActivity::class.java)
            intent_add.putExtra("accion", Accion.agregar)
            startActivity(intent_add)
        }

        // Boton para cerrar sesion
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnCerrarSesion.setOnClickListener {
            CerrarSesion()
        }

        lista_estudiantes = findViewById(R.id.ListaEstudiantes)

        lista_estudiantes.setOnItemClickListener(object  : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val intent_agregar = Intent(this@MainActivity, AddEstudianteActivity::class.java)
                intent_agregar.putExtra("accion", Accion.editar)
                intent_agregar.putExtra("id", estudiantes!![p2].key)
                intent_agregar.putExtra("nombre", estudiantes!![p2].nombre)
                intent_agregar.putExtra("apellido", estudiantes!![p2].apellido)
                intent_agregar.putExtra("grado", estudiantes!![p2].grado)
                intent_agregar.putExtra("materia", estudiantes!![p2].materia)
                intent_agregar.putExtra("notaFinal", estudiantes!![p2].notaFinal)
                startActivity(intent_agregar)
            }
        })

        lista_estudiantes.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ): Boolean {
                val ad = AlertDialog.Builder(this@MainActivity)
                ad.setMessage("¿Quieres eliminar al estudiante ${estudiantes!![p2].nombre}?")
                    .setTitle("Eliminar estudiante")
                    .setPositiveButton("Sí"){
                        dialog, id ->
                        estudiantes!![p2].key?.let {
                            refEstudiante.child(it).removeValue()
                        }
                        Toast.makeText(this@MainActivity, "Registro borrado exitosamente!!", Toast.LENGTH_LONG).show()
                    }
                ad.setNegativeButton("No",
                    object : DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            Toast.makeText(this@MainActivity, "Borrado cancelado", Toast.LENGTH_LONG).show()
                        }
                    })
                ad.show()
                return true
            }
        }

        estudiantes = ArrayList()

        consulta.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                estudiantes!!.clear()
                for (dato in snapshot.children){
                    val estudiante : Estudiante? = dato.getValue(Estudiante::class.java)
                    estudiante?.key = dato.key
                    if(estudiante != null){
                        estudiantes!!.add(estudiante)
                    }

                    //Log.i("ESTUDIANTE", estudiante!!.nombre)
                }

                //Log.i("ESTUDIANTE", estudiantes.toString())

                val adapter = AdaptadorEstudiante(
                    this@MainActivity,
                    estudiantes as ArrayList<Estudiante>
                )

                lista_estudiantes!!.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

    private fun CerrarSesion(){
        FirebaseAuth.getInstance().signOut().also {
            ToastHelper.ToastSimple(this, "Sesion cerrado")

            val inten = Intent(this, LoginActivity::class.java)
            startActivity(inten)
            finish()
        }
    }

    companion object{
        var db : FirebaseDatabase = FirebaseDatabase.getInstance()
        var refEstudiante : DatabaseReference = db.getReference(Accion.estudiantes)
    }
}