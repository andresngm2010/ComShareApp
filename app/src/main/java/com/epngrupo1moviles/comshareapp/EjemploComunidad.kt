package com.epngrupo1moviles.comshareapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EjemploComunidad : AppCompatActivity() {
    lateinit var buttonSeguir:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejemplo_comunidad)

        //obtenemos todos los extras
        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"
        val url = extras.getString("url")?:""
        val nombre = extras.getString("nombreCom")?:""

        instanciarBotonSeguir(email, nombre)

        //en la imagen de comunidad se carga la imagen correxpondiente a la comunidad
        val imagen = findViewById<ImageView>(R.id.imageView)
        Glide.with(applicationContext).load(url).into(imagen)

        //colocamos el nombre correspondiente de la comunidad
        val nombreComunidad = findViewById<TextView>(R.id.textViewNombreComunidad)
        nombreComunidad.text = nombre

        //boton para ir atras
        val buttonBack = findViewById<ImageButton>(R.id.imageButton)
        buttonBack.setOnClickListener {
            finish()
        }

        //para cambiar a la pantalla de publicar
        val buttonPublicar = findViewById<FloatingActionButton>(R.id.fabBtnNuevaPubli)
        buttonPublicar.setOnClickListener {
            val prIntent : Intent = Intent(this,PantallaPublicar::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
                putExtra("nombreCom", nombre)
            }
            startActivity(prIntent)
        }

        buttonSeguir = findViewById<Button>(R.id.buttonSeguir)
        buttonSeguir.setOnClickListener {
            val seguida=Seguidas()
            seguida.nombre = nombre
            if(buttonSeguir.text == "Siguiendo"){
                eliminarComunidadSeguida(email, seguida)
            }
            else{
                añadirComunidadSeguida(email, seguida)
            }
            instanciarBotonSeguir(email, nombre)
        }
    }

    fun añadirComunidadSeguida(email:String, seguida: Seguidas){
        val db = Firebase.firestore
        db.collection("usuario")
            .whereEqualTo("usuario", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents!= null && documents.documents.count()>0){
                    for (document in documents){
                        val id = document.id
                        coleccionSeguidas(id, seguida)
                    }
                }
            }
    }

    fun eliminarComunidadSeguida(email:String, seguida: Seguidas){
        val db = Firebase.firestore
        db.collection("usuario")
            .whereEqualTo("usuario", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents!= null && documents.documents.count()>0){
                    for (document in documents){
                        val id = document.id
                        coleccionSeguidas1(id, seguida)
                    }
                }
            }
    }

    fun coleccionSeguidas(id:String, seguida: Seguidas){
        val db = Firebase.firestore
        db.collection("usuario/$id/Seguidas")
            .add(seguida)
    }

    fun coleccionSeguidas1(id:String, seguida: Seguidas){
        val db = Firebase.firestore
        db.collection("usuario/$id/Seguidas")
            .whereEqualTo("nombre", seguida.nombre)
            .get()
            .addOnSuccessListener { documents ->
                if (documents!= null && documents.documents.count()>0){
                    for (document in documents){
                        val db2 = Firebase.firestore
                        val id2 = document.id
                        db2.collection("usuario/$id/Seguidas")
                            .document(id2)
                            .delete()
                    }
                }
            }
    }

    fun instanciarBotonSeguir(email:String, nombreCom: String){
        val db = Firebase.firestore
        db.collection("usuario")
            .whereEqualTo("usuario", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents!= null && documents.documents.count()>0){
                    for (document in documents){
                        val id = document.id
                        collecionUsuario(id, nombreCom)
                    }
                }
            }
    }

    fun collecionUsuario(id: String, nombreCom: String){
        val db = Firebase.firestore
        db.collection("usuario/$id/Seguidas")
            .whereEqualTo("nombre", nombreCom)
            .get()
            .addOnSuccessListener { documents ->
                if (documents!= null && documents.documents.count()>0){
                    buttonSeguir.text = resources.getString(R.string.textSiguiendo)
                    buttonSeguir.setBackgroundColor(Color.parseColor("#88BBEA"))
                    buttonSeguir.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else{
                    buttonSeguir.text = resources.getString(R.string.textSeguir)
                    buttonSeguir.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    buttonSeguir.setTextColor(Color.parseColor("#88BBEA"))
                }
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