package com.example.peliculas

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.peliculas.databinding.ActivityDetallePeliculaBinding
import com.example.peliculas.db.DBPeliculas
import com.example.peliculas.model.Pelicula
import com.google.android.material.textfield.TextInputEditText

class DetallePeliculaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetallePeliculaBinding
    private lateinit var dbPeliculas: DBPeliculas

    private var pelicula: Pelicula? = null
    private var generoSelected = -1
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallePeliculaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        id = bundle?.getInt("ID", 0)!!

        fillAutoComplete()

        dbPeliculas = DBPeliculas(this)
        pelicula = dbPeliculas.getPelicula(id)

        binding.autoCompleteGenero.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ -> generoSelected = i }
        setContentView(binding.root)

        if (pelicula != null) {
            with(binding) {
                textInputTitulo.setText(pelicula?.titulo)
                textInputAnio.setText(pelicula?.anio)
                textInputDuracion.setText(pelicula?.duracion.toString())
                textInputCalificacion.setText(pelicula?.calificacion.toString())
                generoSelected = pelicula?.genero!!
                autoCompleteGenero.setText(autoCompleteGenero.adapter.getItem(generoSelected).toString())
            }
        }
    }

    fun click(view: View) {
        with(binding)
        {
            when (view.id) {
                R.id.btnEditar -> {
                    btnEditar.visibility = View.GONE
                    btnBorrar.visibility = View.GONE
                    btnActualizar.visibility = View.VISIBLE
                    layoutTitulo.isEnabled = true
                    layoutAnio.isEnabled = true
                    layoutCalificacion.isEnabled = true
                    layoutDuracion.isEnabled = true
                    layoutGenero.isEnabled = true
                    fillAutoComplete()
                }
                R.id.btnBorrar -> {
                    AlertDialog.Builder(this@DetallePeliculaActivity)
                        .setTitle(getString(R.string.AlertDialog_title))
                        .setMessage(getString(R.string.AlertDialog_message, pelicula?.titulo))
                        .setPositiveButton(getText(R.string.AlertDialog_yes), DialogInterface.OnClickListener { dialogInterface, i ->
                            if(dbPeliculas.deletePelicula(id)) {
                                Toast.makeText(
                                    this@DetallePeliculaActivity,
                                    getString(R.string.ToastEliminar_msg),
                                    Toast.LENGTH_LONG).show()
                                startActivity(Intent(this@DetallePeliculaActivity, PantallaInicioActivity::class.java))
                                finish()
                            }
                        })
                        .setNegativeButton(getString(R.string.AlertDialog_no), DialogInterface.OnClickListener { _ , _ -> return@OnClickListener })
                        .show()
                }
                R.id.btnActualizar -> {
                    if (isCamposVacios()) {
                        Toast.makeText(
                            this@DetallePeliculaActivity,
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
                    if (dbPeliculas.updatePelicula(id, titulo, generoSelected, anio, duracion,calificacion)) {
                        Toast.makeText(
                            this@DetallePeliculaActivity,
                            getString(R.string.ToastActualizar_msg),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@DetallePeliculaActivity,
                            PantallaInicioActivity::class.java))
                        finish()
                    } else Toast.makeText(
                        this@DetallePeliculaActivity,
                        getString(R.string.ToastErrorActualizar_msg),
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> return@with
            }
        }
    }

    private fun fillAutoComplete() {
        val items = resources.getStringArray(R.array.genres)
        val adapter = ArrayAdapter(this, R.layout.list_item_autocomplete, items)
        (binding.autoCompleteGenero as? AutoCompleteTextView)?.setAdapter(adapter)
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