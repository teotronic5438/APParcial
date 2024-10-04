package ar.com.ifts18.apparcial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TyCActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tyc)

        // aqui va el codigo
        val texto: String = getString(R.string.tyc)
        val textView: TextView = findViewById(R.id.miTextView)
        textView.text = texto



        val denegarTc = findViewById<Button>(R.id.denegar)
        val aceptarTc= findViewById<Button>(R.id.aceptar)

        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        aceptarTc.setOnClickListener{
            mostrarToast("tocaste el boton de ACEPTAR")

            misPreferencias.edit().putBoolean("terminosAceptados", true).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        denegarTc.setOnClickListener {
            mostrarToast("tocaste el boton de DENEGAR")

            misPreferencias.edit().putBoolean("terminosAceptados", false).apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun mostrarToast(mensaje: String){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
