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

        val tvNombre = rowView.findViewById<TextView>(R.id.tvNombre)
        val tvApellido = rowView.findViewById<TextView>(R.id.tvApellido)
        val tvGrado = rowView.findViewById<TextView>(R.id.tvGrado)
        val tvMateria = rowView.findViewById<TextView>(R.id.tvMateria)
        val tvNotaFinal = rowView.findViewById<TextView>(R.id.tvNotaFinal)

        val estudiante = estudiantes[position]

        tvNombre.text = "Nombre: ${estudiante.nombre}"
        tvApellido.text = "Apellido: ${estudiante.apellido}"
        tvGrado.text = "Grado: ${estudiante.grado}°"
        tvMateria.text = "Materia: ${estudiante.materia}"
        tvNotaFinal.text = "Nota final: ${estudiante.notaFinal.toString()}"

        return rowView
    }
}