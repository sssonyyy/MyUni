package tm.devcraft.myuni.interfaces.registrationU

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_complete_reg_screen.*
import tm.devcraft.myuni.R

class CompleteReg:AppCompatActivity(R.layout.activity_complete_reg_screen) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doNextActivity()
    }
    private fun doNextActivity() {
        ViewCompat.animate(container_comp).withStartAction { container_comp.visibility = View.VISIBLE }
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(1500)
            .start()
        /*Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, SelectActionAuthActivity::class.java))
            finish()}, 3000)*/
    }
}