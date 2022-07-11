package com.epngrupo1moviles.comshareapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prIntent : Intent = Intent(this,TodasComunidades::class.java)
        startActivity(prIntent)
    }

}