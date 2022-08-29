package com.epngrupo1moviles.comshareapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PantallaPublicar : AppCompatActivity() {

        lateinit var titulo: EditText
        lateinit var contenido: EditText
        lateinit var btnPublicar: Button
        lateinit var fechaPublicacion: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_publicar)
        titulo = findViewById(R.id.txtTituloPublicacion)
        contenido = findViewById(R.id.editTextContenido)
        btnPublicar = findViewById(R.id.btnPublicacion)
        fechaPublicacion = Calendar.getInstance()


       //val extras = intent.extras ?: return
      //val email = extras.getString("email") ?:"Unknown"
       //val provider = extras.getString("provider") ?:"Unknown"


        btnPublicar.setOnClickListener {

            val tituloPublicacion = titulo.text.toString()
            val contenidoPublicacion = contenido.text.toString()
            val fecha = fechaPublicacion.time.toString()
            val publicaciones = Publicacion(tituloPublicacion,contenidoPublicacion,"Carlos",fecha)
            ingresarPublicacion(publicaciones )
            finish()
        }
        val buttonClose = findViewById<ImageButton>(R.id.imageButtonCerrar)
        buttonClose.setOnClickListener {
            finish()
        }
    }



    fun ingresarPublicacion(publicacion:Publicacion){
        val db = Firebase.firestore
        Log.i("SI entra", "sivale")
        db.collection("publicaciones")
            .add(publicacion)

            .addOnSuccessListener { documentReference ->

        Toast.makeText(this,"publicacion registrada con exito", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Log.w(EXTRA_LOGIN, "Error adding document", exception)
                Toast.makeText(this,"Error al ingresar la publicacion", Toast.LENGTH_LONG).show() }
    }
    private fun cambioActividad() {

        val homeIntent = Intent(this, PantallaPrincipal::class.java).apply {

        }
        startActivity(homeIntent)
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