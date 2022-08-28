package com.epngrupo1moviles.comshareapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TodasComunidades : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todas_comunidades)

        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"

        cargarComunidades()

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

    fun cargarComunidades(){
        val db = Firebase.firestore
        db.collection("Comunidades")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    val nombre = document.getString("nombre").toString()
                    val url = document.getString("URL").toString()
                    var aux:String = "imageView" + nombre
                    val id:Int = resources.getIdentifier(aux, "id", packageName)
                    val imagen = findViewById<ImageView>(id)
                    cargarImagen(imagen, url)
                    CambiarEJComunidad(imagen, url, nombre)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar documentos", Toast.LENGTH_LONG).show()
            }
    }

    fun cargarImagen(v: ImageView, url: String){
        Glide.with(applicationContext).load(url).into(v)
    }

    fun CambiarEJComunidad(v: ImageView, URL: String, nombreCom:String){
        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"
        v.setOnClickListener {
            val prIntent : Intent = Intent(this,EjemploComunidad::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
                putExtra("url", URL)
                putExtra("nombreCom", nombreCom)
            }
            startActivity(prIntent)
        }
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