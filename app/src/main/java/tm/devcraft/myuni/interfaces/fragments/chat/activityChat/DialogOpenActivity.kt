package tm.devcraft.myuni.interfaces.fragments.chat.activityChat

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_chat_dialog_open.*
import tm.devcraft.myuni.R

class DialogOpenActivity : AppCompatActivity(R.layout.activity_chat_dialog_open) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews() {

    }

    private fun initListeners() {
        open_menu.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                open_menu.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.purple_200))
            } else {
                open_menu.backgroundTintList = null
            }
        }
        btn_back.setOnClickListener {
            finish()
        }
    }
}