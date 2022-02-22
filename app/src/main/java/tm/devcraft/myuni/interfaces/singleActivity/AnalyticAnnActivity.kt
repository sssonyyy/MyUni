package tm.devcraft.myuni.interfaces.singleActivity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_analytics.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.AnnounceUserModel
import tm.devcraft.myuni.data.userData
import tm.devcraft.myuni.utils.Utils
import java.util.*

class AnalyticAnnActivity : AppCompatActivity(R.layout.activity_analytics) {
    var model = Paper.book().read<AnnounceUserModel>("announceSelect")
    var user = Paper.book().read<userData>("userData")
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vuz.text = model.vuz
        type.text = model.skill
        price.text = model.price.toString()
        name.text = user.fullname
        age.text = user.date?.let { Date(it.seconds*1000 + it.nanoseconds) }
            ?.let { Utils.getAge(it) } + " лет"
        Glide.with(this).load(Uri.parse(model.Photos["0"])).fitCenter().into(photo)
        views.text = model.views.toString()
        share.text = model.reposted.toString()
        saved.text = model.saved.toString()
        orders.text = model.request.toString()
    }
}