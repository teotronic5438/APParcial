package ar.com.ifts18.apparcial

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity: AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Para validar datos ingresados
        val etMonto = findViewById<EditText>(R.id.et1)
        val etPlazoEnDias = findViewById<EditText>(R.id.et2)
        val tvNumero2 = findViewById<TextView>(R.id.tvNumero2)

        // Botones para la accion
        val botonHistorial = findViewById<Button>(R.id.btVerHistorial)
        val botonSalir = findViewById<Button>(R.id.btSalir)
        val botonCalcularRendimiento = findViewById<Button>(R.id.btCalcularRendimiento)

        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val correo_global = misPreferencias.getString("correo_global", null)

        val usuariosShared = getSharedPreferences("user_$correo_global", Context.MODE_PRIVATE)

        val hNombre = usuariosShared.getString("nombre", "Admin")


        // Muestro saludo de Bienvenida
        if (hNombre != null) {
            tvNumero2.text = "¡Bienvenido ${hNombre.replaceFirstChar { it.titlecase() }}!"
        }


        // Cargo Spinner para Banco y tipo de Inversion
        val spinnerBanco = findViewById<Spinner>(R.id.banco_spinner)
        val spinnerInversion = findViewById<Spinner>(R.id.inversion_spinner)


        //CREAMOS EL ARRAY DEL ADAPTER del BANCO
        ArrayAdapter.createFromResource(
            this,
            R.array.entradaBanco,
            android.R.layout.simple_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinnerBanco.adapter = adapter
        }

        //CREAMOS EL ARRAY DEL ADAPTER de la INVERSION
        ArrayAdapter.createFromResource(
            this,
            R.array.entradaInversion,
            android.R.layout.simple_spinner_item
        ).also { adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinnerInversion.adapter = adapter
        }


        botonCalcularRendimiento.setOnClickListener{
            val monto: Double? = etMonto.text.toString().toDoubleOrNull()
            val PlazoEndias:Int? = etPlazoEnDias.text.toString().toIntOrNull()
            val entradaDeInversion = spinnerInversion.selectedItem.toString()
            val entradaDeBanco = spinnerBanco.selectedItem.toString()

            // Validaciones previas al envio del form
            if(etMonto.text.isEmpty()) {
                mostrarToast("El Monto no puede quedar vacio")
            }else if(monto?.toInt() ==0) {
                mostrarToast("El monto no puede ser cero")
            }else if(etPlazoEnDias.text.isEmpty()) {
                mostrarToast("El plazo de Diaz no puede estar vacio")
            } else if(PlazoEndias==0){
                mostrarToast("El plazo no puede ser cero")
            }else if(entradaDeBanco == getString(R.string.elija_banco)) {
                mostrarToast("Seleccione uno de los bancos")
            }else if(entradaDeInversion == getString(R.string.elija_inversion)) {
                mostrarToast("Seleccione un tipo de inversion")
            }else {
                // mostrarToast("$monto para $PlazoEndias de $entradaDeBanco para $entradaDeInversion")

                val intent = Intent(this, RendimientoActivity::class.java).apply {
                    //paso parametros a rendimiento
                    putExtra("MONTO_DOBLE", monto)
                    putExtra("PLAZO_INT", PlazoEndias)
                    putExtra("BANCO_STRING", entradaDeBanco)
                    putExtra("INVERSION_STRING", entradaDeInversion)
                }
                startActivity(intent)
            }
        }

        // Acciones para historial o salir
        botonHistorial.setOnClickListener{ irAHistorial() }

        botonSalir.setOnClickListener{ irAInicio() }


    }

    private fun irAHistorial(){
        val intent = Intent(this, HistorialActivity::class.java)
        startActivity(intent) }

    private fun irAInicio(){
        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        misPreferencias.edit().apply {
            putBoolean("estaLogeado", false)
            apply()
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}