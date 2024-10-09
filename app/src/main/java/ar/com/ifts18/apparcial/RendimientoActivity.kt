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
        val tvRecomendacion = findViewById<TextView>(R.id.tvRecomendacion)
        val btnVolverSimulador = findViewById<Button>(R.id.btnVolverSimulador)

        // Traigo Shared Preferences para info usuario
        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val correo_global = misPreferencias.getString("correo_global", null)

        val usuariosShared = getSharedPreferences("user_$correo_global", Context.MODE_PRIVATE)

        val usuarioPreference = usuariosShared.getString("nombre", "Admin")
        tvTitulo.text = "¡Felicidades $usuarioPreference!"

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


        /*

        / Crear un listado con todos los bancos
        val bancos = mutableListOf<Map<String, Map<String, Double>>>()

        // Agregar registros al listado
        bancos.add(mapOf("Nacion" to mapOf("plazoFijo" to 39.0, "FCI" to 56.0)))
        bancos.add(mapOf("Santander" to mapOf("plazoFijo" to 33.0, "FCI" to 45.0)))
        bancos.add(mapOf("Galicia" to mapOf("plazoFijo" to 37.5, "FCI" to 60.0)))
        bancos.add(mapOf("BBVA" to mapOf("plazoFijo" to 35.5, "FCI" to 45.0)))
        bancos.add(mapOf("HSBC" to mapOf("plazoFijo" to 37.0, "FCI" to 60.0)))

        // Buscar el valor de tnaBanco
        val tnaBanco: Double? = bancos.find { it.containsKey(bancoString) }
            ?.get(bancoString)
            ?.get(inversionString)

        // Verificar el resultado y mostrar el valor
        if (tnaBanco != null) {
            println("La TNA de $bancoString para $inversionString es: $tnaBanco")
        } else {
            println("No se encontró información para el banco $bancoString y la inversión $inversionString.")
        }

        */

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

    /*
    private fun guardarEnHistorialPreferences(montoDouble: Double, plazoInt: Int, bancoString: String, interesGanado: Double, ROIcalculado: Double) {
        // Recupero los datos de usuario de Shared "MyPreferences"
        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val nombre = misPreferencias.getString("username", "N/A")
        val apellido = misPreferencias.getString("userlastname", "N/A")
        val tipoInversor = misPreferencias.getString("perfil", "N/A")
        // val montoUser = misPreferencias.getString("monto", "0")

        // Defino un nuevo Shared Preference para guardar los datos en historial
        val historialPreferences = getSharedPreferences("HistorialPreferences", Context.MODE_PRIVATE)
        val editor = historialPreferences.edit()

        // Recuperamos las listas existentes (si no existen, se inicializan como vacías)
        val listaMontos = historialPreferences.getStringSet("montos", mutableSetOf())?.toMutableSet()
        val listaPlazos = historialPreferences.getStringSet("plazos", mutableSetOf())?.toMutableSet()
        val listaBancos = historialPreferences.getStringSet("bancos", mutableSetOf())?.toMutableSet()
        val listaIntereses = historialPreferences.getStringSet("intereses", mutableSetOf())?.toMutableSet()
        val listaROIs = historialPreferences.getStringSet("rois", mutableSetOf())?.toMutableSet()

        // Recuperamos las listas existentes para estos datos adicionales
        val listaNombres = historialPreferences.getStringSet("nombres", mutableSetOf())?.toMutableSet()
        val listaApellidos = historialPreferences.getStringSet("apellidos", mutableSetOf())?.toMutableSet()
        val listaTipoInversor = historialPreferences.getStringSet("tiposInversor", mutableSetOf())?.toMutableSet()
        // val listaMontosUser = historialPreferences.getStringSet("montosUser", mutableSetOf())?.toMutableSet()

        // Convertir los valores actuales en strings para almacenarlos en los sets
        val montoString = montoDouble.toString()
        val plazoString = plazoInt.toString()
        val interesString = String.format("%.2f", interesGanado)
        val roiString = String.format("%.2f", ROIcalculado)

        // Mantenemos un límite de 5 registros
        if (listaMontos != null) {
            if (listaMontos.size >= 5) {
                listaMontos.remove(listaMontos.first())
                if (listaPlazos != null) {
                    listaPlazos.remove(listaPlazos.first())
                }
                if (listaBancos != null) {
                    listaBancos.remove(listaBancos.first())
                }
                if (listaIntereses != null) {
                    listaIntereses.remove(listaIntereses.first())
                }
                if (listaROIs != null) {
                    listaROIs.remove(listaROIs.first())
                }
                if (listaNombres != null) {
                    listaNombres.remove(listaNombres.first())
                }
                if (listaApellidos != null) {
                    listaApellidos.remove(listaApellidos.first())
                }
                if (listaTipoInversor != null) {
                    listaTipoInversor.remove(listaTipoInversor.first())
                }
            }
        }


        // Agregamos los nuevos datos a las listas
        if (listaMontos != null) {
            listaMontos.add(montoString)
        }
        if (listaPlazos != null) {
            listaPlazos.add(plazoString)
        }
        if (listaBancos != null) {
            listaBancos.add(bancoString)
        }
        if (listaIntereses != null) {
            listaIntereses.add(interesString)
        }
        if (listaROIs != null) {
            listaROIs.add(roiString)
        }
        if (listaNombres != null) {
            listaNombres.add(nombre)
        }
        if (listaApellidos != null) {
            listaApellidos.add(apellido)
        }
        if(listaTipoInversor != null) {
            listaTipoInversor.add(tipoInversor)
        }

        // Guardamos las listas actualizadas en SharedPreferences
        editor.putStringSet("montos", listaMontos)
        editor.putStringSet("plazos", listaPlazos)
        editor.putStringSet("bancos", listaBancos)
        editor.putStringSet("intereses", listaIntereses)
        editor.putStringSet("rois", listaROIs)
        editor.putStringSet("nombres", listaNombres)
        editor.putStringSet("apellidos", listaApellidos)
        editor.putStringSet("tiposInversor", listaTipoInversor)
        // Aplicar cambios
        editor.apply()
    }
    */

}