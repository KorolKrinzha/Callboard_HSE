package com.example.eventboard

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eventboard.bottom_nav_activities.FavoritesActivities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
// Не путать с CreatorActivity -  вот это вот профиль самого пользователя,
// где он может менять инфу о себе
class ProfileActivity : AppCompatActivity() {

    companion object{

        var userDocumentId = ""

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.title ="Ваш профиль"

        getData()

        profile_change.setOnClickListener {
            changeData()
        }

        profile_events.setOnClickListener {
            val intent = Intent(this, FavoritesActivities::class.java)
            startActivity(intent)
        }

        profile_contacts.setOnClickListener {

            gotoUrl("https://www.instagram.com/korol_krinzha/")
        }

        profile_github.setOnClickListener {
            gotoUrl("https://github.com/ArtemSmirnovHSE/Callboard_HSE")
        }





    }

    private fun changeData() {

        val profile_fullname = profile_fullname.text.toString()
        val profile_username = profile_username.text.toString()
        if (profile_fullname.isEmpty() || profile_username.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, введите необходимые данные", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        // Обновляем бд новым данными, введенными пользователем
        db.collection("users").document(userDocumentId)

            .update(
                mapOf(
                    "fullname" to profile_fullname,
                    "username" to profile_username
                )
            )

            .addOnSuccessListener { Toast.makeText(this,
                "Параметры успешно обновлены", Toast.LENGTH_LONG).show() }
            .addOnFailureListener { Toast.makeText(this,
                "Ошибка при обновлении параметров, попробуйте еще раз", Toast.LENGTH_LONG).show() }
    }

    // Датой из бд заполняем поля о почте, имени и нике
    private fun getData() {
        val uid = FirebaseAuth.getInstance().uid.toString()
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("uid",uid)
            .get()
            .addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        userDocumentId = document.id


                        profile_email.setText(document.data.get("email").toString())
                        profile_fullname.setText(document.data.get("fullname").toString())
                        profile_username.setText(document.data.get("username").toString())
                    }
                }
        }
    }


    private fun gotoUrl(url:String) {
        // Переход по ссылкам
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}