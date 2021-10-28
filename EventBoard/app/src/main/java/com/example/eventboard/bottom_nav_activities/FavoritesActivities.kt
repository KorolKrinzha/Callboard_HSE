package com.example.eventboard.bottom_nav_activities

import Event
import com.example.eventboard.showitems.EventItem
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.eventboard.EventAgreeActivity
import com.example.eventboard.LatestEventsActivity
import com.example.eventboard.LatestEventsActivity.Companion.EVENT_KEY
import com.example.eventboard.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_event.*

class FavoritesActivities : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        fetchFavoriteEvents()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_event)
        supportActionBar?.title = "Понравившееся события"


        fetchFavoriteEvents()

        bottom_nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, LatestEventsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_favourites -> {}
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







    private fun fetchFavoriteEvents() {



        val adapter = GroupAdapter<ViewHolder>()
        val uid = FirebaseAuth.getInstance().uid

        val db: FirebaseFirestore? = FirebaseFirestore.getInstance()
        if (db != null) {
            db.collection("event_participants")
                .whereArrayContains("participants", uid.toString())
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                    if (task.isSuccessful) {
                        val events = mutableListOf<String>()
                        for (document in task.result!!) {



                            events.add(document.id)
                        }
                        
                        fetchFoundEvents(events)







                    }
                    else{

                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                })
        }
        else{

            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }

        recyclerview_newev.adapter = adapter

    }

    private fun fetchFoundEvents(events_id:MutableList<String>) {
        val adapter = GroupAdapter<ViewHolder>()
        val db: FirebaseFirestore? = FirebaseFirestore.getInstance()
        if (db != null) {
            for (event in events_id) {

                db.collection("events").document(event)
                    .get()
                    .addOnSuccessListener {document ->
                        if (document != null) {
                            
                            val event = Event(
                                document.id,
                                document.data?.get("tittle").toString(),
                                document.data?.get("datetime").toString(),
                                document.data?.get("place").toString(),
                                document.data?.get("description").toString(),
                                document.data?.get("creator").toString())
                            if (event.checkDate()) {
                                adapter.add(EventItem(event))
                                adapter.setOnItemClickListener { item, view ->
                                    val eventItem = item as EventItem

                                    val intent = Intent(this, EventAgreeActivity::class.java)
                                    intent.putExtra(EVENT_KEY, eventItem.event)

                                    startActivity(intent)
                                }
                            }


                        } else {
                            Log.d("KEK", "No such document")
                        }


                    }

            }
        }
        recyclerview_newev.adapter = adapter
    }

}
