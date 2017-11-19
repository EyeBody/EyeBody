package com.example.android.eyebody.management.config

/**
 * Created by YOON on 2017-11-19
 */

/**
 * preference are saved false or true
 * actually it saved 0, 1, 2, 3, ~.
 *
 * switch used when need boolean type selector,
 * switch not used when need string type selector (using dialog)
 * @param preferenceValueExplanation optional param, {false, true} for explanation string when using switch, {preferenceValueList} for explanation when not using switch.
 */
open class ConfigManagementSubContent(val text: String = "", val hasSwitch: Boolean, val preferenceName: String?,
                                      val preferenceValueExplanation: List<String>? = null) {
    /*
    text가 큰 글씨로 써지고
    그 밑에 preferenceValueExplanation이 써짐 (null일 경우 안 씀)
    preferenceValueList는 switch를 사용하지 않을 때 (dialog를 띄울때) 만 사용함.

    TODO : Diaglog는 어떻게 처리할까?
     */
}

class SwitchableSubContent(text: String, preferenceName: String,
                           preferenceValueExplanation: List<String>? = null) :
        ConfigManagementSubContent(
                text = text,
                hasSwitch = true,
                preferenceName = preferenceName,
                preferenceValueExplanation = preferenceValueExplanation)

class DialogableSubContent(text: String, preferenceName: String?,
                           preferenceValueExplanation: List<String>? = null) :
        ConfigManagementSubContent(
                text = text,
                hasSwitch = false,
                preferenceName = preferenceName,
                preferenceValueExplanation = preferenceValueExplanation)
