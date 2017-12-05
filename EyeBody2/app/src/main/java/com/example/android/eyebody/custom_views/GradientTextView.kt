package com.example.android.eyebody.custom_views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.LinearGradient
import android.graphics.Shader
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.TextView
import com.example.android.eyebody.R

/**
 * Created by yeaji on 2017-12-05.
 */
class GradientTextView : TextView {
    //TODO 작업 중인 커스텀 뷰, 그래디언트 적용이 안된다아악
    private var startColor = R.color.gradientBlue
    private var endColor = R.color.gradientPurple

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) { init(attrs) }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) { init(attrs) }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if(changed){
            var color = intArrayOf(startColor, endColor)
            var position = floatArrayOf(0f, 1f)
            var tileMode = Shader.TileMode.REPEAT
            var grad = LinearGradient(0f, 0f, 0f, 200f, color, position, tileMode)

            getPaint().setShader(grad)
        }
    }

    fun init(attrs: AttributeSet) {
        var ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView)
        startColor = ta.getColor(R.styleable.GradientTextView_start_color, R.color.gradientBlue)
        endColor = ta.getColor(R.styleable.GradientTextView_start_color, R.color.gradientPurple)

        ta.recycle()
    }
}