package com.epngrupo1moviles.comshareapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PantallaPrincipal : AppCompatActivity() {
    lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)

        usuario = Usuario() //instanciamos una clase de tipo Usuario

        //conseguimos todos los extras que fueron enviados anteriormente
        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"

        //conseguir el usuario que iniciado sesion
        usuario = conseguirUsuario(email)

        //imagen para la publicacion de la pantalla principal
        val imagenPublicacion = findViewById<ImageView>(R.id.imageViewPublicacion)
        val url = "https://static.wikia.nocookie.net/naruto/images/a/a2/Naruto_Uzumaki_Parte_II_Anime.png/revision/latest?cb=20161013194453&path-prefix=es"
        Glide.with(applicationContext).load(url).into(imagenPublicacion)

        //boton para ir a la pantalla de todas las comunidades
        val buttonTodasComunidades = findViewById<ImageButton>(R.id.imageButtonTodasComunidades)
        buttonTodasComunidades.setOnClickListener {
            val prIntent : Intent = Intent(this,TodasComunidades::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

        //boton para ir a la pantalla de busqueda
        val buttonBuscar = findViewById<ImageButton>(R.id.imageButtonBuscar)
        buttonBuscar.setOnClickListener {
            val prIntent : Intent = Intent(this,Busqueda::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

        //imagen que lleva a la pantalla de todas las comunidades
        val todasCom = findViewById<ImageView>(R.id.imageViewTodasCom)
        todasCom.setOnClickListener {
            val prIntent : Intent = Intent(this,TodasComunidades::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

    }

    //funcion que nos permite conseguir los datos del usuario instanciado en firestore
    private fun conseguirUsuario(email: String):Usuario{
        var usuario = Usuario() //instanciamos un usuario
        //conectamos con la base de datos de firestore
        val db = Firebase.firestore
        db.collection("usuario") //buscamos en la coleccion de usuario
            .whereEqualTo("usuario", email) //con el email que recibimos
            .get()
            .addOnSuccessListener { documents ->
                if(documents!= null && documents.documents.count()>0){ //si se encontro el documento
                    for (document in documents){
                        usuario.usuario = email //instanciamos el email del usuario
                        usuario.contraseña = document.getString("contraseña").toString() //instanciamos la contraseña
                        usuario.imagen = document.getString("imagen").toString() //instanciamos la URL de su imagen de avatar
                        val id = document.id //conseguimos el id que pertenece a este documento
                        val avatar = findViewById<ImageView>(R.id.imageViewAvatar) //buscamos la imageView para el avatar
                        Glide.with(applicationContext).load(usuario.imagen).into(avatar) //ponemos su imagen de avatar
                        poblarComunidades(id) //poblamos las comunidades a las que esta siguiendo el usuario
                        return@addOnSuccessListener
                    }
                }
                else{
                    Log.d(TAG, "no se encontro el documento")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error consiguiendo los documentos", exception)
                Toast.makeText(this, "Error al obtener datos de usuario", Toast.LENGTH_LONG)
                    .show()
            }
        return usuario
    }

    @SuppressLint("CutPasteId")
    private fun poblarComunidades(id: String){ //funcion para conseguir las comunidades seguidas por el usuario
        //conectamos a firestore
        val db = Firebase.firestore
        db.collection("usuario/$id/Seguidas")//buscamos la coleccion de Seguidas con el path del usuario
            .get()
            .addOnSuccessListener { result ->
                var cont = 0
                Log.d(TAG, "Exito al cargar los nombres") //se cargaron correctamente los nombres de las colecciones seguidas
                for (document in result){
                    val db2 = Firebase.firestore
                    db2.collection("Comunidades") //buscamos los nombres seguidos en la coleccion de Comunidades
                        .whereEqualTo("nombre", document.getString("nombre").toString())
                        .get()
                        .addOnSuccessListener { documents ->
                            if(documents!= null && documents.documents.count()>0
                            ){
                                for (document in documents){
                                    //cargamos las imagenes correspondientes en los imageView de la Pantalla para
                                    //mostrar las 3 primeras comunidades que sigue el usuario
                                    if (cont==0){
                                        val imagen1 = findViewById<ImageView>(R.id.imageViewPrimera)
                                        val url:String = document.getString("URL").toString()
                                        Glide.with(applicationContext).load(url).into(imagen1)
                                        cont++
                                    }
                                    else if(cont==1){
                                        val imagen1 = findViewById<ImageView>(R.id.imageViewSegunda)
                                        val url:String = document.getString("URL").toString()
                                        Glide.with(applicationContext).load(url).into(imagen1)
                                        cont++
                                    }
                                    else if(cont==2){
                                        val imagen1 = findViewById<ImageView>(R.id.imageViewTercera)
                                        val url:String = document.getString("URL").toString()
                                        Glide.with(applicationContext).load(url).into(imagen1)
                                        cont++
                                    }
                                }
                            }
                        }
                }
            }
    }

    //para mostrar el menu en la pantalla
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cerrar_sesion,menu)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        return true
    }

    //funcionalidad del menu creado
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