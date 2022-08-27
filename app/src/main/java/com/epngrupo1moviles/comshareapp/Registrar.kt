package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Registrar : AppCompatActivity() {

    lateinit var buttonRegistrar: Button
    lateinit var editTextCorreo: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextConfirmarPassword: EditText
    lateinit var checkBoxConfirmarAños: CheckBox



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        buttonRegistrar = findViewById(R.id.btnRegistrar)
        editTextCorreo = findViewById(R.id.editTextCorreoElectronico)
        editTextPassword  = findViewById(R.id.editTextContrasena)
        editTextConfirmarPassword  = findViewById(R.id.editTextConfirmarContrasena)
        checkBoxConfirmarAños  = findViewById(R.id.checkBoxEdad)



        buttonRegistrar.setOnClickListener{
            var correoUsuario = editTextCorreo.text.toString()
            var contrasenaUsuario = editTextPassword.text.toString()

                if(validarDatosRequeridos()){
                    val  informacion_usuario = Comunity(correoUsuario,contrasenaUsuario)
                    registrarNuevoUsuario(informacion_usuario)
                    limpiarCajas(editTextCorreo,editTextPassword,editTextConfirmarPassword,checkBoxConfirmarAños)
                }

        }

    }

    //inicializar Firebase

    fun registrarNuevoUsuario(comunity: Comunity){
        //inicializar firebase
        val db = Firebase.firestore
        db.collection("usuario")
            .add(comunity)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this,"Usuario registrado existosamente",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener{
                exception ->
                Log.w( "Error adding document", exception)
                Toast.makeText(this,"Error al ingresar el usuario", Toast.LENGTH_LONG).show()
            }
    }


    private fun validarDatosRequeridos():Boolean{
        val email = editTextCorreo.text.toString()
        val contrasena = editTextPassword.text.toString()
        val confirmarContrasena = editTextConfirmarPassword.text.toString()
        if(email.isEmpty()&&contrasena.isEmpty() && confirmarContrasena.isEmpty() ){
            Toast.makeText(baseContext, "Completa todos los campos",
                Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            editTextCorreo.setError("El campo del email es obligatorio")
            editTextCorreo.requestFocus()
            return false
        }
        if (!validarEmail(email)){
            editTextCorreo.setError("Correo electronico invalido")
            editTextCorreo.requestFocus()
            return false
        }
        if (contrasena.isEmpty()) {
            editTextPassword.setError("El campo de contraseña es obligatorio")
            editTextPassword.requestFocus()
            return false
        }
        if (confirmarContrasena.isEmpty()) {
            editTextConfirmarPassword.setError("El campo de contraseña es obligatorio")
            editTextConfirmarPassword.requestFocus()
            return false
        }

        if (contrasena.length < 8) {
            editTextPassword.setError("La longitud minima de la contraseña es 8 caracteres")
            editTextPassword.requestFocus()
            return false
        }
        if (confirmarContrasena.length < 8) {
            editTextConfirmarPassword.setError("La longitud minima de la contraseña es 8 caracteres")
            editTextConfirmarPassword.requestFocus()
            return false
        }
        if(!contrasena.contentEquals(confirmarContrasena)){
            editTextConfirmarPassword.setError("Las contraseñas deben coincidir")
            editTextConfirmarPassword.requestFocus()
            return false
        }
        if(!checkBoxConfirmarAños.isChecked){
            checkBoxConfirmarAños.setError("Debe confirmar que es mayor de edad")
            checkBoxConfirmarAños.requestFocus()
            return false
        }

        return true
    }

    private fun validarEmail(email: String): Boolean{

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }
    private fun limpiarCajas(email: EditText,contrasena: EditText,confirmarContrasena: EditText,checkbox: CheckBox){
        email.setText("")
        contrasena.setText("")
        confirmarContrasena.setText("")
        checkbox.isChecked = false;


    }
}