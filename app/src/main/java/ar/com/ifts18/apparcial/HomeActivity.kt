package ar.com.ifts18.apparcial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvNumero2 = findViewById<TextView>(R.id.tvNumero2)

        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val hNombre = misPreferencias.getString("username", null)
        val hUsername = misPreferencias.getString("userlastname", null)

        tvNumero2.text = "¡bienvenido " + hNombre + "!"

        // Mostrar los datos
       // val displayText = "Nombre: $nombre\nApellido: $apellido\nCorreo: $correo"


        val botonHistorial = findViewById<Button>(R.id.btVerHistorial)
        val botonSalir = findViewById<Button>(R.id.btSalir)
        val botonCalcularRendimiento = findViewById<Button>(R.id.btCalcularRendimiento)

        botonCalcularRendimiento.setOnClickListener{
            mostrarToast("ir a rendimiento")


            irARendimiento() }
        botonHistorial.setOnClickListener{irAHistorial() }
        botonSalir.setOnClickListener{ irAInicio() }


        // aqui va el codigo
    }
    fun irAHistorial(){
        val intent = Intent(this, HistorialActivity::class.java)
        startActivity(intent) }

    fun irAInicio(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent) }

    fun irARendimiento(){
        val intent = Intent(this, RendimientoActivity::class.java)
        startActivity(intent)
    }
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}