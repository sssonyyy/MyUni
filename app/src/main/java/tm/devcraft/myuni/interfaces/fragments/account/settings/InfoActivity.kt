package tm.devcraft.myuni.interfaces.fragments.account.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings_acc.*
import tm.devcraft.myuni.R

class InfoActivity : AppCompatActivity(R.layout.activity_info) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btn_back.setOnClickListener {
            finish()
        }
    }
}