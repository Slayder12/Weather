package com.example.weather.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.weather.WeatherActivity
import com.example.weather.R
import com.example.weather.models.ViewPageModel


class ViewPagerFragment : Fragment() {
    private var check = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titleViewPagerTV: TextView = view.findViewById(R.id.titleViewPagerTV)
        val infoViewPagerTV: TextView = view.findViewById(R.id.infoViewPagerTV)
        val pictureViewPagerIV: ImageView = view.findViewById(R.id.pictureViewPagerIV)
        val startBTN: Button = view.findViewById(R.id.startBTN)

        val viewPagerItem =  arguments?.getSerializable("vp") as ViewPageModel

        check = viewPagerItem.checkButton!!

        titleViewPagerTV.text = viewPagerItem.title
        infoViewPagerTV.text = viewPagerItem.info
        pictureViewPagerIV.setImageResource(viewPagerItem.imageView)

        if (!check) {
            startBTN.visibility = View.VISIBLE
            startBTN.setOnClickListener{
                startActivity(Intent(activity, WeatherActivity::class.java))
            }
        }

    }



}