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
import kotlinx.android.synthetic.main.new_event.*
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





        val db = FirebaseFirestore.getInstance()
        db.collection("events")
        .document(event_id)

        .get()
            .addOnSuccessListener { document ->
                if (document != null) {


                    val editevent = Event(
                        document.id,
                        document.data?.get("tittle").toString(),
                        document.data?.get("datetime").toString(),
                        document.data?.get("place").toString(),
                        document.data?.get("description").toString(),
                        document.data?.get("creator").toString())


                    edit_event_tittle_text.setText(editevent.tittle)
                    edit_event_description_text.setText(editevent.description)


                    // пользователю сразу выбирается то место, которое он указал ранее
                    val places = mapOf("Ляля" to 0, "БХ" to 1, "Солянка" to 2, "Колобок" to 3)
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
                            // вызов метода performDelete класса Event, а затем возврат на "Ваши ивенты"
                            editevent.performDelete()
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
                            var new_datetime: Date = Date(edit_event_datetime_datepicker.year,
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
                            // верификация даты
                            if (currentDate.compareTo(new_datetime)==1){
                                Toast.makeText(this,
                                    "Пожалуйста, укажите правильную дату", Toast.LENGTH_SHORT).show()
                                return@setPositiveButton
                            }


                            new_datetime = Date(edit_event_datetime_datepicker.year,
                                edit_event_datetime_datepicker.month,
                                edit_event_datetime_datepicker.dayOfMonth)
                            // необходимо для длины строк равноц 10 симв.
                            // иначе 6.11.2021 или 11.6.2021 и тд
                            // см. checkDate в Event
                            val new_datetime_string =
                                SimpleDateFormat("dd.MM").
                                format(new_datetime)+"."+"${edit_event_datetime_datepicker.year}"

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

