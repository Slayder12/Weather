package com.example.weather.models

import com.example.weather.R
import java.io.Serializable

class ViewPageModel(
    val title: String,
    val info: String,
    val imageView: Int,
    val checkButton: Boolean?
) : Serializable {

    companion object{
        val viewPagerList = mutableListOf(
            ViewPageModel(
              "Добро пожаловать в приложение Погода!",
                "В мобильном приложении \"Погода\" вы сможете быстро и отслеживать погоду в любом месте.",
                (R.drawable._1),
                true
            ),
            ViewPageModel(
                "Все функции в одном приложении",
                "Откройте для себя удобство мобильного приложения \"Погода\" ",
                (R.drawable._2),
                true
            ),
            ViewPageModel(
                "Точные данные о погоде\n",
                "Позволит вам подготовится к грядущей погоде.",
                (R.drawable._3),
                false
            ),
            
        )

    }
}