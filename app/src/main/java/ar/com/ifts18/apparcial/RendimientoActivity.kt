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

        /*
        val montoString = montoDouble.toString()
        // Pruebo si llegan los parametros BORRAR DESPUES
        misPreferencias.edit().apply{
            putString("monto", "0")
            apply()
        }
        */




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

        // Luego de mostrar los datos, guardo el historial en SharedPreferences
        guardarEnHistorialPreferences(montoDouble, plazoInt, bancoString, interesGanado, ROIcalculado)

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
}