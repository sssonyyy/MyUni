package tm.devcraft.myuni.interfaces.singleActivity

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_event_pub.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.EventUserModel
import tm.devcraft.myuni.interfaces.fragments.create.createActivity.AdapterAnnounceSlider
import java.text.SimpleDateFormat

class EventActivity : AppCompatActivity(R.layout.activity_event) {
    private var model = Paper.book().read<EventUserModel>("popularEvent")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var link = Firebase.dynamicLinks.getDynamicLink(intent)

        if(link.isSuccessful){
            var deepLink: Uri? = link.result.link
            //println(deepLink?.path)
            Firebase.firestore.collection("events").whereEqualTo("id",deepLink?.getQueryParameter("id").toString()).get().addOnSuccessListener {
                    docs ->
                for (doc in docs){
                    val user = doc.toObject<EventUserModel>()
                    val format = SimpleDateFormat("dd/MM/yyyy")
                    type.text = user.type
                    name.text = user.name
                    place.text = user.place
                    if (user.additionalOrg != ""){
                        name_org.text = user.additionalOrg
                    }
                    else{
                        org.visibility = View.GONE
                    }
                    description.text = user.description
                    if(user.dateTo != null ){
                        date.text = format.format(user.dateFrom).toString().replace("/",".") + "-" + format.format(user.dateTo).toString().replace("/",".")
                    }
                    else{
                        date.text = format.format(user.dateFrom).toString().replace("/",".")
                    }

                    var adapter = AdapterAnnounceSlider()
                    user.photos.forEach { s, s2 ->
                        adapter.addItem(Uri.parse(s2))
                    }
                    imageSlider.setSliderAdapter(adapter)

                }
            }
        }
        if (!link.isSuccessful){
            val format = SimpleDateFormat("dd/MM/yyyy")
            type.text = model.type
            name.text = model.name
            place.text = model.place
            if (model.additionalOrg != ""){
                name_org.text = model.additionalOrg
            }
            else{
                org.visibility = View.GONE
            }
            if(model.dateTo != null ){
                date.text = format.format(model.dateFrom).toString().replace("/",".") + "-" + format.format(model.dateTo).toString().replace("/",".")
            }
            else{
                date.text = format.format(model.dateFrom).toString().replace("/",".")
            }
            description.text = model.description
            var adapter = AdapterAnnounceSlider()
            model.photos.forEach { s, s2 ->
                adapter.addItem(Uri.parse(s2))
            }
            imageSlider.setSliderAdapter(adapter)

        }

        btn_back.setOnClickListener {
            finish()
        }
    }
}