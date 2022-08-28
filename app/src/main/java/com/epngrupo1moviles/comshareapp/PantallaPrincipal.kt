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

        usuario = Usuario()

        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"

        usuario = conseguirUsuario(email)

        poblarComunidades()

        /*val imagenPublicacion = findViewById<ImageView>(R.id.imageViewPublicacion)
        val url = "https://static.wikia.nocookie.net/naruto/images/a/a2/Naruto_Uzumaki_Parte_II_Anime.png/revision/latest?cb=20161013194453&path-prefix=es"
        Glide.with(applicationContext).load(url).into(imagenPublicacion)*/

        val buttonTodasComunidades = findViewById<ImageButton>(R.id.imageButtonTodasComunidades)
        buttonTodasComunidades.setOnClickListener {
            val prIntent : Intent = Intent(this,TodasComunidades::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

        val buttonBuscar = findViewById<ImageButton>(R.id.imageButtonBuscar)
        buttonBuscar.setOnClickListener {
            val prIntent : Intent = Intent(this,Busqueda::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }

    }

    private fun conseguirUsuario(email: String):Usuario{
        var usuario = Usuario()
        val db = Firebase.firestore
        db.collection("usuario")
            .whereEqualTo("usuario", email)
            .get()
            .addOnSuccessListener { documents ->
                if(documents!= null &&
                    documents.documents != null &&
                    documents.documents.count()>0
                ){
                    for (document in documents){
                        usuario.usuario = email
                        usuario.contraseña = document.getString("contraseña").toString()
                        usuario.imagen = document.getString("imagen").toString()
                        val avatar = findViewById<ImageView>(R.id.imageViewAvatar)
                        Glide.with(applicationContext).load(usuario.imagen).into(avatar)
                        return@addOnSuccessListener
                    }
                }
                else{
                    Log.d(TAG, "no se encontro el documento")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents", exception)
                Toast.makeText(this, "Error al obtener datos de usuario", Toast.LENGTH_LONG)
                    .show()
            }
        return usuario
    }

    @SuppressLint("CutPasteId")
    private fun poblarComunidades(){
        val db = Firebase.firestore
        db.collection("Comunidades")
            .get()
            .addOnSuccessListener { result ->
                val comunidades = ArrayList<Comunidad>()
                var cont=0
                for(document in result){
                    val comunidad = document.toObject(Comunidad::class.java)
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
                    comunidades.add(comunidad)
                }
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
                //onBackPressed()
                val prIntent : Intent = Intent(this,MainActivity::class.java)
                startActivity(prIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}