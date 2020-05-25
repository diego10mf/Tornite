package net.azarquiel.tornite.views

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detalle.*
import kotlinx.android.synthetic.main.activity_main.*
import net.azarquiel.tornite.R
import net.azarquiel.tornite.adapters.CustomAdapterParticipantes
import net.azarquiel.tornite.model.Cod
import net.azarquiel.tornite.model.Participante
import net.azarquiel.tornite.model.Torneo
import okhttp3.HttpUrl.parse
import org.jetbrains.anko.*
import retrofit2.http.Part
import retrofit2.http.Url
import java.net.HttpCookie.parse
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level.parse
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
/**
 * Created by diego10mf on 25/05/20.
 */

class DetalleActivity : AppCompatActivity(), SearchView.OnQueryTextListener {


    private lateinit var torneo: Torneo
    private lateinit var adapter: CustomAdapterParticipantes
    private lateinit var db: FirebaseFirestore
    private var count = 1;
    private var particps: ArrayList<Participante> = ArrayList()
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        torneo = intent.getSerializableExtra("torneo") as Torneo

        db = FirebaseFirestore.getInstance();

        setListener()
        initRV()

        //*************************************< Boton flotante >*********************************************************************
        fab2.setOnClickListener {
            if (particps.size <= 100) {
                if (count != 0) {
                    addalertParti()
                    count--
                } else {
                    Toast.makeText(
                        this,
                        "Sorry, solo puedes apuntarte con un usuario. Si te has equivocado consultalo con el administrador",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Lo siento, ya hay 100 jugadores apuntados!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        pintar()
    }


    // ****************************************************** <Init RV> ************************************************************
    private fun initRV() {
        adapter = CustomAdapterParticipantes(this, R.layout.rowparti)
        rvparti.adapter = adapter
        rvparti.layoutManager = LinearLayoutManager(this)
    }


    // ****************************************************** <Alerta nuevo participante> ************************************************************


    private fun addalertParti() {
        alert {

            title = "Apuntarse (Solo puedes apuntarte una vez en cada torneo)"

            customView {
                verticalLayout {
                    backgroundResource = R.drawable.fondo
                    lparams(width = wrapContent, height = wrapContent)

                    val etnick = editText {
                        hint = "Nombre/Id Fortnite"
                        padding = dip(16)
                    }
                    val etequipo = editText {
                        hint = "Nº Equipo. Si es en Duo o Escuadrones pon el mismo número que tus compañeros"
                        padding = dip(8)
                    }
                    positiveButton("Aceptar") {
                        if (etnick.text.toString().isEmpty()
                        )
                            toast("Campo Obligatorio")
                        else
                            addParti(etnick.text.toString(), etequipo.text.toString())
                    }
                }
            }
        }.show()
    }

    private fun addParti(nick: String, equipo: String) {
        val parti: MutableMap<String, Any> = HashMap() // diccionario key value
        parti["nick"] = nick
        parti["equipo"] = equipo
        db.collection("torneos").document("${torneo.id}").collection("Participantes")

            .add(parti)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Log.d(MainActivity.TAG, "DocumentSnapshot added with ID: " + documentReference.id)
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.w(MainActivity.TAG, "Error adding document", e)
            })
    }

    // ****************************************************** < Saca los participantes> ************************************************************
    private fun setListener() {
        val docRef = db.collection("torneos").document("${torneo.id}").collection("Participantes")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(MainActivity.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapter.setParti(particps)
            } else {
                Log.d(MainActivity.TAG, "Current data: null")
            }
        }
    }

    private fun documentToList(documents: List<DocumentSnapshot>) {
        particps.clear()
        documents.forEach { d ->
            val nick = d["nick"] as String
            val equipo = d["equipo"] as String
            particps.add(Participante(nick = nick, equipo = equipo))
        }
    }

    // ****************************************************** < Pinta datos de torneo> **************************************************
    private fun pintar() {
        tvfecha2.text = torneo.fecha
        tvnombre2.text = torneo.titulo
        tvcomienzo.text = torneo.comienzo
        tvpremio.text = torneo.premio
    }

    // ****************************************************** < onclick botones> **************************************************

    @SuppressLint("ResourceAsColor")
    fun onClickCod(view: View) {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        if (currentDate == torneo.comienzo) {
            startActivity(Intent(this, Codigo::class.java))
        } else {
            view.setBackgroundColor(R.color.material_on_surface_disabled)
            Toast.makeText(
                this,
                "Los códigos solo estarán disponibes el día que comience el torneo",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @SuppressLint("ResourceAsColor")
    fun onClickCla(view: View) {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        if (currentDate > torneo.comienzo) {
            val url = "https://www.instagram.com/torneosfortnitetoledo/?hl=es"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }else {
            view.setBackgroundColor(R.color.material_on_surface_disabled)
            Toast.makeText(
                this,
                "Solo se puede ver la clasificacion después de disputar el torneo",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // ****************************************************** < Menú detalle> **************************************************

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menudetalle, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Buscar participante...")
        searchView.setOnQueryTextListener(this)

        return true
    }
    // ******* <Filtro> ******
    override fun onQueryTextChange(query: String): Boolean {
        val original = ArrayList<Participante>(particps)
        adapter.setParti(original.filter { parti -> parti.nick.contains(query) })
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
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
    private fun acercade(){
        val  message = "wwww.fortnite.com - www.instagram.com/torneosfortnitetoledo";
        val builder = AlertDialog.Builder(this)
        builder.setTitle("TORNITE by Diego Martín");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.create().show();
    }


}




