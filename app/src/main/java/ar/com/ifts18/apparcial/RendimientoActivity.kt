package ar.com.ifts18.apparcial

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RendimientoActivity: AppCompatActivity() {
    private lateinit var montoRetirarEditText: EditText
    private lateinit var montoInvertirEditText: EditText
    private lateinit var montoBancoEditText: EditText
    private lateinit var tiempoPlazoFijoEditText: EditText
    private lateinit var resultadosTextView: TextView
    private lateinit var calcularButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rendimiento)

        montoRetirarEditText = findViewById(R.id.montoRetirarEditText)
        montoInvertirEditText = findViewById(R.id.montoInvertirEditText)
        montoBancoEditText = findViewById(R.id.montoBancoEditText)
        tiempoPlazoFijoEditText = findViewById(R.id.tiempoPlazoFijoEditText)
        resultadosTextView = findViewById(R.id.resultadosTextView)
        calcularButton = findViewById(R.id.calcularButton)

        calcularButton.setOnClickListener {
            calcularResultados()

            }
        }
    //val mostrarResultado =

    private fun calcularResultados() {
        // Función para validar que un valor sea un número positivo
        fun validarMonto(monto: String?): Double {
            if (monto.isNullOrEmpty()) {
                throw IllegalArgumentException("El campo está vacío")
            }
            val montoDouble = monto.toDoubleOrNull()
            if (montoDouble == null || montoDouble <= 0) {
                throw IllegalArgumentException("Ingrese un valor numérico positivo")
            }
            return montoDouble
        }

        try {
            Toast.makeText(this,"llegue al try" , Toast.LENGTH_SHORT).show()
            val montoInvertir = validarMonto(montoInvertirEditText.text.toString())
            val montoBanco = validarMonto(montoBancoEditText.text.toString())
            val tiempoPlazoFijo = validarMonto(tiempoPlazoFijoEditText.text.toString()).toInt() // Convertimos a Int
            Toast.makeText(this,"$tiempoPlazoFijo" , Toast.LENGTH_SHORT).show()

            // ... Resto de los cálculos

            var roi = null
            var tna = 43
            val interesesGanados = null
            val montoTotal = null

           /* val resultadosFormateados = """
            Monto a Invertir: $${"%.2f".format(montoInvertir)}
            Monto en el Banco: $${"%.2f".format(montoBanco)}
            Tiempo Plazo Fijo: $tiempoPlazoFijo meses
            Intereses Ganados: $${"%.2f".format(interesesGanados)}
            Monto Total en el Banco: $${"%.2f".format(montoTotal)}
            ROI: $${"%.2f".format(roi)}%
            TNA: $${"%.2f".format(tna)}%
        """.trimIndent() */
            Toast.makeText(this,"$resultadosTextView" , Toast.LENGTH_SHORT).show()

            // Mostrar los resultados en un TextView o cualquier otro elemento de la UI
            resultadosTextView.text = "holamundo"

        } catch (e: IllegalArgumentException) {
            // Mostrar un mensaje de error al usuario
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

    }
    }
