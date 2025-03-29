package sv.edu.udb.colegiostone.modelos

class Estudiante {
    var nombre : String = ""
    var apellido : String = ""
    var grado : String = ""

    var key:String? = null
    var per : MutableMap<String, Boolean> = mutableMapOf()

    constructor() { }

    constructor(_nombre : String, _apellido : String, _grado : String){
        nombre = _nombre
        apellido = _apellido
        grado = _grado
    }

    fun toMap() : Map<String, Any?>{
        return  mapOf(
            "nombre" to nombre,
            "apellido" to apellido,
            "grado" to grado,
            "key" to key,
            "per" to per
        )
    }
}