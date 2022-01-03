package com.example.eventboard.viewitems
import User
import com.example.eventboard.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.participant_row_new.view.*

class ParticipantItem(val participant: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.fullname_text.text = participant.fullname
        viewHolder.itemView.username_text.text = participant.username



    }

    override fun getLayout(): Int {
        return R.layout.participant_row_new
    }
}