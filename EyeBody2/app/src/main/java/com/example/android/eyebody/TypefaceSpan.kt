package com.example.android.eyebody

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.util.LruCache

/**
 * Created by Yeji Moon on 2017-11-04.
 * 앱바 타이틀에도 커스텀 폰트 적용하기 위한 클래스
 */
class TypefaceSpan(var context: Context, var typefaceName: String): MetricAffectingSpan() {
    val sTypefaceCache: LruCache<String, Typeface> = LruCache<String, Typeface>(12);
    var mTypeface: Typeface? = null

    init {
        mTypeface = sTypefaceCache.get(typefaceName)

        if(mTypeface == null){
            mTypeface = Typeface.createFromAsset(context.applicationContext.assets, String.format("fonts/%s", typefaceName))
            sTypefaceCache.put(typefaceName, mTypeface)
        }
    }

    override fun updateDrawState(p: TextPaint) {
        p.setTypeface(mTypeface)
        p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    override fun updateMeasureState(p: TextPaint) {
        p.setTypeface(mTypeface)
        p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
    }
}