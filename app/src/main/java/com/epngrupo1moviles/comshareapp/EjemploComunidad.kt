package com.epngrupo1moviles.comshareapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EjemploComunidad : AppCompatActivity() {
    lateinit var buttonSeguir:Button
    lateinit var publicaciones: ArrayList<Publicacion>
    lateinit var usuario1: TextView
    lateinit var fecha1: TextView
    lateinit var titulo1: TextView
    lateinit var contenido1: TextView
    lateinit var usuario2: TextView
    lateinit var fecha2: TextView
    lateinit var titulo2: TextView
    lateinit var contenido2: TextView
    lateinit var usuario3: TextView
    lateinit var fecha3: TextView
    lateinit var titulo3: TextView
    lateinit var contenido3: TextView
    lateinit var usuario4: TextView
    lateinit var fecha4: TextView
    lateinit var titulo4: TextView
    lateinit var contenido4: TextView
    lateinit var usuario5: TextView
    lateinit var fecha5: TextView
    lateinit var titulo5: TextView
    lateinit var contenido5: TextView
    lateinit var usuario6: TextView
    lateinit var fecha6: TextView
    lateinit var titulo6: TextView
    lateinit var contenido6: TextView
    lateinit var usuario7: TextView
    lateinit var fecha7: TextView
    lateinit var titulo7: TextView
    lateinit var contenido7: TextView




    override fun onCreate(savedInstanceState: Bundle?) {
       
}