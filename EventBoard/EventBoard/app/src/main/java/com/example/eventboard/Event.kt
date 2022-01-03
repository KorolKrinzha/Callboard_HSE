import android.os.Parcelable
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

@Parcelize
class Event(
    val id: String, val tittle: String, val datetime: String, val place: String,
    val description: String, val creator: String
) : Parcelable {
    // Parcelable это интерфейс, как Serializable, но круче (как я понял)
    // А Serializable это для сериализации данных
    // Нужно для передачи целого оъекта класса из одной активности в другую
    constructor() : this("", "", "", "", "", "")


    // Участие в событии
    fun performAgree() {
        val db = FirebaseFirestore.getInstance()
        val event_participants: MutableMap<String, Any> = HashMap()
        val uid = FirebaseAuth.getInstance().uid.toString()


        // event_participants устроен так, что состоит из документа с одним полем participants
        // Это array со всеми id участников

        db.collection("event_participants")
            .document(id).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            // добавление нового участника
                            db.collection("event_participants")
                                .document(id)
                                .update(
                                    "participants", FieldValue.arrayUnion
                                        (uid)
                                )
                                .addOnSuccessListener {


                                }.addOnFailureListener {


                                }
                        } else {
                            // Если событие еще не числится в event_participants,
                            // мы просто создаем новый документ с id участника
                            Log.d("KEK", "No docs")
                            val participants = hashMapOf(

                                "participants" to arrayListOf(uid)
                            )

                            db.collection("event_participants").document(id).set(participants)

                        }
                    }

                }

            }


    }

    // во время редактирования: обновление документа
    fun performUpdate() {
        val db = FirebaseFirestore.getInstance()
        db.collection("events").document(id)
            .update(
                mapOf(
                    "tittle" to tittle,
                    "place" to place,
                    "datetime" to datetime,
                    "description" to description,

                    )
            ).addOnSuccessListener { }

    }

    // В коллеции под именем id события находим id пользователя -->
//  удаляем из списка участников
    fun performDisAgree() {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().uid.toString()
        db.collection("event_participants").document(id)
            .update("participants", FieldValue.arrayRemove(uid))

    }

    // удаление события во время редактирования
// удаляем в двух коллекциях - в самом событии и участниках
// (логично, нельзя участвовать в удаленном событии)
    fun performDelete() {
        val db = FirebaseFirestore.getInstance()

        db.collection("event_participants")
            .document(id)
            .delete()
            .addOnSuccessListener { }
            .addOnFailureListener { e -> Log.w("KEK", "Error deleting document", e) }


        db.collection("events")
            .document(id)
            .delete()
            .addOnSuccessListener { }
            .addOnFailureListener { e -> Log.w("KEK", "Error deleting document", e) }

    }


    // Метод, необходимый для валидации даты
// При true мы добавляем событие в ленту главных и любимых
// Те, у которых False, старенькие, никому не нужны:(
    fun checkDate(): Boolean {

        // Calendar.getInstance().time.toString() -->
        // (пример) Sat Nov 06 22:06:51 GMT+03:00 2021
        // преобразуем в норм строку, чтобы кусками из этого сделать объект класа Date
        val currentDateString = SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().time)
        val currentDate: Date = Date(
            currentDateString.slice(0..3).toInt(),
            currentDateString.slice(5..6).toInt(),
            currentDateString.slice(8..9).toInt()
        )
        // во всех случаях настроек даты я сделал так, чтобы она была одного формата
        // эта проверка - всего лишь подстраховка, чтобы не получить ошибку во время работы
        if (datetime.length == 10) {
            val datetime_Date: Date = Date(
                datetime.slice(6..9).toInt(),
                datetime.slice(3..4).toInt(),
                datetime.slice(0..1).toInt()
            )
            // Слайсим строку и создаем объект класса Date чтобы использовать метод

            // 1 - дата раньше нынешней
            if (currentDate.compareTo(datetime_Date) != 1) {
                return true
            }


        }
        return false
    }


    // Для удобного просмотра KEK-логов
    override fun toString(): String {
        return ("${id}: Tittle is ${tittle}, Datetime is ${datetime}," +
                " Place is ${place}")
    }


}