package tm.devcraft.myuni.interfaces.singInU

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_restore_pass.*
import tm.devcraft.myuni.R

class RestorePassActivity : AppCompatActivity(R.layout.activity_restore_pass) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        hintDisable()
    }

    private fun hintDisable() {
        input_mail_cont.isHintEnabled = false
        input_number_phone_cont.isHintEnabled = false
    }

    private fun initListeners() {
        btn_next_action.setOnClickListener {
        startActivity(Intent(this, RestorePassCodeActivity::class.java))
        finish()
        }
        btn_back.setOnClickListener{
            val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
            it.startAnimation(animScale)
            startActivity(Intent(this, SignInUActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SignInUActivity::class.java))
        finish()
    }
}
