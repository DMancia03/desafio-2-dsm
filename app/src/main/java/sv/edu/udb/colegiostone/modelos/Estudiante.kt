package sv.edu.udb.colegiostone.modelos

class Estudiante {
    var nombre : String = ""
    var apellido : String = ""
    var grado : Int = 0
    var materia : String = ""
    var notaFinal : Double = 0.0

    var key:String? = null
    var per : MutableMap<String, Boolean> = mutableMapOf()

    constructor() { }

    constructor(_nombre : String, _apellido : String, _grado : Int, _materia : String, _notaFinal : Double){
        nombre = _nombre
        apellido = _apellido
        grado = _grado
        materia = _materia
        notaFinal = _notaFinal
    }

    fun toMap() : Map<String, Any?>{
        return  mapOf(
            "nombre" to nombre,
            "apellido" to apellido,
            "grado" to grado,
            "materia" to materia,
            "notaFinal" to notaFinal,
            "key" to key,
            "per" to per
        )
    }
}