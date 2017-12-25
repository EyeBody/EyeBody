package com.example.android.eyebody.init

import android.Manifest
import android.os.Bundle
import android.app.Fragment
import android.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE
import android.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.transition.Transition
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import com.example.android.eyebody.R
import io.vrinda.kotlinpermissions.PermissionCallBack

/**
 * Created by YOON on 2017-09-24.
 */

// 시작하기 버튼에 잔잔하게 움직이는 배경 or something
// progress dot (1/3)

class Init1Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val v = inflater!!.inflate(R.layout.fragment_init1, container, false)
        Log.d("mydbg_init1","[ init1 진입 ]")

        val startButton = v.findViewById<Button>(R.id.ImageButton_init1_start_to_init2)

        startButton.setOnClickListener {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)

            val init2 = Init2Fragment()
            activity.fragmentManager.beginTransaction()
                    .replace(R.id.fragment_init_content, init2)
                    .commit()

            val initActivity : InitActivity = activity as InitActivity
        }

        return v
    }

}