package tm.devcraft.myuni.utils

import com.soywiz.klock.DateTime
import java.util.*

class Utils {
    companion object {
        fun getAge(time: Date): String {

            val millis1 = DateTime(time.time)
            val millis2: DateTime = DateTime.now()
            var temp = millis2.year.year - millis1.year.year
            val age = if(millis1.date.dayOfYear < millis2.date.dayOfYear){
                temp
            }else{
                --temp
            }

            return age.toString()
        }

        /*fun getPostDate(time: Date): String{
            val time: DateTime = DateTime(time.time)
            val now: DateTime = DateTime.now()

            return
        }*/
    }


}
