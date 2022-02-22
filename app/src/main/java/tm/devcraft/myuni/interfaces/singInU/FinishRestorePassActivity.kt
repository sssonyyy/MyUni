package tm.devcraft.myuni.interfaces.singInU

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_finish_restore_pass.*
import kotlinx.android.synthetic.main.activity_finish_restore_pass.btn_back
import kotlinx.android.synthetic.main.activity_restore_pass_code.*
import tm.devcraft.myuni.R

class FinishRestorePassActivity : AppCompatActivity(R.layout.activity_finish_restore_pass) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        hintDisable()
    }

    private fun hintDisable() {
        input_pass_cont.isHintEnabled = false
        input_pass_confirm_new_cont.isHintEnabled = false
    }

    private fun initListeners() {
        btn_confirm.setOnClickListener {
            startActivity(Intent(this, SignInUActivity::class.java))
            finish()
        }
        btn_back.setOnClickListener {
            val animScale = AnimationUtils.loadAnimation(this, R.anim.exchange_btn)
            it.startAnimation(animScale)
            startActivity(Intent(this, RestorePassActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, RestorePassActivity::class.java))
        finish()
    }
}