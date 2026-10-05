package com.huamani.tarea_02

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huamani.tarea_02.databinding.ActivityCotizadorBinding
import java.util.Locale

class CotizadorActivity : AppCompatActivity() {

    private lateinit var binding_cotizador: ActivityCotizadorBinding

    override fun onCreate(instancia_guardada: Bundle?) {
        super.onCreate(instancia_guardada)

        binding_cotizador = ActivityCotizadorBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        inicializar_spinner_envio()
        configurar_eventos()
    }

    private fun inicializar_spinner_envio() {
        val alternativas_envio = arrayOf(
            "Envío Estándar ($0.00)",
            "Envío Exprés ($15.00)",
            "Envío Internacional ($30.00)"
        )

        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            alternativas_envio
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding_cotizador.spOpcionesEnvio.adapter = this
        }
    }

    private fun configurar_eventos() {
        binding_cotizador.btnProcesarCotizacion.setOnClickListener {
            procesar_cotizacion()
        }

        binding_cotizador.btnRetornarMenu.setOnClickListener {
            finish()
        }
    }

    private fun procesar_cotizacion() {
        val texto_ingresado = binding_cotizador.etPrecioBase.text.toString().trim()

        when {
            texto_ingresado.isEmpty() -> {
                mostrar_aviso("Ingrese el precio base del equipo")
                return
            }
        }

        val valor_base = texto_ingresado.toDoubleOrNull()

        when {
            valor_base == null || valor_base < 0.0 -> {
                mostrar_aviso("Ingrese un precio válido")
                return
            }
        }

        valor_base.let { precio_inicial ->
            val incremento_marca = calcular_incremento_marca(precio_inicial)
            val monto_garantia = if (binding_cotizador.cbGarantiaExtendida.isChecked) 50.0 else 0.0
            val costo_despacho = obtener_costo_envio()

            val total_pagar = precio_inicial + incremento_marca + monto_garantia + costo_despacho

            val resumen = construir_resumen(
                precio_inicial,
                incremento_marca,
                monto_garantia,
                costo_despacho,
                total_pagar
            )

            binding_cotizador.tvDesgloseFinal.text = resumen
        }
    }

    private fun calcular_incremento_marca(valor_base: Double): Double {
        return when (binding_cotizador.rgMarcas.checkedRadioButtonId) {
            R.id.rbMarcaPremium -> valor_base * 0.10
            R.id.rbMarcaEstandar -> 0.0
            else -> 0.0
        }
    }

    private fun obtener_costo_envio(): Double {
        return when (binding_cotizador.spOpcionesEnvio.selectedItemPosition) {
            1 -> 15.0
            2 -> 30.0
            else -> 0.0
        }
    }

    private fun construir_resumen(
        p_base: Double,
        r_marca: Double,
        c_garantia: Double,
        c_envio: Double,
        total: Double
    ): String {
        val moneda = { valor: Double -> String.format(Locale.US, "$%.2f", valor) }

        return buildString {
            appendLine("• Precio base:            ${moneda(p_base)}")
            appendLine("• Recargo de marca:       ${moneda(r_marca)}")
            appendLine("• Garantía extendida:     ${moneda(c_garantia)}")
            appendLine("• Servicio de envío:      ${moneda(c_envio)}")
            appendLine("────────────────────────────────────────")
            append("• MONTO TOTAL A PAGAR:    ${moneda(total)}")
        }
    }

    private fun mostrar_aviso(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).also {
            it.show()
        }
    }
}
