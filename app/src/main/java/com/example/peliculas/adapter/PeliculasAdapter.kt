package com.example.peliculas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.peliculas.R
import com.example.peliculas.databinding.ElementListviewMovieBinding
import com.example.peliculas.model.Pelicula

class PeliculasAdapter(private val context: Context, private var datos: ArrayList<Pelicula>): BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return datos.size
    }

    override fun getItem(index: Int): Any {
        return datos[index]
    }

    override fun getItemId(index: Int): Long {
        return datos[index].id.toLong()
    }

    override fun getView(index: Int, p1: View?, p2: ViewGroup?): View {
        val binding = ElementListviewMovieBinding.inflate(layoutInflater)
        val element = datos[index]
        with(binding) {
            textViewTitulo.text = element.titulo
            textViewAnio.text = element.anio
            textViewDuracion.text = element.duracion.toString()
            textViewCalificacion.text = "${element.calificacion} / 5"
        }
        return binding.root
    }

    private fun setGeneroAdapter(genero: Int, binding: ElementListviewMovieBinding) {
        with(binding)
        {
            when (genero) {
                Pelicula.ACCION -> {
                    textViewGenero.text = "Acción"
                    imgViewGenreIcon.setBackgroundResource(R.drawable.ic_action)
                }
                Pelicula.DOCUMENTAL -> {
                    textViewGenero.text = "Documental"
                    imgViewGenreIcon.setBackgroundResource(R.drawable.ic_documentary)
                }
                Pelicula.DRAMA -> {
                    textViewGenero.text = "Drama"
                    imgViewGenreIcon.setBackgroundResource(R.drawable.ic_drama)
                }
                Pelicula.MUSICAL -> {
                    textViewGenero.text = "Musical"
                    imgViewGenreIcon.setBackgroundResource(R.drawable.ic_musical)
                }
                Pelicula.ROMANCE -> {
                    textViewGenero.text = "Romance"
                    imgViewGenreIcon.setBackgroundResource(R.drawable.ic_romance)
                }
                Pelicula.CIENCIA_FICCION -> {
                    textViewGenero.text = "Ciencia Ficción"
                    imgViewGenreIcon.setBackgroundResource(R.drawable.ic_sci_fi)
                }
                Pelicula.TERROR -> {
                    textViewGenero.text = "Terror"
                    imgViewGenreIcon.setBackgroundResource(R.drawable.ic_terror)
                }
            }
        }
    }
}