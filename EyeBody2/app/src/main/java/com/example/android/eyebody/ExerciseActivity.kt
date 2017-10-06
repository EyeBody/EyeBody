package com.example.android.eyebody

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/*
도장 찍는 캘린더(운동기록)
custom calendar로 UI 재구성
캘린더는 주요 기능이 아닌데, 커스텀 캘린더 만들기는 쉽지 않아보임

꼭 캘린더를 구현할 필요는 없고, 적당히 운동기록 기능을 만들면 됨
 */
class ExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
    }
}
