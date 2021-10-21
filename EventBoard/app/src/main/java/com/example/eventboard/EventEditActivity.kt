package com.example.eventboard

import Event
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.eventboard.bottom_nav_activities.MyEventsActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_event_edit.*
import java.text.SimpleDateFormat
import java.util.*

class EventEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)


        val event_id: String?
        event_id = if (savedInstanceState == null) {
            val extras = intent.extras
            extras?.getString("EVENT_ID_EDIT")
        } else {
            savedInstanceState.getSerializable("EVENT_ID_EDIT") as String?
        }





        getData(event_id.toString())
    }

    private fun getData(event_id:String) {



        val place_spinner = edit_event_place_spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.places,
            android.R.layout.simple_spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            place_spinner.adapter = adapter
        }


        val places = mapOf("Ляля" to 0, "БХ" to 1, "Солянка" to 2, "Колобок" to 3)


        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("events")
        val ex = ref.document(event_id)

        ex.get()
            .addOnSuccessListener { document ->
                if (document != null) {


                    val editevent = Event(
                        document.id.toString(),
                        document.data?.get("tittle").toString(),
                        document.data?.get("datetime").toString(),
                        document.data?.get("place").toString(),
                        document.data?.get("description").toString(),
                        document.data?.get("creator").toString())


                    edit_event_tittle_text.setText(editevent.tittle)
                    edit_event_description_text.setText(editevent.description)
                    val set_places: Int? = places[editevent.place]

                    if (set_places != null) {
                        place_spinner.setSelection(set_places.toInt())
                    }
                    supportActionBar?.title =
                        "Редактирование события ${editevent.tittle}"


                    edit_event_delete_button.setOnClickListener {

                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Удаление события")
                        builder.setMessage("Вы точно уверены," +
                                " что хотите навсегда удалить событие?")


                        builder.setPositiveButton(R.string.OK) { dialog, which ->

                            editevent?.performDelete()
                            val intent = Intent(this, MyEventsActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                        builder.setNegativeButton(R.string.Cancel) { dialog, which ->

                        }
                        builder.show()


                    }

                    edit_event_change_button.setOnClickListener {

                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Редактирование события")
                        builder.setMessage("Вы точно уверены," +
                                " что хотите изменить параметры события?")


                        builder.setPositiveButton(R.string.OK) { dialog, which ->

                            val new_tittle = edit_event_tittle_text.text.toString()
                            val new_description = edit_event_description_text.text.toString()
                            val new_place = place_spinner.selectedItem.toString()
                            val new_datetime = Date(edit_event_datetime_datepicker.year,
                                edit_event_datetime_datepicker.month+1,
                                edit_event_datetime_datepicker.dayOfMonth)

                            if (new_tittle.isEmpty() || new_description.isEmpty()
                                || new_place.toString().isEmpty() || new_datetime.toString().isEmpty()) {
                                Toast.makeText(this,
                                    "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                                return@setPositiveButton
                            }


                            val currentDateString = SimpleDateFormat("yyyy.MM.dd").
                            format( Calendar.getInstance().time)
                            val currentDate:Date = Date(
                                currentDateString.slice(0..3).toInt(),
                                currentDateString.slice(5..6).toInt(),
                                currentDateString.slice(8..9).toInt()
                            )
                            if (currentDate.compareTo(new_datetime)==1){
                                Toast.makeText(this,
                                    "Пожалуйста, укажите правильную дату", Toast.LENGTH_SHORT).show()
                                return@setPositiveButton
                            }



                            val new_datetime_string =
                                "${edit_event_datetime_datepicker.dayOfMonth}." +
                                "${edit_event_datetime_datepicker.month+1}." +
                                "${edit_event_datetime_datepicker.year}"
                            val newevent = Event(editevent.id,
                                new_tittle,
                                new_datetime_string,
                                new_place,
                                new_description,
                                editevent.creator
                            )
                            newevent.performUpdate()
                            finish()
                        }

                        builder.setNegativeButton(R.string.Cancel) { dialog, which ->

                        }
                        builder.show()


                    }



                } else {
                    edit_event_tittle_text.setText("Ошибка")
                    edit_event_description_text.setText("Ошибка")

                    supportActionBar?.title = "Ошибка"
                }
            }
    }

    }

