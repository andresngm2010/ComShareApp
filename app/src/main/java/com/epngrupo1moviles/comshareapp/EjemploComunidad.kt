package com.epngrupo1moviles.comshareapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EjemploComunidad : AppCompatActivity() {
    lateinit var buttonSeguir:Button
    lateinit var publicaciones: ArrayList<Publicacion>
    lateinit var usuario1: TextView
    lateinit var fecha1: TextView
    lateinit var titulo1: TextView
    lateinit var contenido1: TextView
    lateinit var usuario2: TextView
    lateinit var fecha2: TextView
    lateinit var titulo2: TextView
    lateinit var contenido2: TextView
    lateinit var usuario3: TextView
    lateinit var fecha3: TextView
    lateinit var titulo3: TextView
    lateinit var contenido3: TextView
    lateinit var usuario4: TextView
    lateinit var fecha4: TextView
    lateinit var titulo4: TextView
    lateinit var contenido4: TextView
    lateinit var usuario5: TextView
    lateinit var fecha5: TextView
    lateinit var titulo5: TextView
    lateinit var contenido5: TextView
    lateinit var usuario6: TextView
    lateinit var fecha6: TextView
    lateinit var titulo6: TextView
    lateinit var contenido6: TextView
    lateinit var usuario7: TextView
    lateinit var fecha7: TextView
    lateinit var titulo7: TextView
    lateinit var contenido7: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejemplo_comunidad)

        //obtenemos todos los extras
        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"
        val url = extras.getString("url")?:""
        val nombre = extras.getString("nombreCom")?:""

        publicaciones = ArrayList<Publicacion>()

        usuario1=findViewById(R.id.usuario1)
        fecha1=findViewById(R.id.fecha1)
        titulo1=findViewById(R.id.titulo1)
        contenido1=findViewById(R.id.contenido1)
        usuario2=findViewById(R.id.usuario2)
        fecha2=findViewById(R.id.fecha2)
        titulo2=findViewById(R.id.titulo2)
        contenido2=findViewById(R.id.contenido2)
        usuario3=findViewById(R.id.usuario3)
        fecha3=findViewById(R.id.fecha3)
        titulo3=findViewById(R.id.titulo3)
        contenido3=findViewById(R.id.contenido3)
        usuario4=findViewById(R.id.usuario4)
        fecha4=findViewById(R.id.fecha4)
        titulo4=findViewById(R.id.titulo4)
        contenido4=findViewById(R.id.contenido4)
        usuario5=findViewById(R.id.usuario5)
        fecha5=findViewById(R.id.fecha5)
        titulo5=findViewById(R.id.titulo5)
        contenido5=findViewById(R.id.contenido5)
        usuario6=findViewById(R.id.usuario6)
        fecha6=findViewById(R.id.fecha6)
        titulo6=findViewById(R.id.titulo6)
        contenido6=findViewById(R.id.contenido6)
        usuario7=findViewById(R.id.usuario7)
        fecha7=findViewById(R.id.fecha7)
        titulo7=findViewById(R.id.titulo7)
        contenido7=findViewById(R.id.contenido7)

        obtenerPublicaciones(nombre,publicaciones)

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
                putExtra("url", url)
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

    override fun onStart() {
        super.onStart()
        publicaciones = ArrayList<Publicacion>()
        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"
        val url = extras.getString("url")?:""
        val nombre = extras.getString("nombreCom")?:""
        obtenerPublicaciones(nombre, publicaciones)
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


    fun obtenerPublicaciones(nombre: String,publicaciones: ArrayList<Publicacion>){
        val db = Firebase.firestore
        db.collection("publicaciones")
            .whereEqualTo("comunidad",nombre)
            .get()
            .addOnSuccessListener { documents ->
                Toast.makeText(this, "Documentos con exito", Toast.LENGTH_LONG).show()
                if(documents!= null && documents.documents.count()>0){
                    var cont = 0
                    for (document in documents) {
                        val publicacion = document.toObject(Publicacion::class.java)

                        if(cont==0){
                            usuario1.text = document.getString("usuario").toString()
                            fecha1.text = document.getString("fecha").toString()
                            titulo1.text = document.getString("titulo").toString()
                            contenido1.text = document.getString("contenido").toString()
                            cont++
                        }
                        else if (cont == 1){
                            usuario2.text = document.getString("usuario").toString()
                            fecha2.text = document.getString("fecha").toString()
                            titulo2.text = document.getString("titulo").toString()
                            contenido2.text = document.getString("contenido").toString()
                            cont++
                        }
                        else if (cont == 2){
                            usuario3.text = document.getString("usuario").toString()
                            fecha3.text = document.getString("fecha").toString()
                            titulo3.text = document.getString("titulo").toString()
                            contenido3.text = document.getString("contenido").toString()
                            cont++
                        }
                        else if (cont == 3){
                            usuario4.text = document.getString("usuario").toString()
                            fecha4.text = document.getString("fecha").toString()
                            titulo4.text = document.getString("titulo").toString()
                            contenido4.text = document.getString("contenido").toString()
                            cont++
                        }
                        else if (cont == 4){
                            usuario5.text = document.getString("usuario").toString()
                            fecha5.text = document.getString("fecha").toString()
                            titulo5.text = document.getString("titulo").toString()
                            contenido5.text = document.getString("contenido").toString()
                            cont++
                        }
                        publicaciones.add(publicacion)
                    }

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