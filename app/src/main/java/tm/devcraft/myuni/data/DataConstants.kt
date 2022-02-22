package tm.devcraft.myuni.data

import tm.devcraft.myuni.R
import tm.devcraft.myuni.data.models.fastNavData.FastNavData

object DataConstants {
    val fastNavArr: ArrayList<FastNavData> = arrayListOf(
        FastNavData(
            "События",
            R.drawable.events,
            "Здесь вы найдете\n интересные события\n для студентов",
            1
        ),
        FastNavData(
            "Люди",
            R.drawable.people,
            "Здесь вы найдете\n интересных людей\n внутри и вне вашего\n вуза  ",
            2
        ),
        FastNavData(
            "Избранное",
            R.drawable.hint,
            "Здесь вы найдете всю\n историю вашего\n поиска ",
             3
        ),
        FastNavData(
            "Анонсы",
            R.drawable.announces,
            "Здесь вы найдете\n людей, готовых помочь\n в вашей работе",
            4
        )
    )

    val arr: ArrayList<Any> = arrayListOf(
        "Далер Кодиров",
        R.drawable.user_photo

    )
}