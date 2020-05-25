package net.azarquiel.tornite.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import net.azarquiel.tornite.R

/**
 * Created by diego10mf on 25/05/20.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        txtUser=findViewById(R.id.txtUser)
        txtPassword=findViewById(R.id.txtPassword)

        progressBar= findViewById(R.id.progressBar)
        auth=FirebaseAuth.getInstance()
    }
    fun forgotPassword(view:View) {
        startActivity(Intent(this, ForgotPassActivity::class.java))
    }
    fun register(view:View) {
    startActivity(Intent(this, RegisterActivity::class.java))
    }
    fun login(view:View) {
        loginUser()

    }
    private fun loginUser(){
        val user:String= txtUser.text.toString()
        val password:String= txtPassword.text.toString()

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password) ){
            progressBar.visibility=View.VISIBLE
            auth.signInWithEmailAndPassword(user, password)
                .addOnCompleteListener(this){
                    task ->
                    if (task.isSuccessful){
                        action()
                    }else{
                        Toast.makeText(this, "Error al iniciar sesión. Asegurese de poner bien el correo o la contraseña", Toast.LENGTH_LONG).show()
                        progressBar.visibility=View.INVISIBLE
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
        }else{
            Toast.makeText(this, "Rellene los campos", Toast.LENGTH_LONG).show()
        }
    }
    private fun action(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}
