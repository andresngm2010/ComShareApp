package com.epngrupo1moviles.comshareapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cerrar_sesion,menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cerrar_Sesion->{
                FirebaseAuth.getInstance().signOut()
                onBackPressed()
            }


        }
        return super.onOptionsItemSelected(item)
    }
}