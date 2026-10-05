package com.huamani.tarea_02

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.huamani.tarea_02.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding_main: ActivityMainBinding

    override fun onCreate(instancia_guardada: Bundle?) {
        super.onCreate(instancia_guardada)

        binding_main = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        configurar_navegacion()
    }

    private fun configurar_navegacion() {
        binding_main.btnIrCotizador.setOnClickListener {
            lanzar_actividad<CotizadorActivity>()
        }

        binding_main.btnIrGestorTareas.setOnClickListener {
            lanzar_actividad<TareasActivity>()
        }
    }

    private inline fun <reified T : AppCompatActivity> lanzar_actividad() {
        Intent(this, T::class.java).let {
            startActivity(it)
        }
    }
}
