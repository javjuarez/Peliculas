package com.example.peliculas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.peliculas.adapter.PeliculasAdapter
import com.example.peliculas.databinding.ActivityPantallaInicioBinding
import com.example.peliculas.db.DBPeliculas
import com.example.peliculas.model.Pelicula

class PantallaInicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPantallaInicioBinding
    private lateinit var listPeliculas: ArrayList<Pelicula>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaInicioBinding.inflate(layoutInflater)

        val dbPeliculas = DBPeliculas(this)
        listPeliculas = dbPeliculas.getPeliculas()

        val peliculasAdapter = PeliculasAdapter(this, listPeliculas)
        with(binding) {
            if (listPeliculas.size == 0) textViewSinRegistros.visibility = View.VISIBLE
            else textViewSinRegistros.visibility = View.INVISIBLE
            listViewPeliculas.adapter = peliculasAdapter
            listViewPeliculas.setOnItemClickListener { _, _, _, l ->
                startActivity(Intent(this@PantallaInicioActivity,
                    DetallePeliculaActivity::class.java).putExtra("ID", l.toInt()))
                finish()
            }
        }

        setContentView(binding.root)
    }

    fun click(view: View) {
        startActivity(Intent(this, InsertarPeliculaActivity::class.java))
        finish()
    }
}