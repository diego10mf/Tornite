package net.azarquiel.tornite.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import net.azarquiel.tornite.R
import net.azarquiel.tornite.model.Participante
import net.azarquiel.tornite.model.Torneo

/**
 * Created by diego10mf on 25/05/20.
 */

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtNick:EditText
    private lateinit var txtInstagram:EditText
    private lateinit var txtemail:EditText
    private lateinit var txtPassword:EditText
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        txtNick=findViewById(R.id.txtNick)
        txtInstagram=findViewById(R.id.txtInstagram)
        txtemail=findViewById(R.id.txtemail)
        txtPassword=findViewById(R.id.txtPassword)


        progressBar= findViewById(R.id.progressBar)
        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference= database.reference.child("User")

    }

    fun register(view:View){
        createNewAccount()


    }
    private fun createNewAccount(){
        val nick:String=txtNick.text.toString()
        val instagram:String=txtInstagram.text.toString()
        val email:String=txtemail.text.toString()
        val password:String=txtPassword.text.toString()

        if (!TextUtils.isEmpty(nick) && !TextUtils.isEmpty(instagram) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            progressBar.visibility=View.VISIBLE

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){
                    task ->

                    if (task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        verifyEmail(user)


                        val userBD = dbReference.child(user?.uid!!)
                        userBD.child("Nick").setValue(nick)
                        userBD.child("instagram").setValue(instagram)
                        userBD.child("correo").setValue(email)
                      //  addParti(nick, instagram)
                        action()

                    }
                }
        }else{
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_LONG).show()
        }
    }


    private fun addParti(nick: String, instragram: String) {
        val parti: MutableMap<String, Any> = HashMap() // diccionario key value
        parti["nick"] = nick
        parti["instagram"] = instragram
        db.collection("particips")

            .add(parti)
            .addOnSuccessListener(OnSuccessListener<DocumentReference> { documentReference ->
                Log.d(MainActivity.TAG,"DocumentSnapshot added with ID: " + documentReference.id)
            })
            .addOnFailureListener(OnFailureListener { e ->
                Log.w(MainActivity.TAG,"Error adding document", e)
            })
    }

    private  fun action(){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun verifyEmail(user:FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){
                task ->

                if (task.isComplete){
                    Toast.makeText(this, "Confirme su cuenta en el enlace del correo enviado", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this, "Error al enviar correo", Toast.LENGTH_LONG).show()
                }


            }
    }
}


