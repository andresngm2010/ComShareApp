package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EjemploComunidad : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejemplo_comunidad)

        val extras = intent.extras ?: return
        val email = extras.getString("email") ?:"Unknown"
        val provider = extras.getString("provider") ?:"Unknown"

        val buttonBack = findViewById<ImageButton>(R.id.imageButton)
        buttonBack.setOnClickListener {
            finish()
        }

        val buttonPublicar = findViewById<FloatingActionButton>(R.id.fabBtnNuevaPubli)
        buttonPublicar.setOnClickListener {
            val prIntent : Intent = Intent(this,PantallaPublicar::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(prIntent)
        }
    }
}