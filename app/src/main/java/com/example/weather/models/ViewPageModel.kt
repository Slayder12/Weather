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
                "Ваш надежный спутник в мире метеорологии. ",
                (R.drawable._1),
                true
            ),
            ViewPageModel(
                "Удобные функции для вас",
                "Здесь вы найдете детальные данные о температуре, влажности и скорости ветра.",
                (R.drawable._2),
                true
            ),
            ViewPageModel(
                "Найдите погоду в любом городе!\n",
                "Вы можете легко искать информацию о погоде как для конкретных городов,\nтак и для вашего текущего местоположения.",
                (R.drawable._3),
                false
            ),
            
        )

    }
}