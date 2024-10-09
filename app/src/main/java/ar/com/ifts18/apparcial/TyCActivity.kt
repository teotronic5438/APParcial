package ar.com.ifts18.apparcial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TyCActivity: AppCompatActivity() {

    /**
     * Método llamado al crear la actividad de Términos y Condiciones (TyC).
     * Este método establece la vista de la actividad, carga el texto de
     * los términos y condiciones, y configura los botones para aceptar
     * o denegar los términos. Al aceptar o denegar, se actualizan las
     * preferencias y se envía un resultado a `MainActivity`.
     *
     * @param savedInstanceState Bundle que contiene el estado guardado de la actividad,
     *                           o null si no hay estado guardado.
     * @see MainActivity
     */


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
            // mostrarToast("tocaste el boton de ACEPTAR")

            misPreferencias.edit().putBoolean("terminosAceptados", true).apply()
            val intent = Intent(this, MainActivity::class.java)
            // **Enviamos resultado a MainActivity**
            setResult(RESULT_OK) // Informamos que fue exitoso
            finish()
        }

        denegarTc.setOnClickListener {
            // mostrarToast("tocaste el boton de DENEGAR")

            misPreferencias.edit().putBoolean("terminosAceptados", false).apply()
            val intent = Intent(this, MainActivity::class.java)
            // **Enviamos resultado a MainActivity**
            setResult(RESULT_CANCELED) // Informamos que no se aceptaron los términos
            finish()
        }
    }

    private fun mostrarToast(mensaje: String){
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
    }
}
