package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

class TodasComunidades : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todas_comunidades)

        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"

        /*val imagenComunidad = findViewById<ImageView>(R.id.imageView10)
        imagenComunidad.setOnClickListener{
            cambiarAEjComunidad()
        }*/

        //cargarImagen("MassEffect", "https://cdn2.steamgriddb.com/file/sgdb-cdn/icon_thumb/d2e9dd9dcd97fd12a2cb62e2bf7cbe35.png")

        val buttonHome = findViewById<ImageButton>(R.id.imageButtonHome)
        buttonHome.setOnClickListener {
            val prIntent : Intent = Intent(this,PantallaPrincipal::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }
        val btnBuscar = findViewById<ImageButton>(R.id.imageButtonBuscar)
        btnBuscar.setOnClickListener {
            val prIntent : Intent = Intent(this,Busqueda::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

    }

    /*fun cargarImagen(nombre: String, url: String){
        var aux:String = "R.id.imageView" + nombre
        Toast.makeText(this, "hola"+aux, Toast.LENGTH_SHORT).show()
        val id = resources.getIdentifier(aux, "id", "myPackage")
        val imagen = findViewById<ImageView>(id)
        Glide.with(applicationContext).load(url).into(imagen)
    }*/

    fun cambiarAEjComunidad(v: View?){
        val prIntent : Intent = Intent(this,EjemploComunidad::class.java)
        startActivity(prIntent)
    }
}