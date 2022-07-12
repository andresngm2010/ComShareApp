package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Registrar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        val buttonRegistrar = findViewById<Button>(R.id.btnRegistrar)
        buttonRegistrar.setOnClickListener {
            val prIntent : Intent = Intent(this,TodasComunidades::class.java)
            startActivity(prIntent)
        }
    }
}