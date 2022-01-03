package com.example.eventboard.bottom_nav_activities

import Event
import com.example.eventboard.viewitems.EventItem
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.eventboard.EventAgreeActivity
import com.example.eventboard.LatestEventsActivity
import com.example.eventboard.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_event.*

class RecommendedActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        fetchRecommendedEvents()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_event)
        supportActionBar?.title = "Рекомендованное"

        fetchRecommendedEvents()


        bottom_nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, LatestEventsActivity::class.java)
                    startActivity(intent)

                }
                R.id.nav_favourites -> {
                    val intent = Intent(this, FavoritesActivities::class.java)
                    startActivity(intent)

                }
                R.id.nav_recommended -> {

                }

                R.id.nav_actions-> {
                    val intent = Intent(this, MyEventsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }



    }










    private fun fetchRecommendedEvents() {
        val adapter = GroupAdapter<ViewHolder>()

        val db: FirebaseFirestore? = FirebaseFirestore.getInstance()
        if (db != null) {
            db.collection("events")
                .whereEqualTo("recommended", true)
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            


                            val event = Event(
                                document.id.toString(),
                                document.data.get("tittle").toString(),
                                document.data.get("datetime").toString(),
                                document.data.get("place").toString(),
                                document.data.get("description").toString(),
                                document.data.get("creator").toString())
                            adapter.add(EventItem(event))

                            adapter.setOnItemClickListener { item, view ->
                                val eventItem = item as EventItem

                                val intent = Intent(this, EventAgreeActivity::class.java)
                                intent.putExtra(LatestEventsActivity.EVENT_KEY, eventItem.event)

                                startActivity(intent)
                            }
                        }
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

}