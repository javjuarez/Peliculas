package com.example.peliculas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.peliculas.databinding.ActivityInsertarPeliculaBinding
import com.example.peliculas.db.DBPeliculas
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class InsertarPeliculaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertarPeliculaBinding
    private var generoSelected = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertarPeliculaBinding.inflate(layoutInflater)

        val items = resources.getStringArray(R.array.genres)
        val adapter = ArrayAdapter(this, R.layout.list_item_autocomplete, items)
        (binding.autoCompleteGenero as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.autoCompleteGenero.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ -> generoSelected = i }
        setContentView(binding.root)
    }

    fun click(view: View) {
        val dbPeliculas = DBPeliculas(this)

        with(binding) {

            if (isCamposVacios()) {
                Toast.makeText(
                    this@InsertarPeliculaActivity,
                    getString(R.string.ToastCamposRequeridos_msg),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            if (!restriccionesCampos()) return
            val titulo = textInputTitulo.text.toString()
            val anio = textInputAnio.text.toString()
            val duracion = textInputDuracion.text.toString().toInt()
            val calificacion = textInputCalificacion.text.toString().toInt()

            val id =
                dbPeliculas.insertPelicula(titulo, generoSelected, anio, duracion, calificacion)

            if (id > 0) {
                Toast.makeText(
                    this@InsertarPeliculaActivity,
                    getString(R.string.ToastInsertar_msg),
                    Toast.LENGTH_SHORT
                ).show()
                textInputTitulo.setText("")
                textInputAnio.setText("")
                textInputDuracion.setText("")
                textInputCalificacion.setText("")
                generoSelected = -1
                autoCompleteGenero.setText("")
                textInputTitulo.requestFocus()
            } else Toast.makeText(
                this@InsertarPeliculaActivity,
                getString(R.string.ToastErrorInsertar_msg),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    // Validaciones
    private fun isCamposVacios(): Boolean {
        with(binding) {
            return (campoVacio(textInputTitulo) || campoVacio(textInputAnio) ||
                    campoVacio(textInputDuracion) || campoVacio(textInputCalificacion) ||
                    autocompleteNoValido())
        }
    }

    // Auxiliares

    private fun campoVacio(editText: TextInputEditText): Boolean {
        var esVacio = false
        if (editText.text.toString() == "") {
            editText.error = getString(R.string.error_empty_field)
            esVacio = true
        }
        return esVacio
    }

    private fun autocompleteNoValido(): Boolean {
        return if (generoSelected == -1) {
            binding.layoutGenero.error = getString(R.string.error_empty_autocomplete)
            true
        } else false
    }

    private fun restriccionesCampos(): Boolean {
        var cumpleRestriccion = true
        with(binding) {
            val calif = textInputCalificacion.text.toString().toInt()
            val anio = textInputAnio.text.toString().toInt()
            if (anio > 2022) {
                textInputAnio.error = getString(R.string.anioRange)
                cumpleRestriccion = false
            }
            if (calif !in 1..5) {
                textInputCalificacion.error = getString(R.string.califRange)
                cumpleRestriccion = false
            }
        }
        return cumpleRestriccion
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, PantallaInicioActivity::class.java))
        finish()
    }
}