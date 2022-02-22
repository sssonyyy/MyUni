package tm.devcraft.myuni.interfaces.fragments.create.createActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_announce_pub.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.createAnnounceModel
import tm.devcraft.myuni.interfaces.selectBottomNav.SelectBottomNavActivity
import java.util.*

class AnnouncePublished : AppCompatActivity(R.layout.activity_announce_pub) {
    private val db = Firebase.firestore
    private val modelAnn = Paper.book().read<createAnnounceModel>("createAnn")
    private val REQUEST_CODE_RESULT = 1
    private var listUri: ArrayList<Uri> = arrayListOf()
    private val strg = FirebaseStorage.getInstance()
    private val strgRef = strg.reference.child("announces")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageList: MutableList<String> = mutableListOf()

        imageList.add("https://mathoplywood.com/wp-content/uploads/2021/02/What-is-the-most-durable-tree.jpg")
        imageList.add("https://www.1000ideas.ru/upload/iblock/f35/derevo-problem.jpg")

        //setImageInSlider(imageList, imageSlider)
        initViews()
        initListeners()
    }

    private fun initViews() {
        price.text = modelAnn.price.toString()
        description.text = modelAnn.description
        name_skill.text = modelAnn.skill

    }

    private fun initListeners() {
        btn_add_photo.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
            it.startAnimation(animScale)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_RESULT)
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

            modelAnn.created = FieldValue.serverTimestamp()
            modelAnn.id = UUID.randomUUID().toString().replace("-","")
            var map = hashMapOf<String,String>()
            listUri.forEach { uri ->
                var uid = UUID.randomUUID().toString().replace("-","")


                strgRef.child(modelAnn.id+"/"+uid+".jpg").putFile(uri).addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { task ->
                        strgRef.child(modelAnn.id+"/"+uid+".jpg").downloadUrl.addOnSuccessListener {
                            println(it.toString())
                            if(listUri.indexOf(uri)==0){
                                map["0"] = it.toString()
                            }
                            else{
                                map[listUri.indexOf(uri).toString()] = it.toString()
                            }
                            if(listUri.indexOf(uri)==listUri.count()-1){
                                var doc = db.collection("announces").document()
                                if(map.count() == listUri.count()){
                                    //modelAnn.Photos = map
                                    //println(modelAnn.Photos.toString())
                                    //Paper.book().write("createAnn",modelAnn)
                                    //Paper.book().read<createAnnounceModel>("createAnn").created = Calendar.getInstance().time
                                    //Paper.book().read<createAnnounceModel>("createAnn").id = UUID.randomUUID().toString()
                                    doc.set(modelAnn)
                                    Paper.book().read<createAnnounceModel>("createAnn").tags.forEach {
                                        db.collection("utils").document("tags").update("tags", FieldValue.arrayUnion(it))
                                    }
                                    doc.update("photos",map)
                                    doc.update("statusPublicate", true)
                                    var intent = Intent(this, SelectBottomNavActivity::class.java)
                                    val dLink = Firebase.dynamicLinks.createDynamicLink().setLink(
                                        Uri.parse("https://myuni.su/announces?id=${modelAnn.id}"))
                                        .setDomainUriPrefix("https://myuni.page.link")
                                        .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                                        .buildDynamicLink()


                                    doc.update("dlink", dLink.uri.toString())
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
                                    startActivity(intent)

                                }
                            }
                        }
                    })


                /*strgRef.child(modelAnn.id+"/"+uid+".jpg").putFile(uri).addOnSuccessListener {
                    strgRef.child(modelAnn.id+"/"+uid+".jpg").downloadUrl.addOnSuccessListener {
                        println("!!!!!!!!!!!!!"+it.)

                    }
                }*/
            }

            println(map.toString())

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            data?.data?.let { listUri.add(it) }
            setImageInSlider(listUri, imageSlider)
            Paper.book().write("AnnURI", listUri)
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