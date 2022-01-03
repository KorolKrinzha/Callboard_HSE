package com.example.eventboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_register.*
import androidx.appcompat.app.AppCompatDelegate


class RegisterActivity : AppCompatActivity() {


    private fun UserRegister(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        val username = username_edittext_register.text.toString()
        val fullname = fullname_edittext_register.text.toString()
        // Проверка на заполнение всех полей
        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || fullname.isEmpty()) {
            Toast.makeText(this,
                "Пожалуйста, введите необходимые данные",
                Toast.LENGTH_SHORT).show()
            return
        }

        // создание нового пользователя
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                saveUserToFirebaseDatabase(username, fullname, email, it.result?.user?.uid.toString())


            }.addOnFailureListener{

                Toast.makeText(this, "Ошибка при регистрации. " +
                        "Пожалуйста, попробуйте еще раз", Toast.LENGTH_SHORT).show()

            }

    }

// Сохраняем пользователя в бд, коллекция users
    private fun saveUserToFirebaseDatabase(username:String, fullname:String,
                                           email:String, uid:String) {


        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["username"] = username
        user["fullname"] = fullname
        user["uid"] = uid
        user["email"] = email



        db.collection("users")
            .add(user)
            .addOnSuccessListener {

                val intent = Intent(this, LatestEventsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)



            }
            .addOnFailureListener{
                Log.d("KEK", "Error adding document") }






    }






    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyApplication)
        // минус ночной режим
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        register_button_register.setOnClickListener {
            UserRegister()




        }






        already_have_account_text_view.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }



    }



}

