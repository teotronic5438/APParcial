package ar.com.ifts18.apparcial

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    // TANTO PARA NO BORRAR INFOOOOOOOOOOOOOOOOOOOOOOOO
    private lateinit var tycLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonTC = findViewById<Button>(R.id.btnIniciar)
        val caja = findViewById<CheckBox>(R.id.caja)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)

        // SOLO PARA PRUEBAS HOME DESPUES BORRAR
        val solopruebas = findViewById<Button>(R.id.pruebasAHome)
        solopruebas.setOnClickListener{
            irAHome()
        }

        val solorendimiento = findViewById<Button>(R.id.pruebasARendimiento)
        solorendimiento.setOnClickListener{
            irARendimiento()
        }

        val solohistorial = findViewById<Button>(R.id.pruebasAHistorial)
        solohistorial.setOnClickListener{
            irAHistorial()
        }

        // SOLO PARA PRUEBAS HOME DESPUES BORRAR

        // Inicializa el launcher para manejar el resultado
        tycLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Los términos fueron aceptados, marcamos el CheckBox
                caja.isChecked = true
                val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                misPreferencias.edit().putBoolean("terminosAceptados", true).apply()
            } else if (result.resultCode == RESULT_CANCELED) {
                // Los términos fueron rechazados
                caja.isChecked = false
                val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                misPreferencias.edit().putBoolean("terminosAceptados", false).apply()
            }
        }


        // evento, al hacer click abre actvity TyC
        caja.setOnClickListener{
            caja.isChecked = false
            // mostrarToast("Ingresaste a terminos y condiciones")
            irATerminosYCondiciones()
        }

        // Llamo a SharedPreferences y extraigo "terminosAceptados"
        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val terminosAceptados = misPreferencias.getBoolean("terminosAceptados", false)
        // Dependiendo el boton presionado se cambia el estado del CheckBox
        caja.isChecked = terminosAceptados

        // en este archivo guaddate la key estaLogeado en false y ademas guardala en yaSeLogueo
        val testCompletado = misPreferencias.getBoolean("testCompletado", false)


        // logica de validacion
        botonTC.setOnClickListener {
            // casteo datos
            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val correo = etCorreo.text.toString()

            val usernameGuardado = misPreferencias.getString("username", null)
            val userlastnameGuardado = misPreferencias.getString("userlastname", null)
            val useremailGuardado = misPreferencias.getString("useremail", null)

            // Verifico que haya llenado todos los campos
            if (nombre.isEmpty()){
                mostrarToast("Complete el nombre")
            } else if(apellido.isEmpty()){
                mostrarToast("Complete su apellido")
            } else if(correo.isEmpty()) {
                mostrarToast("Complete su correo")
            } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                mostrarToast("El formato de correo es invalido!")
            } else if (!caja.isChecked) {
                // Verifico que el checkbox de términos y condiciones está marcado
                mostrarToast("Debes aceptar los términos y condiciones.")
            } else {
                // Verifico si no hay usuario guardado
                if (usernameGuardado != null && useremailGuardado != null) {
                    // Verifico si el usuario actual coincide con el guardado
                    if (usernameGuardado == nombre &&
                        userlastnameGuardado == apellido &&
                        useremailGuardado == correo && testCompletado) {

                        // Si coincide, ir a la actividad Home
                        mostrarToast("Ya estás logeado")
                        irAHome()
                        return@setOnClickListener
                    }
                }

                // Guardar datos de usuario en SharedPreferences
                misPreferencias.edit().apply {
                    putString("username", nombre)
                    putString("userlastname", apellido)
                    putString("useremail", correo)
                    apply()
                }

                // Ir a Test Inversor
                irATestInversor()
            }
        }


    }
    private fun irATerminosYCondiciones() {
        val intent = Intent(this, TyCActivity::class.java)
        tycLauncher.launch(intent)
    }

    private fun irATestInversor(){
        val intent = Intent(this, TestInversorActivity::class.java)
        startActivity(intent)
    }

    private fun irAHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


    // SOLO PRUEBA DESPUES BORRAR
    private fun irAHistorial(){
        val intent = Intent(this, HistorialActivity::class.java)
        startActivity(intent)
        finish()
    }    // SOLO PRUEBA DESPUES BORRAR

    // SOLO PRUEBA DESPUES BORRAR
    private fun irARendimiento(){
        val intent = Intent(this, RendimientoActivity::class.java)
        startActivity(intent)
        finish()
    }    // SOLO PRUEBA DESPUES BORRAR
    
    private fun mostrarToast(mensajeToast: String){
        Toast.makeText(this, mensajeToast, Toast.LENGTH_LONG).show()
    }
}