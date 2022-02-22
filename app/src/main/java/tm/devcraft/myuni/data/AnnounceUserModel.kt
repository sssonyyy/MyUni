package tm.devcraft.myuni.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class AnnounceUserModel(var statuspublicate : Boolean = false,
                             var id: String = "",
                             var skill: String = "",
                             var vuz: String = "",
                             var price: Int = 0,
                             var agreement: Boolean = false,
                             var description: String = "",
                             var link: String = "",
                             @ServerTimestamp var created: Timestamp = Timestamp.now(),
                             var uidCreator: String = "",
                             var tags: MutableList<String> = mutableListOf<String>(),
                             var Photos: HashMap<String,String> = hashMapOf(),
                             var views: Int = 0,
                             var saved: Int = 0,
                             var reposted: Int = 0,
                             var request: Int = 0,
                             var dLink : String = "")
