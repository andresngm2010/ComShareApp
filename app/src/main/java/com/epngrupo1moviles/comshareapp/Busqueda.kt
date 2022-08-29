package com.epngrupo1moviles.comshareapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Busqueda :  AppCompatActivity(){
    lateinit var editTextBusqueda: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda)

        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"

        editTextBusqueda = findViewById<EditText>(R.id.editViewBuscar)

        val buttonHome = findViewById<ImageButton>(R.id.imageButtonHome)
        buttonHome.setOnClickListener {
            val prIntent : Intent = Intent(this,PantallaPrincipal::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

        val buttonBuscar = findViewById<ImageButton>(R.id.imageButtonBusqueda)
        buttonBuscar.setOnClickListener{
            val busqueda:String = editTextBusqueda.text.toString()
            cargarBusqueda(busqueda)
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

    fun cargarBusqueda(name: String ){
        limpiarBusqueda()
        val db = Firebase.firestore
        db.collection("Comunidades")
            .get()
            .addOnSuccessListener { documents ->
                Toast.makeText(this, "Documentos con exito", Toast.LENGTH_LONG).show()
                if(documents!= null && documents.documents.count()>0){
                    var count = 1
                    for (document in documents){
                        if(count < 9){
                            val nombre = document.getString("nombre").toString()
                            val url = document.getString("URL").toString()
                            if(nombre.contains(name)){
                                val id = resources.getIdentifier("imageViewB$count", "id", packageName)
                                val imagen = findViewById<ImageView>(id)
                                Glide.with(applicationContext).load(url).into(imagen)
                                CambiarEJComunidad(imagen, url, nombre)
                                count++
                            }
                        }
                    }
                }

            }

    }

    fun limpiarBusqueda(){
        for( i in 1..8 ){
            val id = resources.getIdentifier("imageViewB$i", "id", packageName)
            val imagen = findViewById<ImageView>(id)
            Glide.with(applicationContext).load("").into(imagen)
            imagen.setOnClickListener {

            }
        }
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
