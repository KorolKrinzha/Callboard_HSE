package com.example.eventboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()


        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, введите необходимые данные", Toast.LENGTH_SHORT).show()
            return
        }
        
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                val intent = Intent(this, LatestEventsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)




            }
            .addOnFailureListener {
                Toast.makeText(this, "Ошибка при регистрации. Пожалуйста, попробуйте еще раз", Toast.LENGTH_SHORT).show()
            }
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_button_login.setOnClickListener {
            performLogin()
        }


        back_to_register.setOnClickListener {
            finish()
        }

    }

}

