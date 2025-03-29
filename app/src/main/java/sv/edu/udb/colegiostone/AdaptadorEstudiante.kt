package sv.edu.udb.colegiostone

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import sv.edu.udb.colegiostone.modelos.Estudiante

class AdaptadorEstudiante (
    private val context : Activity,
    var estudiantes : ArrayList<Estudiante>
) : ArrayAdapter<Estudiante>(
    context,
    R.layout.estudiante_layout,
    estudiantes
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = context.layoutInflater

        var rowView : View = convertView ?: layoutInflater.inflate(
            R.layout.estudiante_layout,
            null
        )

        var tvNombre = rowView.findViewById<TextView>(R.id.tvNombre)
        var tvApellido = rowView.findViewById<TextView>(R.id.tvApellido)
        var tvGrado = rowView.findViewById<TextView>(R.id.tvGrado)

        val estudiante = estudiantes[position]
        //Log.i("ESTUDIANTE", "////////")
        //Log.i("ESTUDIANTE", estudiante.key.toString())

        tvNombre.text = "Nombre: ${estudiante.nombre}"
        tvApellido.text = "Apellido: ${estudiante.apellido}"
        tvGrado.text = "Grado: ${estudiante.grado}"

        return rowView
    }
}