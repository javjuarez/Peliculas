package com.example.peliculas.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.peliculas.model.Pelicula
import java.lang.Exception

class DBPeliculas(private val context: Context?) : DBHelper(context) {

    fun insertPelicula(
        titulo: String,
        genero: Int,
        anio: String,
        duracion: Int,
        calificacion: Int
    ): Long {
        val dbHelper = DBHelper(context)
        val db = dbHelper.writableDatabase
        var id: Long = 0

        try {
            val values = ContentValues()
            values.put("titulo", titulo)
            values.put("genero", genero)
            values.put("anio", anio)
            values.put("duracion", duracion)
            values.put("calificacion", calificacion)
            id = db.insert(TABLE_MOVIES, null, values)
        } catch (e: Exception) {

        } finally {
            db.close()
        }

        return id
    }

    fun getPeliculas(): ArrayList<Pelicula> {
        val dbHelper = DBHelper(context)
        val db = dbHelper.writableDatabase

        var listPeliculas = ArrayList<Pelicula>()
        var peliculaTmp: Pelicula? = null
        var cursorPeliculas: Cursor? = null

        cursorPeliculas = db.rawQuery("SELECT * FROM $TABLE_MOVIES", null)
        if (cursorPeliculas.moveToFirst()) {
            do {
                peliculaTmp = Pelicula(
                    cursorPeliculas.getInt(0), cursorPeliculas.getString(1),
                    cursorPeliculas.getInt(2), cursorPeliculas.getString(3),
                    cursorPeliculas.getInt(4), cursorPeliculas.getInt(5)
                )
                listPeliculas.add(peliculaTmp)
            } while (cursorPeliculas.moveToNext())
        }
        cursorPeliculas.close()

        return listPeliculas
    }

    fun getPelicula(id: Int): Pelicula? {
        val dbHelper = DBHelper(context)
        val db = dbHelper.writableDatabase

        var pelicula: Pelicula? = null
        var cursorPeliculas: Cursor? = null

        cursorPeliculas = db.rawQuery("SELECT * FROM $TABLE_MOVIES WHERE id = $id LIMIT 1", null)
        if (cursorPeliculas.moveToFirst())
            pelicula = Pelicula(
                cursorPeliculas.getInt(0), cursorPeliculas.getString(1),
                cursorPeliculas.getInt(2), cursorPeliculas.getString(3),
                cursorPeliculas.getInt(4), cursorPeliculas.getInt(5)
            )
        cursorPeliculas.close()
        return pelicula
    }

    fun updatePelicula(id: Int, titulo: String, genero: Int, anio: String, duracion: Int, calificacion: Int): Boolean {
        var flagCorrecto = false
        val dbHelper = DBHelper(context)
        val db = dbHelper.writableDatabase

        try {
            db.execSQL(
            "UPDATE $TABLE_MOVIES " +
                    "SET titulo = '$titulo', genero = '$genero', anio = '$anio', " +
                    "duracion = '$duracion', calificacion = '$calificacion' WHERE id = $id"
            )
            flagCorrecto = true
        } catch (e: Exception) {
        } finally {
            db.close()
        }

        return flagCorrecto
    }

    fun deletePelicula(id: Int): Boolean {
        var flagCorrecto = false
        val dbHelper = DBHelper(context)
        val db = dbHelper.writableDatabase

        try {
            db.execSQL("DELETE FROM $TABLE_MOVIES WHERE id = $id")
            flagCorrecto = true
        } catch (e: Exception) {
        } finally {
            db.close()
        }

        return flagCorrecto
    }

}