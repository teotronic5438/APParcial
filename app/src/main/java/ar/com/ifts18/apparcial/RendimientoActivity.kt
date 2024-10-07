package ar.com.ifts18.apparcial

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RendimientoActivity: AppCompatActivity() {

    @SuppressLint("SetTextI18n", "DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rendimiento)

        // Declaro todas las variables de las VIEWS
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        val tvMontoRetirar = findViewById<TextView>(R.id.tvMontoRetirar)
        val tvMontoInvertir = findViewById<TextView>(R.id.tvMontoInvertir)
        val tvBanco = findViewById<TextView>(R.id.tvBanco)
        val tvTiempoPlazoFijo = findViewById<TextView>(R.id.tvTiempoPlazoFijo)
        val tvInteresGanado = findViewById<TextView>(R.id.tvInteresesGanados)
        val tvROI = findViewById<TextView>(R.id.tvROI)
        val tvTNA = findViewById<TextView>(R.id.tvTNA)
        val btnVolverSimulador = findViewById<Button>(R.id.btnVolverSimulador)

        // Traigo Shared Preferences para info usuario
        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val usuarioPreference = misPreferencias.getString("username", "Admin")
        tvTitulo.text = "¡Felicidades $usuarioPreference!"

        // Recibo los parametros de Home
        val montoDouble = intent.getDoubleExtra("MONTO_DOBLE", 0.0)
        val plazoInt = intent.getIntExtra("PLAZO_INT", 0)
        val bancoString = intent.getStringExtra("BANCO_STRING") ?: "Banco Amanecer"
        val inversionString = intent.getStringExtra("INVERSION_STRING") ?: "Plazo Fijo Futurama"

        // Pruebo si llegan los parametros BORRAR DESPUES




        // Imprimo resultados en Activity
        val interesGanado = calcularGanancia(montoDouble, plazoInt)
        val valorFinal = montoDouble + interesGanado
        tvMontoRetirar.text = "$ ${String.format("%.2f", valorFinal)}"
        tvMontoInvertir.text = "$ ${montoDouble.toString()}"
        tvBanco.text = bancoString
        tvTiempoPlazoFijo.text = "${plazoInt.toString()} dias"
        tvInteresGanado.text = "$ ${String.format("%.2f", interesGanado)}"
        val ROIcalculado = calcularROI(valorFinal, montoDouble)
        tvROI.text = "${String.format("%.2f", ROIcalculado)} %"
        tvTNA.text = "43 %" // POR AHORA HARDCODEADO

        // Luego de mostrar datos Vuelvo a simulador
        btnVolverSimulador.setOnClickListener {
            irASimulador()
        }
    }

    private fun calcularGanancia(montoDouble: Double, plazoInt: Int): Double {
        // POR AHORA HARDCODEADO DESPUES CREAR TNA
        // castear TNA A DOUBLE
        val TNA = 43.0
        val ganancia = montoDouble * ((TNA / 100) / 360) * plazoInt
        mostrarToast("$ganancia")
        return ganancia
    }

    private fun calcularROI(valorFinal: Double, montoDouble: Double) : Double{
        val roiCalculado = ( (valorFinal - montoDouble) / montoDouble ) * 100
        return roiCalculado
    }

    private fun irASimulador() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}