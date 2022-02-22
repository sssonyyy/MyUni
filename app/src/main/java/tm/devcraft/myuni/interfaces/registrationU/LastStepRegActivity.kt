package tm.devcraft.myuni.interfaces.registrationU

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_last_step_reg.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.RegUserModel
import tm.devcraft.myuni.interfaces.fragments.create.createActivity.AdapterAnnounceSlider
import tm.devcraft.myuni.interfaces.selectAction.SelectActionAuthActivity
import tm.devcraft.myuni.utils.Utils
import java.util.*

class LastStepRegActivity : AppCompatActivity(R.layout.activity_last_step_reg) {
    private val dataForReg =  Paper.book().read<RegUserModel>("userDataReg")
    private val auth = FirebaseAuth.getInstance()
    private lateinit var db: FirebaseFirestore
    private lateinit var strg: FirebaseStorage
    private lateinit var strgRef : StorageReference
    private val REQUEST_CODE_RESULT = 1
    private var listUri: ArrayList<Uri> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        strg = FirebaseStorage.getInstance()
        strgRef = strg.reference.child("users")
        //println(Paper.book().read<Uri>("IMG_URI").toString() + "@@@@@@@@@@@@@@@@@@@@@@@@@@")
        //Paper.init(this)
        //val imgUri: Uri = Uri.parse(Paper.book().read("URI_IMG"))
        //listUri.add(imgUri)
        //auth.addAuthStateListener(mAuthListener)
        initViews()
        initListeners()

    }
    private fun initViews() {
        var adapter = AdapterAnnounceSlider()
        adapter.renewItems(listUri)
        imageSlider.setSliderAdapter(adapter)

        text_reg_name.text = dataForReg.aboutUser?.fullname + ", " + dataForReg.aboutUser?.date?.let {
            Utils.getAge(
                it
            )
        } + " лет"
        placephoto.setImageBitmap(Paper.book().read("IMG_BITMAP"))
        text_reg_faculty.text = dataForReg.aboutUser?.faculty
        text_reg_spec.text = "Специальность: " + dataForReg.aboutUser?.speciality
        text_reg_institut.text = dataForReg.aboutUser?.nameOfInstitution + " " + dataForReg.aboutUser?.course + " курс"

    }

    private fun setDataSlider() {

    }

    private fun initListeners() {
        input_skills.addTextChangedListener { object: TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    TODO("Not yet implemented")
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    input_skills.setText(s.toString().replace(", ","\n-"))
                }

                override fun afterTextChanged(s: Editable?) {
                    //input_skills.setText(input_skills.text.toString().replace(", ","\n-"))
                }
            }

        }
        input_interests.addTextChangedListener { object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                input_interests.setText(input_interests.text.toString().replace(", ","\n-"))
            }
        }
        }
        btn_next_action.setOnClickListener {
            dataForReg.aboutUser?.skills  = input_skills.text.toString()
            dataForReg.aboutUser?.interests = input_interests.text.toString()
            dataForReg.aboutUser?.about = input_about.text.toString()

            //Paper.book().write("userData", dataForReg)
            createUser(dataForReg.email,dataForReg.password)
                if (listUri.isNotEmpty() == false) {
                    Toast.makeText(this, "Загрузите ваше фото", Toast.LENGTH_LONG).show()

                }
        }
        btn_back.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
            it.startAnimation(animScale)
            startActivity(Intent(this, NextStepRegActivity::class.java))
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
                dataForReg.aboutUser?.listUri = listUri
                imageSlider.isAutoCycle = true
                imageSlider.setSliderAdapter(adapter)
            }
            //println( imageSlider.currentPagePosition)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1){
            data?.data?.let { listUri.add(it) }
            dataForReg.aboutUser?.listUri = listUri
            var adapter = AdapterAnnounceSlider()
            adapter.renewItems(listUri)
            imageSlider.setSliderAdapter(adapter)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, NextStepRegActivity::class.java))
        finish()
    }

    private fun loadImgAcc() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    private fun createUser(email: String,password: String){

        //odelAnn.created = FieldValue.serverTimestamp()
        //modelAnn.id = UUID.randomUUID().toString().replace("-","")
        var map = hashMapOf<String,String>()
        //var uid : String

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( OnCompleteListener<AuthResult>{
            task ->
            run {
                if (task.isSuccessful) {
                    var t = task
                    dataForReg.aboutUser?.let {
                        db.collection("users").document(task.result?.user?.uid.toString()).set(
                            it
                        )
                        listUri.forEach { uri ->
                            var uid = UUID.randomUUID().toString().replace("-","")
                            strgRef.child(t.result?.user?.uid.toString()+"/"+uid+".jpg").putFile(uri).addOnSuccessListener(
                                OnSuccessListener<UploadTask.TaskSnapshot> { task ->
                                    strgRef.child(t.result?.user?.uid.toString()+"/"+uid+".jpg").downloadUrl.addOnSuccessListener {
                                        println(it.toString()+"@!!!!!!!!!!!")
                                        if(listUri.indexOf(uri)==0){
                                            map["0"] = it.toString()
                                        }
                                        else{
                                            map[listUri.indexOf(uri).toString()] = it.toString()
                                        }
                                        if(listUri.indexOf(uri)==listUri.count()-1){
                                            println(map.toString())
                                            //dataForReg.aboutUser?.photo = map
                                            //println(modelAnn.Photos.toString())
                                            Paper.book().write("userDataReg",dataForReg)
                                            //Paper.book().read<createAnnounceModel>("createAnn").created = Calendar.getInstance().time
                                            //Paper.book().read<createAnnounceModel>("createAnn").id = UUID.randomUUID().toString()
                                            if(map.count() == listUri.count()){
                                                var doc = db.collection("users").document(t.result?.user?.uid.toString())
                                                doc.update("photo",map)
                                                doc.update("uid",t.result?.user?.uid.toString())
                                                //doc.update("statusPublicate", true)
                                                Paper.book().delete("userDataReg")
                                                val dLink = Firebase.dynamicLinks.createDynamicLink().setLink(
                                                    Uri.parse("https://myuni.su/people?id=${t.result?.user?.uid.toString()}"))
                                                    .setDomainUriPrefix("https://myuni.page.link")
                                                    .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
                                                    .buildDynamicLink()

                                                doc.update("dLink",dLink.uri.toString())
                                                doc.update("email", t.result?.user?.email)
                                                var intent = Intent(this, SelectActionAuthActivity::class.java)
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                })
                        }
                        //println(map.toString())

                        //println(dataForReg.uri)
                        //strgRef.child("images/users/" + task.result?.user?.uid.toString()).putFile(dataForReg.uri)
                    }

                    //finish()
                }
                else if(task.isCanceled){
                    println(task.exception?.message.toString())
                }
                else {
                    println(task.exception?.message.toString())
                }
            }
        })

        //println(FirebaseAuth.getInstance().currentUser?.uid)

        }



}