package com.epngrupo1moviles.comshareapp

import android.content.ContentValues.TAG
import android.content.Intent
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
import com.google.firebase.ktx.Firebase
import java.nio.file.attribute.AclEntry

enum class ProviderType{
    BASIC,GOOGLE,FACEBOOK
}

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText

    private val GOOGLE_SIGN_IN = 100
    //private val callbackManager = CallbackManager.Factory.create()
    // ingreso con GOOGLE
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

    lateinit var btnIngresarGoogle: Button
    //lateinit var btnIngresarFacebook: Button
    lateinit var correoEditText: EditText
    lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        editTextEmail = findViewById(R.id.txtViewUsuario)
        editTextPassword = findViewById(R.id.txtViewContrasena)

        btnIngresarGoogle = findViewById(R.id.btnIniciarSesionGoogle)
        correoEditText = findViewById(R.id.txtViewUsuario)
        passwordEditText = findViewById(R.id.txtViewContrasena)


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

        val butonIniciarSesion = findViewById<Button>(R.id.btnIngresar)
        butonIniciarSesion.setOnClickListener {
            var correoUsuario = correoEditText.text.toString()
            var contrasenaUsuario = passwordEditText.text.toString()
            if (validarDatosRequeridos()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(correoUsuario, contrasenaUsuario)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            cambioActividad(it.result?.user?.email ?: "", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }

                    }
            }
        }

        val textRegistrarse = findViewById<TextView>(R.id.txtViewRegistrar)
        textRegistrarse.setOnClickListener {
            val prIntent = Intent(this, Registrar::class.java)
            startActivity(prIntent)
        }
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
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                    //autenticar en firebase
                    if (it.isSuccessful) {
                        cambioActividad(account.email ?: "", ProviderType.GOOGLE)
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
        val email = correoEditText.text.toString()
        val contrasena = passwordEditText.text.toString()
        if(email.isEmpty()&&contrasena.isEmpty() ){
            Toast.makeText(baseContext, "Completa todos los campos",
                Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            correoEditText.setError("El campo del email es obligatorio")
            correoEditText.requestFocus()
            return false
        }
        if (!validarEmail(email)){
            correoEditText.setError("Correo electronico invalido")
            correoEditText.requestFocus()
            return false
        }
        if (contrasena.isEmpty()) {
            passwordEditText.setError("El campo de contraseña es obligatorio")
            passwordEditText.requestFocus()
            return false
        }


        if (contrasena.length < 8) {
            passwordEditText.setError("La longitud minima de la contraseña es 8 caracteres")
            passwordEditText.requestFocus()
            return false
        }


        /*if(!checkBoxConfirmarAños.isChecked){
            checkBoxConfirmarAños.setError("Debe confirmar que es mayor de edad")
            checkBoxConfirmarAños.requestFocus()
            return false
        }*/

        return true
    }

    private fun validarEmail(email: String): Boolean{

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun cambioActividad(email: String, provider: ProviderType) {
        if (provider == ProviderType.GOOGLE){
            Toast.makeText(this,"Se ha autenticado con Google",Toast.LENGTH_SHORT).show()
        }
        if (provider == ProviderType.BASIC){
            Toast.makeText(this,"Se ha autenticado de forma basica",Toast.LENGTH_SHORT).show()
        }
        val homeIntent = Intent(this, PantallaPrincipal::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

    private fun signInBasic(email: String, contraseña: String){
        auth.signInWithEmailAndPassword(email, contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    cambioActividad(email, ProviderType.BASIC)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }
}