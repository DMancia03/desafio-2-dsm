package sv.edu.udb.colegiostone.utils

import android.widget.Toast

class ToastHelper {
    companion object{
        fun ToastSimple(context : android.content.Context, mensaje : String){
            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
        }
    }
}