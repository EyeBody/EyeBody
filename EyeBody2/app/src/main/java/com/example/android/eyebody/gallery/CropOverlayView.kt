package com.example.android.eyebody.gallery

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by Yeji Moon on 2017-11-13.
 */

class CropOverlayView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    lateinit var mCropWindowChangeListener: CropWindowChangeListener

    fun setCropWindowChangeListener(listener: CropWindowChangeListener) {
        mCropWindowChangeListener = listener
    }

    interface CropWindowChangeListener {
        fun onCropWindowChanged(inProgress: Boolean)
    }

    private fun callOnCropWindowChanged(inProgress: Boolean) {
        try {
            if (mCropWindowChangeListener != null) {
                mCropWindowChangeListener.onCropWindowChanged(inProgress)
            }
        } catch (e: Exception) {
            Log.e("AIC", "Exception in crop window changed", e)
        }

    }
}