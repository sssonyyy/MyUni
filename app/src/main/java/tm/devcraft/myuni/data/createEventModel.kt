package tm.devcraft.myuni.data

import com.google.firebase.firestore.FieldValue
import java.util.*

data class createEventModel(var statusPublicate: Boolean = false,
                            var name: String = "",
                            var id: String = "",
                            var type: String = "",
                            var place: String = "",
                            var dateFrom : Date? = null,
                            var dateTo: Date? = null,
                            var additionalOrg: String = "",
                            var description: String = "",
                            var Link: String = "",
                            var created: FieldValue? = null,
                            var uidCreator: String = "",
                            var tags: MutableList<String> = mutableListOf<String>(),
                            var Photos: HashMap<String,String> = hashMapOf(),
                            var views: Int = 0,
                            var saved: Int = 0,
                            var reposted: Int = 0,
                            var request: Int = 0,
                            var dLink: String = ""
)
