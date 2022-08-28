package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.nio.file.attribute.AclEntry

enum class ProviderType{
    BASIC,GOOGLE
}

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN = 100
    // ingreso con GOOGLE
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

    lateinit var btnIngresarGoogle: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnIngresarGoogle = findViewById(R.id.btnIniciarSesionGoogle)

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

            val butonIniciarSesionGoogle = findViewById<Button>(R.id.btnIniciarSesionGoogle)
            butonIniciarSesionGoogle.setOnClickListener {
                val prIntent: Intent = Intent(this, PantallaPrincipal::class.java)
                startActivity(prIntent)
            }

            val butonIniciarSesion = findViewById<Button>(R.id.btnIngresar)
            butonIniciarSesion.setOnClickListener {
                val prIntent: Intent = Intent(this, PantallaPrincipal::class.java)
                startActivity(prIntent)
            }

            val textRegistrarse = findViewById<TextView>(R.id.txtViewRegistrar)
            textRegistrarse.setOnClickListener {
                val prIntent: Intent = Intent(this, Registrar::class.java)
                startActivity(prIntent)
            }
        }

    }

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
                        CambioActividad(account.email ?: "", ProviderType.GOOGLE)
                    } else {
                        showAlert()
                    }
                }
            } catch (e: ApiException) {
                showAlert()
            }

        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun CambioActividad(email: String, provider: ProviderType) {
        Toast.makeText(this,"Se ha autenticado con Google",Toast.LENGTH_SHORT).show()
        val homeIntent = Intent(this, PantallaPrincipal::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}