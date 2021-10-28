package com.example.eventboard

import Event
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.eventboard.bottom_nav_activities.FavoritesActivities
import com.example.eventboard.bottom_nav_activities.MyEventsActivity
import com.example.eventboard.bottom_nav_activities.RecommendedActivity
import com.example.eventboard.showitems.EventItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_event.*

class LatestEventsActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_new_event -> {
                val intent = Intent(this, NewEventActivity::class.java)
                startActivity(intent)
            }
            R.id.mmenu_signout -> {
                FirebaseAuth.getInstance().signOut()
                verifyLogIn()
            }
            R.id.menu_account -> {
                 val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }







    override fun onResume() {
        super.onResume()
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

        
        fetchEvents()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyApplication)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Доска ивентов"
        setContentView(R.layout.activity_latest_event)


        verifyLogIn()
        fetchEvents()



        bottom_nav.setOnItemSelectedListener {


            when (it.itemId) {
                R.id.nav_home -> {
                }
                R.id.nav_favourites -> {
                    val intent = Intent(this, FavoritesActivities::class.java)
                    startActivity(intent)

                }
                R.id.nav_recommended -> {
                    val intent = Intent(this, RecommendedActivity::class.java)
                    startActivity(intent)
                }

                R.id.nav_actions-> {
                    val intent = Intent(this, MyEventsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
            }







    }
    companion object{

        val EVENT_KEY = "EVENT_KEY"

    }


    private fun verifyLogIn(){
        val uid = FirebaseAuth.getInstance().uid
        if (uid==null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    private fun fetchEvents(){
        val adapter = GroupAdapter<ViewHolder>()

        val db  = FirebaseFirestore.getInstance()
        db.collection("events")
            .orderBy("datetime", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener({ task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {


                        val event = Event(
                            document.id,
                            document.data.get("tittle").toString(),
                            document.data.get("datetime").toString(),
                            document.data.get("place").toString(),
                            document.data.get("description").toString(),
                            document.data.get("creator").toString())
                        if(event.checkDate()) {
                            adapter.add(EventItem(event))



                            adapter.setOnItemClickListener { item, view ->
                                val eventItem = item as EventItem

                                val intent = Intent(this, EventAgreeActivity::class.java)
                                intent.putExtra(EVENT_KEY, eventItem.event)

                                startActivity(intent)
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            })

        recyclerview_newev.adapter = adapter
    }




}






