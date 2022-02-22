package tm.devcraft.myuni.data

import com.google.firebase.Timestamp

data class userData(
    var email: String = "",
    var uid: String = "",
    var fullname: String = "",
    var sex: String = "",
    var date: Timestamp? = null,
    var about: String = "",
    var course: Int = 0,
    var dLink: String = "",
    var faculty: String = "",
    var interests: String = "",
    var skills: String = "",
    var speciality: String = "",
    var nameOfInstitution: String = "",
    var photo: HashMap<String,String> = hashMapOf(),
    var views: Int = 0,
    var saved: Int = 0,
    var reposted: Int = 0,
    var request: Int = 0,
    var savedAnn: ArrayList<String> = arrayListOf(),
    var savedEvents: ArrayList<String> = arrayListOf(),
    var savedPeople: ArrayList<String> = arrayListOf()
    )
