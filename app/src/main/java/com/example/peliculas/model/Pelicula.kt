package com.example.peliculas.model

data class Pelicula(
    var id: Int,
    var titulo: String,
    var genero: Int,
    var anio: String,
    var duracion: Int,
    var calificacion: Int
) {
    companion object {
        const val ACCION = 0
        const val DOCUMENTAL = 1
        const val DRAMA = 2
        const val MUSICAL = 3
        const val ROMANCE = 4
        const val CIENCIA_FICCION = 5
        const val TERROR = 6
    }
}