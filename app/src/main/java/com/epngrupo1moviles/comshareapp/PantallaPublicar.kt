package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class PantallaPublicar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_publicar)

        val buttonClose = findViewById<ImageButton>(R.id.imageButtonCerrar)
        buttonClose.setOnClickListener {
            finish()
        }

        val buttonPublicar = findViewById<Button>(R.id.btnPublicar)
        buttonPublicar.setOnClickListener {
            finish()
        }
    }
}