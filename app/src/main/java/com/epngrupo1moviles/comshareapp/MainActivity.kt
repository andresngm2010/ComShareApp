package com.epngrupo1moviles.comshareapp

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.nio.file.attribute.AclEntry

enum class ProviderType{
    BASIC,GOOGLE
}


class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var archivoManejador : FileHandler
    private val GOOGLE_SIGN_IN = 100
    lateinit var btnIngresarGoogle: Button
    //lateinit var btnIngresarFacebook: Button
    lateinit var correoEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var checkRecordarme : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        editTextEmail = findViewById(R.id.txtViewUsuario)
        editTextPassword = findViewById(R.id.txtViewContrasena)

        btnIngresarGoogle = findViewById(R.id.btnIniciarSesionGoogle)
        correoEditText = findViewById(R.id.txtViewUsuario)
        passwordEditText = findViewById(R.id.txtViewContrasena)

        archivoManejador = SharedPreferencesManager(this)
        checkRecordarme = findViewById(R.id.checkBoxRecordarme)
        LeerDatosDePreferencias()


        btnIngresarGoogle.setOnClickListener {
            //CONFIGURACION
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            // se realiza un logout de la cuenta autenticada ese momento y cmabia a otra cuenta de google
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        //boton para iniciar sesion de forma normal con correo y contraseña
        val butonIniciarSesion = findViewById<Button>(R.id.btnIngresar)
        butonIniciarSesion.setOnClickListener {
            //se leen los datos correspondientes
            var correoUsuario = correoEditText.text.toString()
            var contrasenaUsuario = passwordEditText.text.toString()

            if (validarDatosRequeridos()) {//se verifican los datos
                //hacemos la autenticacion en Firebase para correo y contraseña

                GuardarDatosEnPreferencias()
                if (validarDatosRequeridos()) {

                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(correoUsuario, contrasenaUsuario)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                cambioActividad(it.result?.user?.email ?: "", ProviderType.BASIC) //se hace el cambio a la Pantalla Principal
                            } else {
                                showAlert()
                            }

                        }
                }
            }
        }

        val textRegistrarse = findViewById<TextView>(R.id.txtViewRegistrar)
        textRegistrarse.setOnClickListener {
            val prIntent =
                Intent(this, Registrar::class.java) //abrimos la actividad para registrarse
            startActivity(prIntent)
        }
    }

    private fun LeerDatosDePreferencias(){
        val listadoLeido = archivoManejador.ReadInformation()
        if(listadoLeido.first != null){
            checkRecordarme.isChecked = true
        }
        editTextEmail.setText ( listadoLeido.first )
        editTextPassword.setText ( listadoLeido.second )
    }

    fun registrarUsuarioGoogleEnFirestore(nombre: String, url: String){
        val db = Firebase.firestore
        var usuario = Usuario()
        usuario.usuario = nombre
        //usuario.contraseña = contrasena
        usuario.imagen = url
        db.collection("usuario")
            .add(usuario)
    }

    fun GuardarDatosEnPreferencias(){

        val email = editTextEmail.text.toString()
        val contrasena = editTextPassword.text.toString()
        val listadoAGrabar:Pair<String,String>
        if(checkRecordarme.isChecked){
            listadoAGrabar = email to contrasena
        }
        else{
            listadoAGrabar ="" to ""
        }
        archivoManejador.SaveInformation(listadoAGrabar)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //se recupera la cuenta autenticada
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance()
                    .signInWithCredential(credential)
                    .addOnCompleteListener {
                    //autenticar en firebase
                    if (it.isSuccessful) {

                        cambioActividad(account.email ?: "", ProviderType.GOOGLE)
                        // registrar en firebase
                        registrarUsuarioGoogleEnFirestore(account.email.toString(),"https://img2.freepng.es/20190702/tl/kisspng-computer-icons-portable-network-graphics-avatar-tr-clip-directory-professional-transparent-amp-png-5d1bfa95e508d4.2980489715621147099381.jpg")
                    } else {
                        showAlert()
                    }
                }
            } catch (e: ApiException) {
                showAlert()
            }
        }
    }

    private fun validarDatosRequeridos():Boolean{
        //coger los textos de los campos
        val email = correoEditText.text.toString()
        val contrasena = passwordEditText.text.toString()
        if(email.isEmpty()&&contrasena.isEmpty() ){//verifica si los campos estan vacios
            Toast.makeText(baseContext, "Completa todos los campos",
                Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) { //si el email esta vacio
            correoEditText.setError("El campo del email es obligatorio")
            correoEditText.requestFocus()
            return false
        }
        if (!validarEmail(email)){ //valida que el correo electronico sea valido
            correoEditText.setError("Correo electronico invalido")
            correoEditText.requestFocus()
            return false
        }
        if (contrasena.isEmpty()) { //si la contraseña esta vacia
            passwordEditText.setError("El campo de contraseña es obligatorio")
            passwordEditText.requestFocus()
            return false
        }
        if (contrasena.length < 8) { //valida la longitud de la contraseña
            passwordEditText.setError("La longitud minima de la contraseña es 8 caracteres")
            passwordEditText.requestFocus()
            return false
        }
        return true
    }

    private fun validarEmail(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    private fun showAlert() { //muestra una alerta cuando no se ha podido auntenticar correctamente
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun cambioActividad(email: String, provider: ProviderType) { //funcion para cambiar a la Pantalla Principal
        if (provider == ProviderType.GOOGLE){
            Toast.makeText(this,"Se ha autenticado con Google",Toast.LENGTH_SHORT).show()
        }
        if (provider == ProviderType.BASIC){
            Toast.makeText(this,"Se ha autenticado de forma basica",Toast.LENGTH_SHORT).show()
        }
        val homeIntent = Intent(this, PantallaPrincipal::class.java).apply {
            putExtra("email", email) //se colocan como extras el email y el tipo de sesion
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

}