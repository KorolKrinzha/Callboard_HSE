package com.example.eventboard.showitems
import Event
import android.util.Log
import com.example.eventboard.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.event_row_new.view.*
import android.view.MotionEvent
import android.view.View


class EventItem(val event: Event): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tittle_event_mainboard.text = event.tittle
        viewHolder.itemView.details_event_mainboard.text = "${event.place}      ${event.datetime}"




    }

    override fun getLayout(): Int {
        return R.layout.event_row_new
    }
}