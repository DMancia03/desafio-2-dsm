package sv.edu.udb.colegiostone.utils

class MateriaHelper {
    companion object{
        public fun ObtenerIndiceMateria(materia : String) : Int{
            when(materia){
                "Matemática" -> return 0
                "Ciencias naturales" -> return 1
                "Ciencias sociales" -> return 2
                "Lenguaje y literatura" -> return 3
                "Moral" -> return 4
                "Religión" -> return 5
            }

            return 0
        }
    }
}