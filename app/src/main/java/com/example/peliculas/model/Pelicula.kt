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
        val ACCION = 0
        val DOCUMENTAL = 1
        val DRAMA = 2
        val MUSICAL = 3
        val ROMANCE = 4
        val CIENCIA_FICCION = 5
        val TERROR = 6
    }
}