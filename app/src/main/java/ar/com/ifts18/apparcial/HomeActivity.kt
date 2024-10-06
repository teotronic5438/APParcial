package ar.com.ifts18.apparcial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val tvNumero1 = findViewById<TextView>(R.id.tvNumero1)

        // Mostrar los datos
       // val displayText = "Nombre: $nombre\nApellido: $apellido\nCorreo: $correo"


/*PRUEBAAAA
        val botonHistorial = findViewById<Button>(R.id.btVerHistorial)
        val botonSalir = findViewById<Button>(R.id.btSalir)
*/
       // botonHistorial.setOnClickListener{ irAHistorial() }
      //  botonSalir.setOnClickListener{ irAInicio() }

        // aqui va el codigo
    }
    fun irAHistorial(){
        val intent = Intent(this, HistorialActivity::class.java)
        startActivity(intent) }

    fun irAInicio(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent) }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}