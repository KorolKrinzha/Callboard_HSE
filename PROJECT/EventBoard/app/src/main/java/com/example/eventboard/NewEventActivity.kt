package com.example.eventboard

import Event
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.new_event.*
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*


class NewEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_event)
        supportActionBar?.title = "Создать событие"
        val place_spinner = new_event_place
        ArrayAdapter.createFromResource(
            this,
            R.array.places,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            place_spinner.adapter = adapter
        }






        new_event_button_create.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Создание события")
            builder.setMessage("Вы уверены, что хотите создать событие?")


            builder.setPositiveButton(R.string.OK) { dialog, which ->

                perfromNewEvent()
            }

            builder.setNegativeButton(R.string.Cancel) { dialog, which ->

            }



            builder.show()


        }




    }

    private fun perfromNewEvent() {
        val tittle = new_event_tittle.text.toString()
        val description = new_event_description.text.toString()
        var datetime = Date(new_event_datetime.year,
            new_event_datetime.month+1,
            new_event_datetime.dayOfMonth)



        val currentDateString = SimpleDateFormat("yyyy.MM.dd").
        format( Calendar.getInstance().time)
        val currentDate:Date = Date(
            currentDateString.slice(0..3).toInt(),
            currentDateString.slice(5..6).toInt(),
            currentDateString.slice(8..9).toInt()
        )






        val place = new_event_place.selectedItem.toString()
        val creator: String = FirebaseAuth.getInstance().uid.toString()
        if (tittle.isEmpty() || description.isEmpty() || datetime.toString().isEmpty() || place.isEmpty()) {
                Toast.makeText(this,
                    "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return
            }

        if (currentDate.compareTo(datetime)==1){
            Toast.makeText(this,
                "Пожалуйста, укажите правильную дату", Toast.LENGTH_SHORT).show()
            return
        }



        else{

            val db: FirebaseFirestore? = FirebaseFirestore.getInstance()
            val event: MutableMap<String, Any> = HashMap()
            datetime = Date(new_event_datetime.year,
                new_event_datetime.month,
                new_event_datetime.dayOfMonth)


            event["tittle"] = tittle
            event["place"] = place
            event["datetime"] =
                    SimpleDateFormat("dd.MM").format(datetime)+"."+"${new_event_datetime.year}"
            event["description"] = description
            event["creator"] = creator



            db!!.collection("events").add(event).addOnSuccessListener { documentReference ->
                    val newevent = Event(documentReference.id.toString(),
                        event["tittle"].toString(),
                    event["datetime"].toString(),
                    event["place"].toString(),
                    event["description"].toString(),
                        event["creator"].toString())
                newevent.performAgree()
                
            }


            val intent = Intent(this, LatestEventsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

    }
}
