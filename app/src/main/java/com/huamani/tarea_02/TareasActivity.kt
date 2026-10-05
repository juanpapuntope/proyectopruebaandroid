package com.huamani.tarea_02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huamani.tarea_02.databinding.ActivityTareasBinding

class TareasActivity : AppCompatActivity() {

    private lateinit var binding_tareas: ActivityTareasBinding
    private val registro_pendientes = mutableListOf<String>()
    private lateinit var adaptador_lista: TareasAdapter

    override fun onCreate(instancia_guardada: Bundle?) {
        super.onCreate(instancia_guardada)

        binding_tareas = ActivityTareasBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        preparar_adaptador()
        asociar_eventos()
        actualizar_marcador()
    }

    private fun preparar_adaptador() {
        adaptador_lista = TareasAdapter(this, registro_pendientes) { indice ->
            eliminar_tarea_en(indice)
        }
        binding_tareas.lvTareas.adapter = adaptador_lista
    }

    private fun asociar_eventos() {
        binding_tareas.btnInsertarTarea.setOnClickListener {
            insertar_tarea()
        }

        binding_tareas.lvTareas.setOnItemClickListener { _, _, posicion, _ ->
            eliminar_tarea_en(posicion)
        }

        binding_tareas.btnRegresarInicio.setOnClickListener {
            finish()
        }
    }

    private fun insertar_tarea() {
        val descripcion = binding_tareas.etTarea.text.toString().trim()

        when {
            descripcion.isEmpty() -> {
                Toast.makeText(
                    this,
                    "Ingrese la descripción de la tarea",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        registro_pendientes.add(descripcion)
        adaptador_lista.notifyDataSetChanged()
        actualizar_marcador()

        binding_tareas.etTarea.text?.clear()
    }

    private fun eliminar_tarea_en(indice: Int) {
        if (indice in registro_pendientes.indices) {
            registro_pendientes.removeAt(indice)
            adaptador_lista.notifyDataSetChanged()
            actualizar_marcador()

            Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actualizar_marcador() {
        val cantidad = registro_pendientes.size
        binding_tareas.tvMarcadorPendientes.text = "Tareas pendientes: $cantidad"
    }

    private class TareasAdapter(
        contexto: AppCompatActivity,
        lista: List<String>,
        private val alEliminar: (Int) -> Unit
    ) : ArrayAdapter<String>(contexto, R.layout.item_tarea, lista) {

        override fun getView(posicion: Int, convertView: View?, parent: ViewGroup): View {
            val vista = convertView ?: LayoutInflater.from(context).inflate(
                R.layout.item_tarea, parent, false
            )

            val tvNombre = vista.findViewById<TextView>(R.id.tvNombreTarea)
            val btnEliminar = vista.findViewById<ImageButton>(R.id.btnEliminarTarea)

            tvNombre.text = getItem(posicion)

            vista.setOnClickListener {
                alEliminar(posicion)
            }

            btnEliminar?.setOnClickListener {
                alEliminar(posicion)
            }

            return vista
        }
    }
}
