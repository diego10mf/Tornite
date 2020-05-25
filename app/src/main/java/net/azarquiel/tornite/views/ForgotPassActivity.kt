package net.azarquiel.tornite.views

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import net.azarquiel.tornite.R

/**
 * Created by diego10mf on 25/05/20.
 */
class ForgotPassActivity : AppCompatActivity() {

    private lateinit var txtemail: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        txtemail=findViewById(R.id.txtemail)
        auth=FirebaseAuth.getInstance()
        progressBar= findViewById(R.id.progressBar)
    }
    fun send(view:View){
        val email=txtemail.text.toString()

        if (!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this){
                        task ->
                        if (task.isSuccessful){
                            progressBar.visibility=View.VISIBLE
                            startActivity(Intent(this,LoginActivity::class.java))
                        }else{
                            Toast.makeText(this, "Error al enviar email. Rellene los campos", Toast.LENGTH_LONG).show()
                        }
                    }

        }
    }
}
