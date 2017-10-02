package com.example.android.eyebody.init

import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyebody.R

/**
 * Created by YOON on 2017-09-24.
 */

// 시작하기 버튼에 잔잔하게 움직이는 배경 or something
// progress dot (3/3)

class Init3Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val v = inflater!!.inflate(R.layout.fragment_init3, container, false)

        Log.d("mydbg_init3","init3 진입")

        return v
    }

}