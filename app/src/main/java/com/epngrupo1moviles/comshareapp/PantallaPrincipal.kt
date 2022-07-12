package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class PantallaPrincipal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)

        val buttonTodasComunidades = findViewById<ImageButton>(R.id.imageButtonTodasComunidades)
        buttonTodasComunidades.setOnClickListener {
            val prIntent : Intent = Intent(this,TodasComunidades::class.java)
            startActivity(prIntent)
        }

        val buttonBuscar = findViewById<ImageButton>(R.id.imageButtonBuscar)
        buttonBuscar.setOnClickListener {
            val prIntent : Intent = Intent(this,Busqueda::class.java)
            startActivity(prIntent)
        }


    }
}