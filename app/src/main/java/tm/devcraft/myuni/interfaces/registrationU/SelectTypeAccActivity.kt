package tm.devcraft.myuni.interfaces.registrationU

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select_type_acc.*
import tm.devcraft.myuni.R

class SelectTypeAccActivity : AppCompatActivity(R.layout.activity_select_type_acc) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_selected_student.setOnClickListener {
            startActivity(Intent(this, RegistrationUActivity::class.java))
            finish()
        }
        initListeners()
    }
    private fun initListeners() {
        btn_back.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(applicationContext, R.anim.other_btn)
            it.startAnimation(animScale)
            //startActivity(Intent(this, SelectActionAuthActivity::class.java))
            finish()
        }
        btn_selected_student.setOnClickListener {
            startActivity(Intent(this, RegistrationUActivity :: class.java))
            finish()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        //startActivity(Intent(this, SelectActionAuthActivity::class.java))
        finish()
    }
}