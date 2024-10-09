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

    /**
     * Called when the activity is first created.
     *
     * This method initializes the activity layout and sets up the user interface elements,
     * retrieves user preferences, receives intent extras for the investment calculation,
     * and displays the results based on user input. It also includes event listeners for buttons
     * that allow users to navigate back to the simulator.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in
     *                           onSaveInstanceState(Bundle). Otherwise, it is null.
     */


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
        val tvRecomendacion = findViewById<TextView>(R.id.tvRecomendacion)
        val btnVolverSimulador = findViewById<Button>(R.id.btnVolverSimulador)

        // Traigo Shared Preferences para info usuario
        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val correo_global = misPreferencias.getString("correo_global", null)

        val usuariosShared = getSharedPreferences("user_$correo_global", Context.MODE_PRIVATE)

        val usuarioPreference = usuariosShared.getString("nombre", "Admin")
        if (usuarioPreference != null) {
            tvTitulo.text = "¡Felicidades ${usuarioPreference.replaceFirstChar { it.uppercase() }}!"
        }

        // Recibo los parametros de Home
        val montoDouble = intent.getDoubleExtra("MONTO_DOBLE", 0.0)
        val plazoInt = intent.getIntExtra("PLAZO_INT", 0)
        val bancoString = intent.getStringExtra("BANCO_STRING") ?: "Banco Amanecer"
        val inversionString = intent.getStringExtra("INVERSION_STRING") ?: "Plazo Fijo Futurama"


        // Crear un listado con todos los bancos


        val bancos: MutableMap<String, Double> = mutableMapOf()

        // Agregar registros al listado
        bancos["Banco Nacion"] = 39.0
        bancos["Banco Santander"] = 33.0
        bancos["Banco Galicia"] = 37.5 // prueba 43.0
        bancos["Banco BBVA"] = 35.5
        bancos["Banco HSBC"] = 37.0 // 37.0


        val tnaBanco: Double? = bancos[bancoString]


        // Verifica si tnaBanco no es nulo antes de continuar
        if (tnaBanco != null) {

            // Imprimo resultados en Activity
            val interesGanado = calcularGanancia(montoDouble, plazoInt, tnaBanco)
            val valorFinal = montoDouble + interesGanado

            tvMontoRetirar.text = "$ ${String.format("%.2f", valorFinal)}"
            tvMontoInvertir.text = "$ ${montoDouble.toString()}"
            tvBanco.text = bancoString
            tvTiempoPlazoFijo.text = "${plazoInt.toString()} días"
            tvInteresGanado.text = "$ ${String.format("%.2f", interesGanado)}"

            val ROIcalculado = calcularROI(valorFinal, montoDouble)
            tvROI.text = "${String.format("%.2f", ROIcalculado)} %"
            tvTNA.text = tnaBanco.toString() // Mostrar TNA

            // Guardar el historial en SharedPreferences (si lo necesitas)
            guardarEnHistorialPreferences(montoDouble, plazoInt, bancoString, valorFinal, ROIcalculado, inversionString)



        } else {
            // Manejo de error si no se encuentra el banco
            mostrarToast("Banco no encontrado: $bancoString")
        }

        // Cálculo para el banco con la TNA más alta
        val bancoConTnaMasAlta = obtenerBancoConTnaMasAlta(bancos)
        // Destrucutring por separado
        val (bancoTNAAlta, tasaTNAAlta) = bancoConTnaMasAlta
        val gananciaMasAlta = calcularGanancia(montoDouble, plazoInt, tasaTNAAlta)
        val valorFinalMasAlta = montoDouble + gananciaMasAlta
        val roiMasAlto = calcularROI(montoDouble + gananciaMasAlta, montoDouble)
        tvRecomendacion.text = "Para mayor rendimiendo puedes usar el $bancoTNAAlta que tiene una taza de ${String.format("%.2f", tasaTNAAlta)} %.\nTe daria una ganancia de $ ${String.format("%.2f", valorFinalMasAlta)}.\nCon un ROI de: ${String.format("%.2f", roiMasAlto)} %."

        // Luego de mostrar datos Vuelvo a simulador
        btnVolverSimulador.setOnClickListener {
            irASimulador()
        }
    }

    private fun calcularGanancia(montoDouble: Double, plazoInt: Int, tnaBanco: Double): Double {
        // POR AHORA HARDCODEADO DESPUES CREAR TNA
        // castear TNA A DOUBLE
        // val TNA = 43.0
        val ganancia = montoDouble * ((tnaBanco / 100) / 360) * plazoInt
        // mostrarToast("$ganancia")    // mostrando solo ganancia si llegue hasta aqui SOY FELIZ
        return ganancia
    }

    private fun calcularROI(valorFinal: Double, montoDouble: Double) : Double{
        val roiCalculado = ( (valorFinal - montoDouble) / montoDouble ) * 100
        return roiCalculado
    }

    private fun obtenerBancoConTnaMasAlta(bancos: Map<String, Double>): Pair<String, Double> {
        // Busca el banco con la mayor TNA
        val bancoConTnaMasAlta = bancos.maxByOrNull { it.value }
        return bancoConTnaMasAlta?.toPair() ?: Pair("No disponible", 0.0)
    }

    private fun irASimulador() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("DefaultLocale")
    private fun guardarEnHistorialPreferences(montoDouble: Double, plazoInt: Int, bancoString: String, valorFinal: Double, ROIcalculado: Double, inversionString: String) {
        // Inicializa el nuevo registro
        val nuevoRegistro = """
Monto invertido: $ ${String.format("%.2f", montoDouble)}
Plazo elegido: $plazoInt dias 
Banco: $bancoString 
Recibiras: $ ${String.format("%.2f", valorFinal)}
El roi calculado: ${String.format("%.2f", ROIcalculado)} %
Inversion elegida: ${inversionString.uppercase()}
         """
        // String.format("%.2f", interesGanado)

        // Traigo Shared Preferences para info usuario
        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val correo_global = misPreferencias.getString("correo_global", null)

        val historialGuardar = getSharedPreferences("user_$correo_global", Context.MODE_PRIVATE)


        // Obtén el número de registros actuales (limitar a 5)
        val contadorRegistro = historialGuardar.getInt("contador", 0)

        val editor = historialGuardar.edit()

        // Si el contador es menor a 5, incrementa y guarda normalmente
        if (contadorRegistro < 5) {
            // Guardar el nuevo registro en la posición actual
            editor.putString("historial${contadorRegistro + 1}", nuevoRegistro)
            // Incrementar el contador
            editor.putInt("contador", contadorRegistro + 1)
        } else {
            // Si ya hay 5 registros, desplaza todos los registros una posición hacia atrás
            for (i in 1 until 5) {
                val registroAnterior = historialGuardar.getString("historial${i + 1}", "")
                editor.putString("historial$i", registroAnterior) // Mover el registro a la posición anterior
            }
            // Sobreescribir el último registro con el nuevo
            editor.putString("historial5", nuevoRegistro)
        }

        // Aplicar los cambios
        editor.apply()

        // Opcional: Mostrar un toast con el registro nuevo
        // mostrarToast("Registro guardado: $nuevoRegistro")
    }


}