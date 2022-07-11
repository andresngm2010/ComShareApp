package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class TodasComunidades : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todas_comunidades)

        /*val imagenComunidad = findViewById<ImageView>(R.id.imageView10)
        imagenComunidad.setOnClickListener{
            cambiarAEjComunidad()
        }*/
    }

    fun cambiarAEjComunidad(v: View?){
        val prIntent : Intent = Intent(this,EjemploComunidad::class.java)
        startActivity(prIntent)
    }
}