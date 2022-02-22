package tm.devcraft.myuni.interfaces.fragments.account

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_my_acc.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.interfaces.fragments.create.createActivity.AdapterAnnounceSlider
import tm.devcraft.myuni.interfaces.selectBottomNav.SelectBottomNavActivity
import tm.devcraft.myuni.utils.Utils
import java.util.*

class MyAccFragment : Fragment() {
    private lateinit var db : FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = Firebase.firestore
        return inflater.inflate(R.layout.fragment_my_acc, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        //var user = Paper.book().read<userData>("userData")
        var user = (activity as SelectBottomNavActivity).uvm.user.value
        var listPhoto = arrayListOf<Uri>()
        println(user)
        if (user != null) {
            user.photo.forEach { s, s2 ->
                listPhoto.add(Uri.parse(s2))
            }
        }
        var adapter = AdapterAnnounceSlider()
        if(listPhoto.count() == 1){
            imageSlider.isAutoCycle = false
        }
        adapter.renewItems(listPhoto)
        imageSlider.setSliderAdapter(adapter)
        imageSlider.layoutDirection
        if (user != null) {
            info_about.text = user?.fullname + ", " + user.date?.let { Date(it.seconds*1000 + it.nanoseconds)}
                ?.let { Utils.getAge(it) } + " лет"
        }
        facultet_load.text = user?.faculty
        speciality_load.text = user?.speciality
        educational_institution.text = user?.nameOfInstitution + ", " + user?.course
        content_about.text = user?.about
        content_interests.text = user?.interests
        content_skill.text = user?.skills

        /*db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).get().addOnSuccessListener { document ->
            if (document != null) {
                val user = document.toObject<RegUserModel.NextStepRegModel>()

            } else {

            }
        }
            .addOnFailureListener { exception ->

            }*/

    }
}
