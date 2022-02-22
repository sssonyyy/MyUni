package tm.devcraft.myuni.interfaces.loadScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_load_screen.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.interfaces.selectAction.SelectActionAuthActivity
import tm.devcraft.myuni.interfaces.selectBottomNav.SelectBottomNavActivity

class LoadScreenActivity : AppCompatActivity(R.layout.activity_load_screen) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Paper.init(this)
            //Firebase.auth.signOut()

        doNextActivity()
    }

    private fun doNextActivity() {
        ViewCompat.animate(text_load_screen).withStartAction { text_load_screen.visibility = View.VISIBLE }
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(1500)
            .start()
        Handler(Looper.getMainLooper()).postDelayed({
            if(Firebase.auth.currentUser != null ){
                startActivity(Intent(this, SelectBottomNavActivity::class.java))
            }else{
                startActivity(Intent(this, SelectActionAuthActivity::class.java))
            }

            finish()}, 3000)
    }
}