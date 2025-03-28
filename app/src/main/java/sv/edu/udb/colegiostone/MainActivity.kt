package sv.edu.udb.colegiostone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sv.edu.udb.colegiostone.utils.Accion

class MainActivity : AppCompatActivity() {
    private lateinit var btnAgregar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnAgregar = findViewById(R.id.btnAgregarEstudiante)
        btnAgregar.setOnClickListener {
            val intent_add = Intent(this, AddEstudianteActivity::class.java)
            intent_add.putExtra("accion", Accion.agregar)
            startActivity(intent_add)
        }
    }
}