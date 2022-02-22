package tm.devcraft.myuni.interfaces.fragments.create.createActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_create_event.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.createEventModel
import java.text.SimpleDateFormat
import java.util.*

class CreateEvent : AppCompatActivity(R.layout.activity_create_event) {
    private lateinit var modelEvent: createEventModel
    private var tags = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Paper.init(this)
        modelEvent = createEventModel()
        link_cont.isHintEnabled = false
        description_cont.isHintEnabled = false

        initListeners()

    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate(field: String) {
        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("ВЫБЕРИТЕ ДАТУ РОЖДЕНИЯ")
        val materialDatePicker = builder.build()
        materialDatePicker.show(
            supportFragmentManager,
            "DATE_PICKER"
        )

        materialDatePicker.addOnPositiveButtonClickListener { selection ->
            val formatter = SimpleDateFormat("dd/M/yyyy")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selection as Long
            if(field == "from"){
                modelEvent.dateFrom = calendar.time
                selected_from.text = formatter.format(calendar.time).toString().replace("/", ".")
            }
            else{
                modelEvent.dateTo = calendar.time
                selected_to.text = formatter.format(calendar.time).toString().replace("/", ".")
            }
        }
    }

    private fun initListeners() {
        btn_switch.setOnCheckedChangeListener { _, b ->
            if (b){
                field_name_add_org.visibility = View.VISIBLE
            }
            else{
                input_about.text?.clear()
                field_name_add_org.visibility = View.GONE
            }
        }
        btn_back.setOnClickListener {
            finish()
        }

        select_date_from.setOnClickListener {
            setDate("from")
        }

        select_date_to.setOnClickListener {
            setDate("to")
        }

        for(index in 0 until tagsEventCreate.childCount){
            var chip : Chip = tagsEventCreate[index] as Chip
            chip.setOnCheckedChangeListener { compoundButton, b ->
                if(b){
                    tags.add(compoundButton.text.toString())
                }
                else{
                    tags.remove(compoundButton.text.toString())
                }
            }
        }

        view_event.setOnClickListener {
            if(btn_switch.isChecked){
                modelEvent.additionalOrg = name_add_org.text.toString()
            }
            modelEvent.name = input_name.text.toString()
            modelEvent.Link = input_link.text.toString()
            modelEvent.description = input_about.text.toString()
            modelEvent.type = type_selected.text.toString()
            modelEvent.place = place_selected.text.toString()
            modelEvent.uidCreator = FirebaseAuth.getInstance().uid.toString()
            modelEvent.tags = tags
            println(modelEvent.tags)
            Paper.book().write("createEvent", modelEvent)
            startActivity(Intent(this, EventPublished::class.java))
        }
    }
}