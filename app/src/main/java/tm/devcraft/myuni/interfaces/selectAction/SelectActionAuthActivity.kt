package tm.devcraft.myuni.interfaces.selectAction

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_select_action.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.interfaces.registrationU.SelectTypeAccActivity
import tm.devcraft.myuni.interfaces.singInU.SignInUActivity


class SelectActionAuthActivity : AppCompatActivity(R.layout.activity_select_action) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimation()
        initListeners()


    }

    private fun initListeners() {
        btn_sign_in.setOnClickListener{
            signInUser()
        }
        btn_signin.setOnClickListener{
            startActivity(Intent(this, SelectTypeAccActivity::class.java))
            //finish()
        }
    }

    private fun signInUser() {
        startActivity(Intent(this, SignInUActivity::class.java))
        //finish()
    }

    private fun setAnimation() {
        ViewCompat.animate(text_select_act_screen).withStartAction { text_select_act_screen.visibility = View.VISIBLE }
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(1500)
            .start()
        ViewCompat.animate(btn_sign_in).withStartAction { btn_sign_in.visibility = View.VISIBLE }
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(1800)
            .start()
        ViewCompat.animate(btn_signin).withStartAction { btn_signin.visibility = View.VISIBLE }
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(2100)
            .start()
    }
}