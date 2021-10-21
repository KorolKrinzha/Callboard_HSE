package com.example.eventboard

import Event
import User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.eventboard.showitems.ParticipantItem
import com.example.eventboard.showitems.SingleEventItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_event_agree.*

class EventAgreeActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        val event = intent.getParcelableExtra<Event>(LatestEventsActivity.EVENT_KEY)
        supportActionBar?.title = "${event?.tittle}"
        // TODO update title after editting
        fetchSingleEvent(event?.id.toString())    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_agree)

        val event = intent.getParcelableExtra<Event>(LatestEventsActivity.EVENT_KEY)
        supportActionBar?.title = "${event?.tittle}"
        EVENT_ID_CREATOR = event?.creator.toString()
        EVENT_ID = event?.id.toString()


        fetchSingleEvent(event?.id.toString())


        single_event_button.setBackgroundColor(getResources().getColor(R.color.red))
        single_event_button.setText("(~_~;)")


        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().uid.toString()

        if (event?.creator==uid){
            single_event_button.setBackgroundColor(getResources().getColor(R.color.black))
            single_event_button.setText(R.string.single_event_edit)
            single_event_button.setOnClickListener {

                val intent = Intent(this, EventEditActivity::class.java)
                intent.putExtra("EVENT_ID_EDIT", event?.id)
                startActivity(intent)




                //event?.performDelete()

            }
        }
        else{
            db.collection("event_participants")
                .document(event?.id.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val participants = document.data?.get("participants") as? List<String>
                        if (participants?.contains(uid) == true){

                            single_event_button.setBackgroundColor(getResources().
                            getColor(R.color.red))
                            single_event_button.setText(R.string.single_event_disagree)

                            single_event_button.setOnClickListener {

                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("Отказ от участия")
                                builder.setMessage("Вы уверены, что хотите отказаться от участия в событии?")


                                builder.setPositiveButton(R.string.OK) { dialog, which ->

                                    event?.performDisAgree()
                                    finish()
                                }

                                builder.setNegativeButton(R.string.Cancel) { dialog, which ->

                                }
                                builder.show()




                            }




                        }
                        else{
                            single_event_button.setBackgroundColor(getResources().
                            getColor(R.color.blue_bg_var))
                            single_event_button.setText(R.string.single_event_agree)

                            single_event_button.setOnClickListener {
                                event?.performAgree()
                                finish()
                            }

                        }



                    } else {
                        Log.d("KEK", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("KEK", "get failed with ", exception)
                }

        }










    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_creator, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.nav_creator-> {
                val intent = Intent(this, CreatorActivity::class.java)


                intent.putExtra(EVENT_ID_CREATOR, EVENT_ID_CREATOR)


                startActivity(intent)
            }
            R.id.nav_participants-> {
                val intent = Intent(this, ParticipantsActivity::class.java)

                intent.putExtra(EVENT_ID, EVENT_ID)
                startActivity(intent)

            }


        }
        return super.onOptionsItemSelected(item)
    }




    private fun fetchSingleEvent(id: String) {
        val adapter = GroupAdapter<ViewHolder>()
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("events")
        val ex = ref.document(id)

        ex.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val event = Event(
                        document.id,
                        document.data?.get("tittle").toString(),
                        document.data?.get("datetime").toString(),
                        document.data?.get("place").toString(),
                        document.data?.get("description").toString(),
                        document.data?.get("creator").toString()
                    )

                    if (event != null) {
                        adapter.add(SingleEventItem(event))
                    } else {

                    }


                } else {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }

        recyclerview_chat_log.adapter = adapter

    }





    companion object{

        var EVENT_ID_CREATOR:String = "EVENT_ID_CREATOR"
        var EVENT_ID:String = "EVENT_ID"

    }


}