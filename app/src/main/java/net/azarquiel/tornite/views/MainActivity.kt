package net.azarquiel.tornite.views

import android.app.Notification
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import net.azarquiel.tornite.R
import net.azarquiel.tornite.adapters.CustomAdapter
import net.azarquiel.tornite.model.Torneo
import org.jetbrains.anko.*

/**
 * Created by diego10mf on 25/05/20.
 */

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener,  NavigationView.OnNavigationItemSelectedListener {
    companion object {
        const val TAG = "TORNITE2020"
    }


    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: CustomAdapter
    private var torneos: ArrayList<Torneo> = ArrayList()
    private lateinit var torneo :Torneo
    private var CHANNEL_ID:String = "Notificación"
    private var NOTIFICATION_ID:Int = 0

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //getData()
        db = FirebaseFirestore.getInstance();
        initRV()
        setListener()
        crearnotificacion()

        //*************************************< Boton flotante >*********************************************************************
        fab.setOnClickListener {
            mostrarinfo()
            // addalertData() }
        }
    }

    // ****************************************************** < onclick botones> **************************************************

    fun mostrarinfo(){
        Toast.makeText(this, "Lo siento, crear torneo todavía no está disponible", Toast.LENGTH_LONG).show()
    }

    fun onClickTorneo(v : View){
        val torneo = v.tag as Torneo
        val intent = Intent(this, DetalleActivity::class.java)
        intent.putExtra("torneo", torneo)

        startActivity(intent)
    }


    // ****************************************************** <Init RV> ************************************************************
    private fun initRV() {
        adapter = CustomAdapter(this, R.layout.rowtorneo)
        rvtorneos.adapter = adapter
        rvtorneos.layoutManager = LinearLayoutManager(this)
    }


    // ****************************************************** <Saca los Torneos> ************************************************************
    private fun setListener() {

        val docRef = db.collection("torneos")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapter.setTorneos(torneos)
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }
    private fun documentToList(documents: List<DocumentSnapshot>) {
        torneos.clear()
        documents.forEach { d ->
            val id  =d["id"] as String
            val titulo = d["titulo"] as String
            val fecha = d["fecha"] as String
            val comienzo = d["comienzo"] as String
            val premio = d["premio"] as String

            torneos.add(Torneo(id =id, titulo = titulo, fecha = fecha, comienzo = comienzo, premio = premio ))
        }
    }

    private fun getData() {
        db.collection("torneos")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(TAG,document.id + " => " + document.data)
                        procesarData(document.data)
                    }
                } else {
                    Log.w(
                        TAG,"Error getting documents.", task.exception
                    )
                }
            }
    }
    private fun procesarData(data: Map<String, Any>) {
        for ((k, v) in data){
            Log.d(TAG, "$k => $v")
        }
    }

    // ****************************************************** <Alerta nuevo torneo> ************************************************************
    private fun addalertData() {
        alert {

            title = "Nuevo Torneo"

            customView {
                verticalLayout {
                    backgroundResource = R.drawable.fondo
                    lparams(width = wrapContent, height = wrapContent)

                    id = (1..100).random();

                    val ettitulo = editText {
                        hint = "Nombre torneo"
                        padding = dip(16)

                    }
                    val etfecha = editText {
                        hint = "Fecha de hoy"
                        padding = dip(16)
                    }
                    val etcomienzo = editText {

                        hint = "Comienzo torneo"
                        padding = dip(16)
                    }
                    val etpremio = editText {

                        hint = "Premio"
                        padding = dip(16)
                    }
                    positiveButton("Aceptar") {
                        if (ettitulo.text.toString().isEmpty() || etfecha.text.toString()
                                .isEmpty() || etcomienzo.text.toString().isEmpty() || etpremio.text.toString().isEmpty()
                        )
                            toast("Campos Obligatorios")
                        else
                            addData(id.toString(), ettitulo.text.toString(), etfecha.text.toString(), etcomienzo.text.toString(), etpremio.text.toString())

                    }
                }
            }
        }.show()
    }

    private fun addData(id: String, titulo: String, fecha: String, comienzo: String, premio: String) {
        val torneo: MutableMap<String, Any> = HashMap() // diccionario key value
        torneo["titulo"] = titulo
        torneo["fecha"] = fecha
        torneo["comienzo"] = comienzo
        torneo["premio"] = premio
        torneo["id"] =  id
        db.collection("torneos")
            .add(torneo)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->

                Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.id)
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.w(TAG,"Error adding document", e)
            })

    }


    // ****************************************************** <Drawer navigation(no utilizado)> ************************************************************
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.nav_login -> {
              //  dialoglogin()
            }
            R.id.nav_register -> {
               // dialogregister()
            }
            R.id.nav_logout -> {
                //  logout()
            }
            R.id.nav_info -> {
                acercade()
            }
            R.id.nav_salir -> {
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    // **************************************** <Volver atrás sin salir de la app> *********************************************************
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    // ****************************************************** <Filtro búsqueda y menú> ************************************************************

    override fun onCreateOptionsMenu(menu: Menu): Boolean {


        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Buscar torneo...")
        searchView.setOnQueryTextListener(this)

        return true
    }

    // ******* <Filtro> ******
    override fun onQueryTextChange(query: String): Boolean {
        val original = ArrayList<Torneo>(torneos)
        adapter.setTorneos(original.filter { torneo -> torneo.titulo.contains(query) })
        return false
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_salir -> {
                if (getIntent().getBooleanExtra("EXIT", false)) {
                    finish();
                }
                finishAffinity()
                finish()

                true
            }
            R.id.acerca -> {
                acercade()
                true
            }

            R.id.action_logout -> {
                startActivity(Intent(this,LoginActivity::class.java))
                FirebaseAuth.getInstance().signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
    private fun acercade(){
        val  message = "\"wwww.fortnite.com - www.instagram.com/torneosfortnitetoledo\"";
        val builder = AlertDialog.Builder(this)
        builder.setTitle("TORNITE by Diego Martín");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.create().show();
    }
    // ****************************************************** <Notificaciones> ************************************************************
    private fun crearnotificacion() {
        if (torneos != null) {
            var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            builder.setSmallIcon(R.drawable.ic_plus_one_black_24dp)
            builder.setContentTitle("Notificación Tornite")
            builder.setContentText("Hay un NUEVO torneo disponible en la app")
            builder.setColor(Color.BLUE)
            builder.setPriority(NotificationCompat.PRIORITY_HIGH)
            builder.setLights(Color.YELLOW, 1000, 1000)
            builder.setDefaults(Notification.DEFAULT_SOUND)
            var nmc = NotificationManagerCompat.from(applicationContext)
            nmc.notify(NOTIFICATION_ID, builder.build())
        }
    }
}
