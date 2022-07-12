package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val butonIniciarSesionGoogle = findViewById<Button>(R.id.button2)
        butonIniciarSesionGoogle.setOnClickListener{
            val prIntent : Intent = Intent(this,TodasComunidades::class.java)
            startActivity(prIntent)
        }

        val butonIniciarSesion = findViewById<Button>(R.id.button3)
        butonIniciarSesion.setOnClickListener{
            val prIntent : Intent = Intent(this,TodasComunidades::class.java)
            startActivity(prIntent)
        }

        val textRegistrarse = findViewById<TextView>(R.id.txtViewRegistrar)
        textRegistrarse.setOnClickListener {
            val prIntent : Intent = Intent(this,Registrar::class.java)
            startActivity(prIntent)
        }
    }

}