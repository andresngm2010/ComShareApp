package com.epngrupo1moviles.comshareapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Busqueda :  AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)

        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"

        val buttonHome = findViewById<ImageButton>(R.id.imageButtonHome)
        buttonHome.setOnClickListener {
            val prIntent : Intent = Intent(this,PantallaPrincipal::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

        val buttonTodasComunidades = findViewById<ImageButton>(R.id.imageButtonTodasComunidades)
        buttonTodasComunidades.setOnClickListener {
            val prIntent : Intent = Intent(this,TodasComunidades::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

        val back = findViewById<ImageButton>(R.id.btnVolver)
        back.setOnClickListener {
            finish()
        }
    }

    fun cambiarAEjComunidad(v: View?){
        val prIntent : Intent = Intent(this,EjemploComunidad::class.java)
        startActivity(prIntent)
    }

}
