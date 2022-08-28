package com.epngrupo1moviles.comshareapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cerrar_sesion,menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cerrar_Sesion->{
                FirebaseAuth.getInstance().signOut()
                //onBackPressed()
                val prIntent : Intent = Intent(this,MainActivity::class.java)
                startActivity(prIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
