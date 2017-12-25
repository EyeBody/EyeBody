package com.example.android.eyebody.management.config.subcontent

/**
 * Created by YOON on 2017-12-02
 */

class SwitchableSubContent(text: String, preferenceName: String,
                           preferenceValueExplanation: List<String>? = null) :
        CMBaseSubContent(
                text = text,
                preferenceName = preferenceName,
                hasSwitch = true,
                preferenceValueExplanation = preferenceValueExplanation
        )
