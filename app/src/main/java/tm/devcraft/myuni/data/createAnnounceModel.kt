package tm.devcraft.myuni.data

import com.google.firebase.firestore.FieldValue

data class createAnnounceModel(var statusPublicate: Boolean = false,
    var id: String = "", var skill: String = "", var vuz: String = "", var price: Int = 0, var Agreement: Boolean = false, var description: String = "", var Link: String = "", var created: FieldValue? = null,
    var uidCreator: String = "", var tags: MutableList<String> = mutableListOf<String>(), var Photos: HashMap<String,String> = hashMapOf(), var dLink: String = "", var views: Int = 0,
                               var saved: Int = 0,
                               var reposted: Int = 0,
                               var request: Int = 0,)
