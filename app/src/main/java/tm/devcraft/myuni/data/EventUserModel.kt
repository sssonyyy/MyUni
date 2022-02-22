package tm.devcraft.myuni.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class EventUserModel(var statusPublicate: Boolean = false,
                          var id: String = "",
                          var type: String = "",
                          var name: String = "",
                          var place: String = "",
                          var dateFrom : Date? = null,
                          var dateTo: Date? = null,
                          var additionalOrg: String = "",
                          var description: String = "",
                          var link: String = "",
                          @ServerTimestamp var created: Timestamp = Timestamp.now(),
                          var uidCreator: String = "",
                          var tags: MutableList<String> = mutableListOf<String>(),
                          var photos: HashMap<String, String> = hashMapOf(),
                          var views: Int = 0,
                          var reposted: Int = 0,
                          var saved: Int = 0,
                          var reported: Int = 0,
                          var request: Int = 0,
                          var dLink: String = ""
)
