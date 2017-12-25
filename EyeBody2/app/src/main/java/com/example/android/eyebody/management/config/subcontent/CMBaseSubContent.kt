package com.example.android.eyebody.management.config.subcontent

import junit.framework.Assert

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
open class CMBaseSubContent(val text: String = "", val hasSwitch: Boolean, val preferenceName: String?,
                            val preferenceValueExplanation: List<String>? = null) {
    /*
    text 가 큰 글씨로 써지고
    그 밑에 preferenceValueExplanation 이 써짐 (현재 value 에 따라 출력, null 일 경우 안 씀)
     */
    init {
        Assert.assertTrue("Assertion failed : more than 1 preferenceValueExplanation needs preferenceName\n" +
                "if not needed, preferenceName must be null\n" +
                "preferenceName : is null : ${preferenceName == null}\n" +
                "preferenceValueExplanation : size : ${preferenceValueExplanation?.size ?: "null"}",
                (preferenceName == null) == (preferenceValueExplanation == null || preferenceValueExplanation.size <= 1))
    }
}
