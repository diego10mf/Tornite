package net.azarquiel.tornite.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_codigo.*
import net.azarquiel.tornite.R
import net.azarquiel.tornite.adapters.CustomAdaptercod
import net.azarquiel.tornite.model.Cod

/**
 * Created by diego10mf on 25/05/20.
 */
class Codigo : AppCompatActivity() {
    private lateinit var adapter: CustomAdaptercod
    private lateinit var db: FirebaseFirestore

    private var codigos: ArrayList<Cod> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codigo)
        db = FirebaseFirestore.getInstance();
        setListener()
        //getData()
        initRV()
    }

    // ****************************************************** <Init RV> ************************************************************
    private fun initRV() {
        adapter = CustomAdaptercod(this, R.layout.rowcod)
        rvcod.adapter = adapter
        rvcod.layoutManager = LinearLayoutManager(this)
    }


    // ****************************************************** < Saca los codigos> ************************************************************
    private fun setListener() {
        val docRef = db.collection("codigos")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(MainActivity.TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && !snapshot.isEmpty) {
                documentToList(snapshot.documents)
                adapter.setCodigos(codigos)


            } else {
                Log.d(MainActivity.TAG, "Current data: null")
            }
        }
    }
    private fun documentToList(documents: List<DocumentSnapshot>) {
        codigos.clear()
        documents.forEach { d ->
            val partida = d["partida"] as String
            val codigo = d["codigo"] as String


            codigos.add(Cod(partida = partida, codigo = codigo))
        }
    }
    private fun getData() {
        db.collection("codigos")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        Log.d(MainActivity.TAG,document.id + " => " + document.data)
                        procesarData(document.data)
                    }
                } else {
                    Log.w(
                        MainActivity.TAG,"Error getting documents.", task.exception
                    )
                }
            }
    }
    private fun procesarData(data: Map<String, Any>) {
        for ((k, v) in data){
            Log.d(MainActivity.TAG, "$k => $v")
        }
    }


    // ****************************************************** < Menú > ************************************************************
    override fun onCreateOptionsMenu(menu: Menu): Boolean {


        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menudetalle, menu)


        return true
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
        val  message = "\"wwww.fortnite.com - www.instagram.com/torneosfortnitetoledo\"";
        val builder = AlertDialog.Builder(this)
        builder.setTitle("TORNITE by Diego Martín");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.create().show();
    }
}
