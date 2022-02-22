package tm.devcraft.myuni.interfaces.singleActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_event_analytics.*
import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.EventUserModel
import tm.devcraft.myuni.data.userData
import java.text.SimpleDateFormat

class AnalyticEventActivity : AppCompatActivity(R.layout.activity_event_analytics) {
    var model = Paper.book().read<EventUserModel>("eventSelect")
    var user = Paper.book().read<userData>("userData")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name.text = model.name
        type.text = model.type
        val format = SimpleDateFormat("dd/MM/yyyy")
        if(model.dateTo != null ){
            date_created.text = format.format(model.dateFrom).toString().replace("/",".") + "-" + format.format(model.dateTo).toString().replace("/",".")
        }
        else{
            date_created.text = format.format(model.dateFrom).toString().replace("/",".")
        }
        views.text = model.views.toString()
        share.text = model.reposted.toString()
        saved.text = model.saved.toString()

    }
}