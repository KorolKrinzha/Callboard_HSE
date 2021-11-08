package com.example.eventboard.viewitems

import Event
import android.util.Log
import com.example.eventboard.R
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.single_event.view.*

class SingleEventItem(val event: Event): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        addcreator(viewHolder)
        viewHolder.itemView.single_event_text_tittle.text = event.tittle
        viewHolder.itemView.single_event_text_place.text = event.place
        viewHolder.itemView.single_event_text_datetime.text = event.datetime
        viewHolder.itemView.single_event_text_description.text = event.description





    }




    override fun getLayout(): Int {
        return R.layout.single_event
    }

    private fun addcreator(viewHolder: ViewHolder) {
        val db = FirebaseFirestore.getInstance()
        val ref = event.creator
        db.collection("users").whereEqualTo("uid", ref)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val creator = document.data.get("username")
                    viewHolder.itemView.single_event_text_creator.text =  creator.toString()
                }


            }
            .addOnFailureListener { exception ->
                Log.d("KEK", "Error getting documents: ", exception)
                viewHolder.itemView.single_event_text_creator.text =  "Error"

            }
    }








}