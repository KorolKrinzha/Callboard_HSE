package com.example.eventboard.showitems
import Event
import android.util.Log
import com.example.eventboard.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.event_row_new.view.*
import android.view.MotionEvent
import android.view.View
import com.google.firebase.firestore.FirebaseFirestore


class EventItem(val event: Event): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tittle_event_mainboard.text = event.tittle
        viewHolder.itemView.details_event_mainboard.text = "${event.place}, ${event.datetime}"
        checkSpecial(viewHolder)
        checkLength(viewHolder)









    }



    override fun getLayout(): Int {
        return R.layout.event_row_new
    }


    private fun checkLength(viewHolder: ViewHolder){
        if (event.tittle.length>21){
            val tittle_event_shorten = event.tittle.slice(0..20)+"..."
            viewHolder.itemView.tittle_event_mainboard.text = tittle_event_shorten
        }
    }




    private fun checkSpecial(viewHolder: ViewHolder) {
        val db = FirebaseFirestore.getInstance()
        db.collection("events").document(event.id).
        get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val recommended = task.result?.data?.get("recommended")

                if (recommended==true) {

                    viewHolder.itemView.event_layout.
                    setBackgroundResource(R.drawable.event_special_background)
                }

            }
        }
    }


}