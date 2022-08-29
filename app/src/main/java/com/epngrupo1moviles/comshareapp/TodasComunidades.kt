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

        //para ir a la pantalla principal
        val buttonHome = findViewById<ImageButton>(R.id.imageButtonHome)
        buttonHome.setOnClickListener {
            val prIntent : Intent = Intent(this,PantallaPrincipal::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }
        //para ir a la pantalla de buscar
        val btnBuscar = findViewById<ImageButton>(R.id.imageButtonBuscar)
        btnBuscar.setOnClickListener {
            val prIntent : Intent = Intent(this,Busqueda::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

    }

    //funcion para cargar todas las comunidades desplegadas
    fun cargarComunidades(){
        //coneccion con firestore
        val db = Firebase.firestore
        db.collection("Comunidades")//buscamos en la coleccion de Comunidades
            .get()
            .addOnSuccessListener { result ->
                for (document in result){ //para cada documento encontrado
                    val nombre = document.getString("nombre").toString() //se obtiene el nombre
                    val url = document.getString("URL").toString() //se obtiene la url de la imagen desplegada
                    var aux:String = "imageView" + nombre //auxiliar para encontrar el id del imageView
                    val id:Int = resources.getIdentifier(aux, "id", packageName) //obtenemos el identificador del imageView
                    val imagen = findViewById<ImageView>(id) //obtenemos el imageView con su identificador
                    cargarImagen(imagen, url) //se carga la imagen
                    CambiarEJComunidad(imagen, url, nombre) //añadimos el listener para cambiar de pantalla
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar documentos", Toast.LENGTH_LONG).show()
            }
    }

    //funcion para cargar la imagen en un imageView
    fun cargarImagen(v: ImageView, url: String){
        Glide.with(applicationContext).load(url).into(v)
    }

    //funcion para añadir onClickListener en los imageView
    fun CambiarEJComunidad(v: ImageView, URL: String, nombreCom:String){
        //obtenemos los extras enviados
        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"
        //se añade el onClickListener
        v.setOnClickListener {
            //se cambia a la pantalla de la Comunidad
            val prIntent : Intent = Intent(this,EjemploComunidad::class.java).apply {
                //se añaden los extras necesarios
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