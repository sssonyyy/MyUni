package tm.devcraft.myuni.interfaces.registrationU

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_confirm_code.*
import tm.devcraft.myuni.R

class ConfirmCodeActivity : AppCompatActivity(R.layout.activity_confirm_code) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        btn_resend.setOnClickListener {
            startActivity(Intent(this, CompleteReg::class.java))
            //finish()
        }

        btn_back.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
            it.startAnimation(animScale)
            //startActivity(Intent(this, LastStepRegActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //startActivity(Intent(this, LastStepRegActivity::class.java))
        finish()
    }
}