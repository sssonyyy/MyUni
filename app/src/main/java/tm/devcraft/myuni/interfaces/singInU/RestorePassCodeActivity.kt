package tm.devcraft.myuni.interfaces.singInU

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_restore_pass.*
import kotlinx.android.synthetic.main.activity_restore_pass_code.*
import kotlinx.android.synthetic.main.activity_restore_pass_code.btn_back
import kotlinx.android.synthetic.main.activity_restore_pass_code.btn_next_action
import kotlinx.android.synthetic.main.activity_sign_in_uactivity.*
import tm.devcraft.myuni.R

class RestorePassCodeActivity : AppCompatActivity(R.layout.activity_restore_pass_code) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {

    }

    private fun initListeners() {
        btn_next_action.setOnClickListener {
            startActivity(Intent(this, FinishRestorePassActivity::class.java))
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
