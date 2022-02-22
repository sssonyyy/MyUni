package tm.devcraft.myuni.interfaces.fragments.account.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_settings_acc.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.interfaces.selectAction.SelectActionAuthActivity

class SettingsActivity : AppCompatActivity(R.layout.activity_settings_acc) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signOut.setOnClickListener{
            Firebase.auth.signOut()
            var intent = Intent(this,SelectActionAuthActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        teamInfo.setOnClickListener {
            startActivity(Intent(this,InfoActivity::class.java))
        }

        btn_back.setOnClickListener {
            finish()
        }
    }
}