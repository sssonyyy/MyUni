package tm.devcraft.myuni.interfaces.fragments.create.createActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_create_announce.*
import kotlinx.android.synthetic.main.activity_create_announce.btn_back
import kotlinx.android.synthetic.main.activity_create_announce.btn_switch
import kotlinx.android.synthetic.main.activity_create_announce.description_cont
import kotlinx.android.synthetic.main.activity_create_announce.input_about
import kotlinx.android.synthetic.main.activity_create_announce.input_link
import kotlinx.android.synthetic.main.activity_create_announce.link_cont
//import kotlinx.android.synthetic.main.activity_create_event.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.createAnnounceModel

class CreateAnnounce : AppCompatActivity(R.layout.activity_create_announce) {
    private lateinit var modelAnn: createAnnounceModel
    private var tags = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       //Paper.init(this)
        modelAnn = createAnnounceModel()
        link_cont.isHintEnabled = false
        description_cont.isHintEnabled = false

        initListeners()

    }

    private fun initListeners() {
        btn_switch.setOnCheckedChangeListener { _, b ->
            if (b){
                input_price_cont.visibility = View.VISIBLE
            }
            else{
                input_price.text?.clear()
                input_price_cont.visibility = View.GONE
            }
        }
        btn_back.setOnClickListener {
            finish()
        }

        for(index in 0 until tagsAnnounceCreate.childCount){
            var chip : Chip = tagsAnnounceCreate[index] as Chip
            chip.setOnCheckedChangeListener { compoundButton, b ->
                if(b){
                    tags.add(compoundButton.text.toString())
                }
                else{
                    tags.remove(compoundButton.text.toString())
                }
            }
        }

        view_announce.setOnClickListener {
            modelAnn.Agreement = ch_confirm.isChecked
            modelAnn.Link = input_link.text.toString()
            modelAnn.description = input_about.text.toString()
            modelAnn.skill = skill_selected.text.toString()
            modelAnn.vuz = vuz_selected.text.toString()
            modelAnn.uidCreator = FirebaseAuth.getInstance().uid.toString()
            if(btn_switch.isChecked){
                modelAnn.price = Integer.parseInt(input_price.text.toString())
            }
            modelAnn.tags = tags
            println(modelAnn.tags)
            Paper.book().write("createAnn", modelAnn)
            startActivity(Intent(this, AnnouncePublished::class.java))
        }
    }
}