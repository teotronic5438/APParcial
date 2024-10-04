package ar.com.ifts18.apparcial

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HistorialActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        // aqui va el codigo
        // Inicializa el botón para regresar
        val buttonRegresar = findViewById<Button>(R.id.buttonRegresar)
        buttonRegresar.setOnClickListener {
            // Regresa a MainActivity
            finish() // Termina ThirdActivity y regresa a la actividad anterior
        }
    }
}
