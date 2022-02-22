package tm.devcraft.myuni.data

import android.net.Uri
import java.util.*

data class RegUserModel(val email: String, val password: String,  var aboutUser: NextStepRegModel? = null, var uri: Uri = Uri.EMPTY){
    data class NextStepRegModel(
        var photo: HashMap<String, String> = hashMapOf<String,String>(),
        var fullname: String = "",
        var date: Date? = null,
        var sex: String = "",
        var nameOfInstitution: String = "",
        var faculty: String = "",
        var course: Int = 1,
        var speciality: String = "",
        var skills: String = "",
        var interests:String = "",
        var about: String = "",
        var isBanned: Boolean = false,
        var levelSubs: Int = 0,
        var uid : String = "",
        var savedAnn: ArrayList<String> = arrayListOf(),
        var savedEvents: ArrayList<String> = arrayListOf(),
        var savedPeople: ArrayList<String> = arrayListOf(),
        var listUri : ArrayList<Uri> = arrayListOf()
        )
}
