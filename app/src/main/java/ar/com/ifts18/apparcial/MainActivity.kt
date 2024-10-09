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
    /**
     * Método que se llama al crear la actividad `MainActivity`.
     *
     * En este método se inicializan los elementos de la interfaz de usuario,
     * como botones y campos de texto. También se configura el lanzador de
     * actividades para los términos y condiciones, y se gestionan las
     * preferencias compartidas para recordar el estado de aceptación de
     * términos y condiciones.
     *
     * @param savedInstanceState Un `Bundle` que contiene el estado anterior de la actividad, si existe.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonTC = findViewById<Button>(R.id.btnIniciar)
        val caja = findViewById<CheckBox>(R.id.caja)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellido = findViewById<EditText>(R.id.etApellido)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)


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



        // logica de validacion
        botonTC.setOnClickListener {
            val nombre = etNombre.text.toString()
            val apellido = etApellido.text.toString()
            val correo = etCorreo.text.toString()

            // Verifico que haya llenado todos los campos
            if (nombre.isEmpty()) {

                mostrarToast("Complete el nombre")

            } else if (apellido.isEmpty()) {

                mostrarToast("Complete su apellido")

            } else if (correo.isEmpty()) {

                mostrarToast("Complete su correo")

            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {

                mostrarToast("El formato de correo es inválido")

            } else if (!caja.isChecked) {

                mostrarToast("Debes aceptar los términos y condiciones")

            } else {

                // usaremos la key para usar global durante el uso de la app
                misPreferencias.edit().apply{
                    putString("correo_global", correo)
                    apply()
                }

                // CAMINO FELIZ: LLENO CORRECTAMENTE LOS CAMPOS
                // Verifico si el usuario ya está registrado (CREO SHARED CON KEY POR USUARIO USO EL CORREO)
                val usuariosShared = getSharedPreferences("user_$correo", Context.MODE_PRIVATE)
                val test_completado = usuariosShared.getBoolean("test_completado", false)
                // Traifgo el correo usandolo como key para ver si ya tuvo ingresos o no
                val correo_shared = usuariosShared.getString("correo", null)

                if (correo_shared == null) {
                    // SI NO LO ENCUENTRA ES PORQUE ES LA PRIMERA VEZ QUE INGRESA
                    val editorUsuariosShared = usuariosShared.edit()
                    editorUsuariosShared.putString("nombre", nombre)
                    editorUsuariosShared.putString("apellido", apellido)
                    editorUsuariosShared.putString("correo", correo)
                    editorUsuariosShared.putString("perfil", "")
                    editorUsuariosShared.putBoolean("test_completado", false)
                    editorUsuariosShared.putBoolean("terminos_aceptados", true)
                    editorUsuariosShared.apply()

                    // Redirigimos a la pantalla del Test de Inversor
                    mostrarToast("Redirigiendo a Test Inversor")
                    irATestInversor()
                } else {
                    // YA ESTÁ REGISTRADO - Verificamos si completó el test o no
                    if (test_completado) {
                        // El test ya fue completado, lo redirigimos a HomeActivity
                        mostrarToast("Ya completaste el test, redirigiendo a Home")
                        irAHome()
                    } else {
                        // No ha completado el test, lo redirigimos a TestInversorActivity
                        mostrarToast("Debes completar el Test Inversor")
                        irATestInversor()
                    }
                }
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
        finish()
    }

    private fun irAHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    
    private fun mostrarToast(mensajeToast: String){
        Toast.makeText(this, mensajeToast, Toast.LENGTH_LONG).show()
    }
}