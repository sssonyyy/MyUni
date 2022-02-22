package tm.devcraft.myuni.interfaces.registrationU

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_next_step_reg.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.RegUserModel
import java.text.SimpleDateFormat
import java.util.*




class NextStepRegActivity : AppCompatActivity(R.layout.activity_next_step_reg) {

    private val dataForReg =  Paper.book().read<RegUserModel>("userDataReg")
    private val REQUEST_CODE_RESULT = 1
    private var selected: String? = ""
    private var sendImg: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_step_reg)

        //Paper.init(this)
        initViews()
        initListeners()
        btn_signin.setOnClickListener{
            if((input_name.text?.trim().toString().isNotEmpty())&&(selected_born.text?.trim().toString().isNotEmpty())&&(selected_sex.text?.trim().toString().isNotEmpty())&&(input_name_school.text?.trim().toString().isNotEmpty())&&(input_name_facultet.text?.trim().toString().isNotEmpty())&&(input_name_course.text?.trim().toString().isNotEmpty())&&(input_name_spec.text?.trim().toString().isNotEmpty())){
              if  ((input_name.text.toString().contains( Regex("^([a-zA-Z]{2,}\\s[a-zA-Z]{1,}'?-?[a-zA-Z]{2,}\\s?([a-zA-Z]{1,})?)")) != true)&&(input_name.text.toString().contains(Regex("^(?=.{1,40}\$)[а-яёА-ЯЁ]+(?:[-' ][а-яёА-ЯЁ]+)"))!= true)){
                  Toast.makeText(this, "Пожалуйста, введите ваше настоящее имя.", Toast.LENGTH_LONG).show()
                  }
              else{
                  saveUserData()
              }
            }
            else  Toast.makeText(this, "Все поля должны быть заполнены.", Toast.LENGTH_LONG).show()
        }
    }
    private fun saveUserData(){

        val intent = Intent(this, LastStepRegActivity::class.java)
        startActivity(intent)
        //finish()
    }
    private fun initListeners() {
        btn_back.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
            it.startAnimation(animScale)
            //startActivity(Intent(this, RegistrationUActivity::class.java))
            finish()
        }
        select_sex_cont.setOnClickListener {
            createSelectSexDialog()
        }
        select_born_cont.setOnClickListener {
            setDateBorn()
        }
        /*load_img_container_parent_do.setOnClickListener {
            loadImgAcc()
        }*/
        btn_signin.setOnClickListener {
            dataForReg.aboutUser?.course = Integer.parseInt(input_name_course.text.toString())
            dataForReg.aboutUser?.faculty = input_name_facultet.text.toString()
            dataForReg.aboutUser?.fullname = input_name.text.toString()
            dataForReg.aboutUser?.nameOfInstitution = input_name_school.text.toString()
            dataForReg.aboutUser?.speciality = input_name_spec.text.toString()
            println(dataForReg.toString())
            Paper.book().write("userDataReg", dataForReg)
            val intent = Intent(this, LastStepRegActivity::class.java)
            startActivity(intent)
            //Uri.parse()
            //finish()
        }

    }



    @SuppressLint("SimpleDateFormat")
    private fun setDateBorn() {
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
            dataForReg.aboutUser?.date = calendar.time
            selected_born.text = formatter.format(calendar.time).toString().replace("/", ".")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createSelectSexDialog(){
        val singleItems = arrayOf("Мужской", "Женский")
        val dialogInterface = MaterialAlertDialogBuilder(this, R.style.CheckBoxTheme)
        dialogInterface.setTitle("Выберите пол")
        dialogInterface.setIcon(R.drawable.ic_gender)
        dialogInterface.background = resources.getDrawable(R.drawable.bg_mui_dialog, null)
        dialogInterface.setSingleChoiceItems(singleItems, 0) { dialog, which ->
            selected = singleItems[which]
        }
        dialogInterface.setPositiveButton("Применить") { dialog, which ->
            selected_sex.text = selected
            dataForReg.aboutUser?.sex = (if(selected == "Мужской"){
                "male"
            }
            else{
                "female"
            }).toString()
            }
        dialogInterface.setNegativeButton("Отмена") { dialog, which -> }
        dialogInterface.show()

    }


    private fun initViews() {
        input_name_cont.isHintEnabled = false
        input_name_school_cont.isHintEnabled = false
        input_name_facultet_cont.isHintEnabled = false
        input_name_course_cont.isHintEnabled = false
        input_name_spec_cont.isHintEnabled = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, RegistrationUActivity::class.java))

    }
}


/*
private fun loadImgAcc() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, REQUEST_CODE_RESULT)
}
*/