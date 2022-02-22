package tm.devcraft.myuni.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import io.paperdb.Paper
import tm.devcraft.myuni.data.userData
import tm.devcraft.myuni.utils.FirebaseUtil

class UserVM: ViewModel() {
    var user : MutableLiveData<userData> = MutableLiveData(userData())
    var userLiveData: LiveData<userData> = user
    var db = FirebaseUtil.firebaseFirestore
    var fb = FirebaseUtil.firebaseAuth

    fun loadDataUser(){
        db.collection("users").document(fb.uid.toString()).get().addOnSuccessListener {
                doc ->
            if(doc != null){
                user.value = doc.toObject<userData>()!!
                Paper.book().write("userData",user.value)


                println(Paper.book().read<userData>("userData").toString())
                //initListeners()
            }
            else{
                //initListeners()
            }
        }
    }

    fun testAdd(test: String){
        user.value?.savedPeople?.add(test)
        user.value = user.value
        println(user.value)
    }
}