package tm.devcraft.myuni.interfaces.fragments.create.createActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.smarteist.autoimageslider.SliderView
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_event_pub.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.createEventModel
import tm.devcraft.myuni.interfaces.selectBottomNav.SelectBottomNavActivity
import java.text.SimpleDateFormat
import java.util.*

class EventPublished : AppCompatActivity(R.layout.activity_event_pub) {

    private val db = Firebase.firestore
    private val modelEvent = Paper.book().read<createEventModel>("createEvent")
    private val REQUEST_CODE_RESULT = 1
    private var listUri: ArrayList<Uri> = arrayListOf()
    private val strg = FirebaseStorage.getInstance()
    private val strgRef = strg.reference.child("events")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initListeners()
    }

    @SuppressLint("SimpleDateFormat")
    private fun initViews(){
        type.text = modelEvent.type
        place.text = modelEvent.place
        if (modelEvent.additionalOrg.isNotEmpty()){
            org.visibility = View.VISIBLE
            name_org.text = modelEvent.additionalOrg
        }
        else{
            org.visibility = View.GONE
        }
        if(modelEvent.dateTo != null){
            val formatter = SimpleDateFormat("dd/M/yyyy")
            date.text = formatter.format(modelEvent.dateFrom?.time).toString().replace("/", ".") + "-" + formatter.format(modelEvent.dateTo?.time).toString().replace("/", ".")
        }
        else{
            val formatter = SimpleDateFormat("dd/M/yyyy")
            date.text = formatter.format(modelEvent.dateFrom?.time).toString().replace("/", ".")
        }

        description.text = modelEvent.description
        name.text = modelEvent.name
    }



    private fun initListeners() {
        btn_back.setOnClickListener {
            finish()
        }
        btn_add_photo.setOnClickListener {
            if(listUri.count() < 4){
                val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
                it.startAnimation(animScale)
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_RESULT)
            }
        }
        btn_delete_photo.setOnClickListener {
            if(listUri.isNotEmpty()){
                listUri.removeAt(imageSlider.currentPagePosition)
                val adapter = AdapterAnnounceSlider()
                adapter.renewItems(listUri)

                imageSlider.isAutoCycle = true
                imageSlider.setSliderAdapter(adapter)
            }
            //println( imageSlider.currentPagePosition)
        }

        publicate.setOnClickListener {

            modelEvent.created = FieldValue.serverTimestamp()
            modelEvent.id = UUID.randomUUID().toString().replace("-","")
            var map = hashMapOf<String,String>()
            listUri.forEach { uri ->
                var uid = UUID.randomUUID().toString().replace("-","")


                strgRef.child(modelEvent.id+"/"+uid+".jpg").putFile(uri).addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { task ->
                        strgRef.child(modelEvent.id+"/"+uid+".jpg").downloadUrl.addOnSuccessListener {
                            println(it.toString())
                            if(listUri.indexOf(uri)==0){
                                map["0"] = it.toString()
                            }
                            else{
                                map[listUri.indexOf(uri).toString()] = it.toString()
                            }
                            if(listUri.indexOf(uri)==listUri.count()-1){
                                if(map.count() < listUri.count()){

                                }
                                //modelEvent.Photos = map
                                //println(modelEvent.Photos.toString())
                                //Paper.book().write("createEvent",modelEvent)
                                //Paper.book().read<createAnnounceModel>("createAnn").created = Calendar.getInstance().time
                                //Paper.book().read<createAnnounceModel>("createAnn").id = UUID.randomUUID().toString()
                                var doc = db.collection("events").document()
                                doc.set(modelEvent)
                                Paper.book().read<createEventModel>("createEvent").tags.forEach {
                                    db.collection("utils").document("tagsEvents").update("tags", FieldValue.arrayUnion(it))
                                }
                                doc.update("photos",map)
                                doc.update("statusPublicate", true)
                                var intent = Intent(this, SelectBottomNavActivity::class.java)
                                val dLink = Firebase.dynamicLinks.createDynamicLink().setLink(
                                    Uri.parse("https://myuni.su/events?id=${modelEvent.id}"))
                                    .setDomainUriPrefix("https://myuni.page.link")
                                    .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                                    .buildDynamicLink()

                                doc.update("dLink",dLink.uri.toString())
                                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
                                startActivity(intent)
                            }
                        }
                    })


                /*strgRef.child(modelAnn.id+"/"+uid+".jpg").putFile(uri).addOnSuccessListener {
                    strgRef.child(modelAnn.id+"/"+uid+".jpg").downloadUrl.addOnSuccessListener {
                        println("!!!!!!!!!!!!!"+it.)

                    }
                }*/
            }

            //println(map.toString())

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            data?.data?.let { listUri.add(it) }
            setImageInSlider(listUri, imageSlider)
            //Paper.book().write("AnnURI", listUri)
        }
    }

    private fun setImageInSlider(images: ArrayList<Uri>, imageSlider: SliderView) {
        val adapter = AdapterAnnounceSlider()
        adapter.renewItems(images)

        imageSlider.isAutoCycle = true
        imageSlider.setSliderAdapter(adapter)
        imageSlider.isAutoCycle = true
        imageSlider.startAutoCycle()
    }

}