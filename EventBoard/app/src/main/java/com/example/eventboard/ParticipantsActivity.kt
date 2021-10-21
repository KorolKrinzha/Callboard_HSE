package com.example.eventboard

import User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eventboard.EventAgreeActivity.Companion.EVENT_ID_CREATOR
import com.example.eventboard.showitems.ParticipantItem
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_participants.*

class ParticipantsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participants)
        supportActionBar?.title = "Участники события"
        val event_id: String?
        event_id = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getString(EventAgreeActivity.EVENT_ID)
        } else {
            savedInstanceState.getSerializable("EVENT_ID") as String?
        }

        fetchParticipants(event_id.toString())

    }


    private fun fetchParticipants(id:String) {
        val adapter = GroupAdapter<ViewHolder>()
        val db = FirebaseFirestore.getInstance()
        db.collection("event_participants")
            .document(id).get().addOnSuccessListener { document ->
                if (document != null) {
                    val participants: ArrayList<String> =
                        document.data?.get("participants") as ArrayList<String>
                    for (participant in participants) {
                        db.collection("users")
                            .whereEqualTo("uid", participant)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    for (document in task.result!!) {
                                        val user = User(
                                            document.data.get("uid").toString(),
                                            document.data.get("username").toString(),
                                            document.data.get("fullname").toString()
                                        )

                                        
                                        adapter.add(ParticipantItem(user))

                                        adapter.setOnItemClickListener { item, view ->

                                            val useritem = item as ParticipantItem


                                            val intent = Intent(this, CreatorActivity::class.java)
                                            intent.putExtra(EVENT_ID_CREATOR, useritem.participant.id)

                                            startActivity(intent)
                                        }

                                    }
                                }

                            }
                    }
                }
            }
        recyclerview_participants.adapter = adapter

    }

}