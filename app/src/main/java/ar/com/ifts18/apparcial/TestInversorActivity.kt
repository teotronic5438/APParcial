package ar.com.ifts18.apparcial

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TestInversorActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testinversor)

        // aqui va el codigo

        val btEnviar = findViewById<Button>(R.id.elem2)
        val rgOpciones2 = findViewById<RadioGroup>(R.id.rg2pc)

        var sumTotal = 0;

        btEnviar.setOnClickListener {


            val rbSeleccionado2 = rgOpciones2.checkedRadioButtonId


            if (rbSeleccionado2 != -1) {

                //Encontrar el RB correspondiente
                val rbSeleccionado = findViewById<RadioButton>(rbSeleccionado2)

                val opcion = rbSeleccionado.text

                if (rbSeleccionado.text == "Menos de 180 días") {
                    sumTotal += 1
                } else if (rbSeleccionado.text == "Entre 180 días y 1 año") {
                    sumTotal += 2
                } else if (rbSeleccionado.text == "De 1 a 2 años") {
                    sumTotal += 3
                } else {
                    sumTotal += 4
                }
                Log.d("tag", "Se seleccionó $opcion")

            } else {

                //TBD Se debe seleccionar un RB
                Log.d("tag", "No se seleccionó nada")
            }
        }

        Toast.makeText(this, "Total: " + sumTotal.toString() , Toast.LENGTH_LONG).show()

    }
}