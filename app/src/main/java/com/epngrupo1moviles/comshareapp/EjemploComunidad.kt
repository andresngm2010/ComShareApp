package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class EjemploComunidad : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejemplo_comunidad)

        val buttonBack = findViewById<ImageButton>(R.id.imageButton)
        buttonBack.setOnClickListener {
            finish()
        }
    }
}