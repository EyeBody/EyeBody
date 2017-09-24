package com.example.android.eyebody.init

import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.android.eyebody.R

/**
 * Created by YOON on 2017-09-24.
 */

// 시작하기 버튼에 잔잔하게 움직이는 배경 or something
// progress dot (1/3)

class Init1Fragment : Fragment() {
   /* override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        Log.d("mydbg_init1","init1 진입")

        val startButton = activity.findViewById<ImageButton>(R.id.button_init1_start)

        startButton.setOnClickListener { view ->
            val init2 = Init2Fragment()
            activity.fragmentManager.beginTransaction()
                    .replace(R.id.fragment_init_content, init2)
                    .commit()
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }*/
}