import android.os.Parcelable
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.parcel.Parcelize

@Parcelize
class Event(val id:String, val tittle:String, val datetime:String, val place:String, val description:String, val creator:String ):Parcelable {

    constructor(): this( "", "","", "", "", "")




    fun performAgree() {
        val db = FirebaseFirestore.getInstance()
        val event_participants:MutableMap<String, Any> = HashMap()
        val uid = FirebaseAuth.getInstance().uid
        event_participants["uid"] = uid.toString()
        event_participants["event_id"] = id


        db.collection("event_participants")
            .document(id).get().addOnCompleteListener { task->
                if (task.isSuccessful){
                    val document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            db.collection("event_participants")
                                .document(id)
                                .update("participants", FieldValue.arrayUnion
                                    (event_participants["uid"]))
                                .addOnSuccessListener {
                                    

                                }.addOnFailureListener {
                                    



                                }
                        }
                        else{
                            Log.d("KEK", "No docs")
                            val participants = hashMapOf(

                                "participants" to arrayListOf(event_participants["uid"]))

                            db.collection("event_participants").
                            document(id).set(participants)
                            
                        }
                    }

                }

            }


    }

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


    fun performDisAgree() {
        val db = FirebaseFirestore.getInstance()
        val uid = FirebaseAuth.getInstance().uid.toString()
        db.collection("event_participants").document(id)
            .update("participants", FieldValue.arrayRemove(uid))

    }


    fun performDelete(){
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






    override fun toString(): String {
        return ("${id}: Tittle is ${tittle}, Datetime is ${datetime}," +
                " Place is ${place}")
    }




}