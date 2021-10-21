package com.example.eventboard

import Event
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Toast
import com.example.eventboard.showitems.CreatorItem
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_creator.*

class CreatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creator)



        val creator_id: String?
        creator_id = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getString(EventAgreeActivity.EVENT_ID_CREATOR)
        } else {
            savedInstanceState.getSerializable("EVENT_ID_CREATOR") as String?
        }





        fetchCreatorInfo(creator_id.toString())
        fetchCreatorEvents(creator_id.toString())

    }

    private fun fetchCreatorInfo(creator_id: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("uid",creator_id)
            .get()
            .addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        creator_username.setText(document.data.get("fullname").toString())
                        creator_email.setText(document.data.get("email").toString())
                        creator_email.setMovementMethod(
                            LinkMovementMethod.getInstance())
                        supportActionBar?.title = "Пользователь ${document.data.get("username")}"
                    }
                }
                else{
                    creator_username.setText("Ошибка")
                    creator_email.setText("Ошибка")
                    supportActionBar?.title = "Ошибка"
                }
            }


    }


    private fun fetchCreatorEvents(creator_id:String){
        val adapter = GroupAdapter<ViewHolder>()

        val db: FirebaseFirestore? = FirebaseFirestore.getInstance()
        if (db != null) {
            db.collection("events").whereEqualTo("creator", creator_id)
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
                            if (event!=null){ adapter.add(CreatorItem(event))}

                            adapter.setOnItemClickListener { item, view ->
                                val CreatorItem = item as CreatorItem

                                val intent = Intent(this, EventAgreeActivity::class.java)
                                intent.putExtra(LatestEventsActivity.EVENT_KEY, CreatorItem.event)

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

        recyclerview_editor_events.adapter = adapter
    }


}