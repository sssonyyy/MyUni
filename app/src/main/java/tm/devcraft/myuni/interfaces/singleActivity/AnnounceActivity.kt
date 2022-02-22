package tm.devcraft.myuni.interfaces.singleActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_announce.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.AnnounceUserModel
import tm.devcraft.myuni.data.userData
import tm.devcraft.myuni.interfaces.fragments.create.createActivity.AdapterAnnounceSlider
import tm.devcraft.myuni.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class AnnounceActivity : AppCompatActivity() {
    private var modelAnn = Paper.book().read<AnnounceUserModel>("announceSelect")
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announce)

        var link = Firebase.dynamicLinks.getDynamicLink(intent)

        if(link.isSuccessful){
            var deepLink: Uri? = link.result.link
            //println(deepLink?.path)
            Firebase.firestore.collection("announces").whereEqualTo("id",deepLink?.getQueryParameter("id").toString()).get().addOnSuccessListener {
                    docs ->
                for (doc in docs){
                    val model = doc.toObject<AnnounceUserModel>()
                    Firebase.firestore.collection("users").document(model.uidCreator).get().addOnSuccessListener {
                        docU ->
                        val user = docU.toObject<userData>()
                        val format = SimpleDateFormat("dd/MM/yyyy")
                        if(model.price != 0){
                            price.text = model.price.toString() + " руб"
                        }
                        else{
                            price.visibility = View.GONE
                            label_price.visibility = View.GONE
                        }
                        description.text = model.description
                        name_skill.text = model.skill
                        name_age.text = user?.fullname + ", " + user?.date?.let { Date(it.seconds*1000 + it.nanoseconds) }
                            ?.let { Utils.getAge(it) } + " лет"
                        vuz.text = user?.nameOfInstitution + " " + user?.course + " курс"
                        place.text = model.vuz
                        //price.text = model.price.toString()
                        description.text = model.description
                        var adapter = AdapterAnnounceSlider()
                        model.Photos.forEach { s, s2 ->
                            adapter.addItem(Uri.parse(s2))
                        }
                        imageSlider.setSliderAdapter(adapter)
                        share.setOnClickListener {
                            var intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT,model.dLink)
                            startActivity(Intent.createChooser(intent,"Share Link"))
                            doc.reference.update("reposted",model.reposted+1)
                        }
                        save.setOnClickListener {
                            doc.reference.update("saved",model.reposted+1)
                            Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).update("savedEvents",
                                FieldValue.arrayUnion(model.id))
                        }
                    }


                }
            }
        }
        if (!link.isSuccessful){
                    Firebase.firestore.collection("users").document(modelAnn.uidCreator).get().addOnSuccessListener {
                            doc ->
                        val user = doc.toObject<userData>()
                        val format = SimpleDateFormat("dd/MM/yyyy")
                        if(modelAnn.price != 0){
                            price.text = modelAnn.price.toString() + " руб"
                        }
                        else{
                            price.visibility = View.GONE
                            label_price.visibility = View.GONE
                        }
                        description.text = modelAnn.description
                        name_skill.text = modelAnn.skill
                        name_age.text = user?.fullname + ", " + user?.date?.let { Date(it.seconds*1000 + it.nanoseconds) }
                            ?.let { Utils.getAge(it) } + " лет"
                        vuz.text = user?.nameOfInstitution + " " + user?.course + " курс"
                        place.text = modelAnn.vuz
                        //price.text = modelAnn.price.toString()
                        description.text = modelAnn.description
                        var adapter = AdapterAnnounceSlider()
                        modelAnn.Photos.forEach { s, s2 ->
                            adapter.addItem(Uri.parse(s2))
                        }
                        imageSlider.setSliderAdapter(adapter)
                        share.setOnClickListener {
                            var intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_TEXT,modelAnn.dLink)
                            startActivity(Intent.createChooser(intent,"Share Link"))
                            Firebase.firestore.collection("announces")
                                .whereEqualTo("id", modelAnn.id).get()
                                .addOnSuccessListener { docs ->
                                    for (doc in docs) {
                                        doc.reference.update("reposted", modelAnn.reposted + 1)
                                    }
                                }
                        }
                        save.setOnClickListener {

                            Firebase.firestore.collection("announces")
                                .whereEqualTo("id", modelAnn.id).get()
                                .addOnSuccessListener { docs ->
                                    for (doc in docs) {
                                        doc.reference.update("saved", modelAnn.saved + 1)
                                    }
                                }
                            Firebase.firestore.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).update("savedEvents",
                                FieldValue.arrayUnion(modelAnn.id))
                        }
                    }




        }

        btn_back.setOnClickListener {
            finish()
        }

    }
}