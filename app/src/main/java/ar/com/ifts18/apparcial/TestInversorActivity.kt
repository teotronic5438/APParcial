package ar.com.ifts18.apparcial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TestInversorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testinversor)

        // aqui va el codigo


        val btSave = findViewById<Button>(R.id.save)
        val btCancel = findViewById<Button>(R.id.cancel)
        val rgOpciones1 = findViewById<RadioGroup>(R.id.rg1pc)
        val rgOpciones2 = findViewById<RadioGroup>(R.id.rg2pc)
        val rgOpciones3 = findViewById<RadioGroup>(R.id.rg3pc)

        var sumTotal = 0;

        btSave.setOnClickListener {


            val rbSeleccionado1 = rgOpciones1.checkedRadioButtonId
            val rbSeleccionado2 = rgOpciones2.checkedRadioButtonId
            val rbSeleccionado3 = rgOpciones3.checkedRadioButtonId

            if (rbSeleccionado1 != -1) {

                //Encontrar el RB correspondiente
                val rbSeleccionado1 = findViewById<RadioButton>(rbSeleccionado1)

                val opcion1 = rbSeleccionado1.text

                if (rbSeleccionado1.text == "No poseo conocimiento") {
                    sumTotal += 1
                } else if (rbSeleccionado1.text == "Tengo conocimientos básicos acerca de las distintas alternativas de inversión") {
                    sumTotal += 2
                } else if (rbSeleccionado1.text == "Tengo conocimientos acerca del riesgo y rentabilidad potencial de los distintos instrumentos financieros") {
                    sumTotal += 3
                } else {
                    sumTotal += 4
                }
                Log.d("tag", "Se seleccionó $opcion1")

                if (rbSeleccionado2 != -1) {

                    val rbSeleccionado2 = findViewById<RadioButton>(rbSeleccionado2)

                    val opcion2 = rbSeleccionado2.text

                    if (rbSeleccionado2.text == "Menos del 5%") {
                        sumTotal += 1
                    } else if (rbSeleccionado2.text == "Entre el 5% y el 20%") {
                        sumTotal += 2
                    } else if (rbSeleccionado2.text == "Entre el 21% y el 50%") {
                        sumTotal += 3
                    } else {
                        sumTotal += 4
                    }
                    Log.d("tag", "Se seleccionó $opcion2")

                    if (rbSeleccionado3 != -1) {


                        val rbSeleccionado3 = findViewById<RadioButton>(rbSeleccionado3)

                        val opcion3 = rbSeleccionado3.text

                        if (rbSeleccionado3.text == "Menos de 180 días") {
                            sumTotal += 1
                        } else if (rbSeleccionado3.text == "Entre 180 días y 1 año") {
                            sumTotal += 2
                        } else if (rbSeleccionado3.text == "De 1 a 2 años") {
                            sumTotal += 3
                        } else {
                            sumTotal += 4
                        }
                        Log.d("tag", "Se seleccionó $opcion3")


                        val percentage = ((sumTotal.toFloat() / 12) * 100)
                        var perfil: String = ""

                        if (0 < percentage && percentage < 33) {
                            perfil = "Conservador"
                            Toast.makeText(this, "El perfil es $perfil", Toast.LENGTH_LONG).show()

                        } else if (32 < percentage && percentage < 66) {
                            perfil = "Moderado"
                            Toast.makeText(this, "El perfil es $perfil", Toast.LENGTH_LONG).show()
                        } else {
                            perfil = "De Riesgo"
                            Toast.makeText(this, "El perfil es $perfil", Toast.LENGTH_LONG).show()

                        }


                        // guardo en my shared preferences el perfil
                        val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
                        misPreferencias.edit().apply {

                            putString("perfil", perfil)
                            putBoolean("testCompletado", true)
                            apply()
                        }
                        irAHomeActivity()

                    } else {

                        //TBD Se debe seleccionar un RB
                        Log.d("tag", "No answer for question 3")
                        Toast.makeText(this, "No se seleccionó respuesta de la pregunta 3", Toast.LENGTH_LONG).show()
                    }

                } else {

                    //TBD Se debe seleccionar un RB
                    Log.d("tag", "No answer for question 2")
                    Toast.makeText(this, "No se seleccionó respuesta de la pregunta 2", Toast.LENGTH_LONG).show()
                }

            } else {

                //TBD Se debe seleccionar un RB
                Log.d("tag", "No answer for question 1")
                Toast.makeText(this, "No se seleccionó respuesta de la pregunta 1", Toast.LENGTH_LONG).show()
            }

        }


        btCancel.setOnClickListener {

            val misPreferencias = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            misPreferencias.edit().apply {

                putBoolean("testCompletado", false)
                apply()
            }

            irAMainActivity()
        }
    }


        private fun irAHomeActivity(){
            val intent = Intent(this, HomeActivity::class.java).apply {  }
            startActivity(intent)
        }

        private fun irAMainActivity(){
            val intent = Intent(this, MainActivity::class.java).apply {  }
            startActivity(intent)
    }
}