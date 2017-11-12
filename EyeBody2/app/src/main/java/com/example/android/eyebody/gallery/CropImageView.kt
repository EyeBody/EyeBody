package com.example.android.eyebody.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.list_collage.view.*
import java.lang.ref.WeakReference

/**
 * Created by Yeji Moon on 2017-11-12.
 * https://github.com/ArthurHub/Android-Image-Cropper 참고하여 자바를 코틀린으로 재작성
 * CropImageView, CropImageOptions, CropImage
 * attrs.xml
 */
class CropImageView: FrameLayout {
    var mImageView: ImageView? = null
//    var mCropOverlayView: CropOverlayView?
//    val mImageInverseMatrix = Matrix()
//    var mProgressBar: ProgressBar
//    val mImagePoints = FloatArray(8)
//    val mScaleImagePoints = FloatArray(8)
//    var mAnimation: CropImageAnimation? = null
//    var mBitmap: Bitmap? = null
//    var mInitialDegreesRotated: Int = 0
//    var mDegreesRotated: Int = 0
    var mFlipHorizontally: Boolean = false
    var mFlipVertically: Boolean = false
//    var mLayoutWidth: Int = 0
//    var mLayoutHeight: Int = 0
//    var mImageResource: Int = 0
    var mScaleType: ScaleType = ScaleType.FIT_CENTER
    var mSaveBitmapToInstanceState = false
    var mShowCropOverlay = true
    var mShowProgressBar = true
    var mAutoZoomEnabled = true
    var mMaxZoom: Int = 0
//    var mOnCropOverlayReleasedListener: OnSetCropOverlayReleasedListener? = null
//    var mOnSetCropOverlayMovedListener: OnSetCropOverlayMovedListener? = null
//    var mOnSetCropWindowChangeListener: OnSetCropWindowChangeListener? = null
//    var mOnSetImageUriCompleteListener: OnSetImageUriCompleteListener? = null
//    var mOnCropImageCompleteListener: OnCropImageCompleteListener? = null
//    var mLoadedImageUri: Uri? = null
//    var mLoadedSampleSize = 1
//    var mZoom = 1f
//    var mZoomOffsetX: Float = 0.toFloat()
//    var mZoomOffsetY: Float = 0.toFloat()
//    var mRestoreCropWindowRect: RectF? = null
//    var mRestoreDegreesRotated: Int = 0
//    var mSizeChanged: Boolean = false
//    var mSaveInstanceStateBitmapUri: Uri? = null
//    var mBitmapLoadingWorkerTask: WeakReference<BitmapLoadingWorkerTask>? = null
//    var mBitmapCroppingWorkerTask: WeakReference<BitmapCroppingWorkerTask>? = null

    constructor(context: Context, attrs: AttributeSet? = null): super(context, attrs){
        var options: CropImageOptions? = null
        var intent: Intent? = null
        if(context is Activity)
            intent = (context as Activity).intent

        if(intent != null){
            var bundle: Bundle = intent.getBundleExtra(CropImage().CROP_IMAGE_EXTRA_BUNDLE)
            if(bundle != null)
                options = bundle.getParcelable(CropImage().CROP_IMAGE_EXTRA_OPTIONS)
        }

        if(options == null){
            options = CropImageOptions()

            if(attrs != null){
                var ta: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CropImageView, 0, 0)
                try{
                    options.fixAspectRatio = ta.getBoolean(R.styleable.CropImageView_cropFixAspectRatio, options.fixAspectRatio)
                    options.aspectRatioX = ta.getInteger(R.styleable.CropImageView_cropAspectRatioX, options.aspectRatioX)
                    options.aspectRatioY = ta.getInteger(R.styleable.CropImageView_cropAspectRatioY, options.aspectRatioY)
                    options.scaleType = ScaleType.values()[ta.getInt(R.styleable.CropImageView_cropScaleType, options.scaleType.ordinal)]
                    options.autoZoomEnabled = ta.getBoolean(R.styleable.CropImageView_cropAutoZoomEnabled, options.autoZoomEnabled)
                    options.multiTouchEnabled = ta.getBoolean(R.styleable.CropImageView_cropMultiTouchEnabled, options.multiTouchEnabled)
                    options.maxZoom = ta.getInteger(R.styleable.CropImageView_cropMaxZoom, options.maxZoom)
                    options.cropShape = CropShape.values()[ta.getInt(R.styleable.CropImageView_cropShape, options.cropShape.ordinal)]
                    options.guidelines = Guidelines.values()[ta.getInt(R.styleable.CropImageView_cropGuidelines, options.guidelines.ordinal)]
                    options.snapRadius = ta.getDimension(R.styleable.CropImageView_cropSnapRadius, options.snapRadius)
                    options.touchRadius = ta.getDimension(R.styleable.CropImageView_cropTouchRadius, options.touchRadius)
                    options.initialCropWindowPaddingRatio = ta.getFloat(R.styleable.CropImageView_cropInitialCropWindowPaddingRatio, options.initialCropWindowPaddingRatio)
                    options.borderLineThickness = ta.getDimension(R.styleable.CropImageView_cropBorderLineThickness, options.borderLineThickness)
                    options.borderLineColor = ta.getInteger(R.styleable.CropImageView_cropBorderLineColor, options.borderLineColor)
                    options.borderCornerThickness = ta.getDimension(R.styleable.CropImageView_cropBorderCornerThickness, options.borderCornerThickness)
                    options.borderCornerOffset = ta.getDimension(R.styleable.CropImageView_cropBorderCornerOffset, options.borderCornerOffset)
                    options.borderCornerLength = ta.getDimension(R.styleable.CropImageView_cropBorderCornerLength, options.borderCornerLength)
                    options.borderCornerColor = ta.getInteger(R.styleable.CropImageView_cropBorderCornerColor, options.borderCornerColor)
                    options.guidelinesThickness = ta.getDimension(R.styleable.CropImageView_cropGuidelinesThickness, options.guidelinesThickness)
                    options.guidelinesColor = ta.getInteger(R.styleable.CropImageView_cropGuidelinesColor, options.guidelinesColor)
                    options.backgroundColor = ta.getInteger(R.styleable.CropImageView_cropBackgroundColor, options.backgroundColor)
                    options.showCropOverlay = ta.getBoolean(R.styleable.CropImageView_cropShowCropOverlay, mShowCropOverlay)
                    options.showProgressBar = ta.getBoolean(R.styleable.CropImageView_cropShowProgressBar, mShowProgressBar)
                    options.borderCornerThickness = ta.getDimension(R.styleable.CropImageView_cropBorderCornerThickness, options.borderCornerThickness)
                    options.minCropWindowWidth = ta.getDimension(R.styleable.CropImageView_cropMinCropWindowWidth, options.minCropWindowWidth.toFloat()).toInt()
                    options.minCropWindowHeight = ta.getDimension(R.styleable.CropImageView_cropMinCropWindowHeight, options.minCropWindowHeight.toFloat()).toInt()
                    options.minCropResultWidth = ta.getFloat(R.styleable.CropImageView_cropMinCropResultWidthPX, options.minCropResultWidth.toFloat()).toInt()
                    options.minCropResultHeight = ta.getFloat(R.styleable.CropImageView_cropMinCropResultHeightPX, options.minCropResultHeight.toFloat()).toInt()
                    options.maxCropResultWidth = ta.getFloat(R.styleable.CropImageView_cropMaxCropResultWidthPX, options.maxCropResultWidth.toFloat()).toInt()
                    options.maxCropResultHeight = ta.getFloat(R.styleable.CropImageView_cropMaxCropResultHeightPX, options.maxCropResultHeight.toFloat()).toInt()
                    options.flipHorizontally = ta.getBoolean(R.styleable.CropImageView_cropFlipHorizontally, options.flipHorizontally)
                    options.flipVertically = ta.getBoolean(R.styleable.CropImageView_cropFlipHorizontally, options.flipVertically)

                    mSaveBitmapToInstanceState = ta.getBoolean(R.styleable.CropImageView_cropSaveBitmapToInstanceState, mSaveBitmapToInstanceState)

                    if (ta.hasValue(R.styleable.CropImageView_cropAspectRatioX)
                            && ta.hasValue(R.styleable.CropImageView_cropAspectRatioX)
                            && !ta.hasValue(R.styleable.CropImageView_cropFixAspectRatio)) {
                        options.fixAspectRatio = true
                    }
                } finally {
                    ta.recycle()
                }
            }
        }

        options.validate()

        mScaleType = options.scaleType
        mAutoZoomEnabled = options.autoZoomEnabled
        mMaxZoom = options.maxZoom
        mShowCropOverlay = options.showCropOverlay
        mShowProgressBar = options.showProgressBar
        mFlipHorizontally = options.flipHorizontally
        mFlipVertically = options.flipVertically

        var inflater = LayoutInflater.from(context)
        var v = inflater.inflate(R.layout.crop_image_view, this, true)

        var mCropOverlayView = v.findViewById<CropOverlayView>(R.id.CropOverlayView)
        mCropOverlayView.setCropWindowChangeListener(
                CropOverlayView.CropWindowChangeListener{
                    override fun onCropWindowChanged 에베베벱베
                }
        )
    }

    enum class CropShape {
        RECTANGLE,
        OVAL
    }

    enum class Guidelines {
        OFF,
        ON_TOUCH,
        ON
    }

    enum class ScaleType {
        FIT_CENTER,
        CENTER,
        CENTER_CROP,
        CENTER_INSIDE
    }

    enum class RequestSizeOptions {
        NONE,
        SAMPLING,
        RESIZE_INSIDE,
        RESIZE_FIT,
        RESIZE_EXACT
    }
}