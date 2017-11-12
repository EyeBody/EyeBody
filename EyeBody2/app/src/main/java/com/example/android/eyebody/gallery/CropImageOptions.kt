package com.example.android.eyebody.gallery

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.TypedValue

/**
 * Created by Yeji Moon on 2017-11-12.
 */
class CropImageOptions : Parcelable {
    var cropShape = CropImageView.CropShape.RECTANGLE
    var snapRadius = 0f
    var touchRadius = 0f
    var guidelines = CropImageView.Guidelines.ON_TOUCH
    var scaleType = CropImageView.ScaleType.FIT_CENTER
    var showCropOverlay = true
    var showProgressBar = true
    var autoZoomEnabled = true
    var multiTouchEnabled = false
    var maxZoom = 4
    var initialCropWindowPaddingRatio = 0.1f

    var fixAspectRatio = false
    var aspectRatioX = 1
    var aspectRatioY = 1

    var borderLineThickness = 0f
    var borderLineColor = Color.argb(170, 255, 255, 255)
    var borderCornerThickness = 0f
    var borderCornerOffset = 0f
    var borderCornerLength = 0f
    var borderCornerColor = Color.WHITE

    var guidelinesThickness = 0f
    var guidelinesColor = Color.argb(170, 255, 255, 255)
    var backgroundColor = Color.argb(119, 0, 0, 0)

    var minCropWindowWidth = 0
    var minCropWindowHeight = 0
    var minCropResultWidth = 40
    var minCropResultHeight = 40
    var maxCropResultWidth = 99999
    var maxCropResultHeight = 99999

    var activityTitle = ""
    var activityMenuIconColor = 0

    var outputUri = Uri.EMPTY
    var outputCompressFormat = Bitmap.CompressFormat.JPEG
    var outputCompressQuality = 90
    var outputRequestWidth = 0
    var outputRequestHeight = 0
    var outputRequestSizeOptions = CropImageView.RequestSizeOptions.NONE
    var noOutputImage = false

    var initialCropWindowRectangle = null
    var initialRotation = -1
    var allowRotation = true
    var allowFlipping = true
    var allowCounterRotation = false
    var rotationDegrees = 90
    var flipHorizontally = false
    var flipVertically = false
    var cropMenuCropButtonTitle = null

    var cropMenuCropButtonIcon = 0

    constructor(){
        var dm: DisplayMetrics = Resources.getSystem().displayMetrics

        snapRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, dm)
        touchRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, dm)

        borderLineThickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, dm)
        borderCornerThickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, dm)
        borderCornerOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, dm)
        borderCornerLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14f, dm)

        guidelinesThickness = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, dm)

        minCropWindowWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42f, dm) as Int
        minCropWindowHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42f, dm) as Int
    }

    constructor(parcel: Parcel) : this() {
        cropShape = CropImageView.CropShape.values()[parcel.readInt()]
        snapRadius = parcel.readFloat()
        touchRadius = parcel.readFloat()
        guidelines = CropImageView.Guidelines.values()[parcel.readInt()]
        scaleType = CropImageView.ScaleType.values()[parcel.readInt()]
        showCropOverlay = parcel.readByte() != 0.toByte()
        showProgressBar = parcel.readByte() != 0.toByte()
        autoZoomEnabled = parcel.readByte() != 0.toByte()
        multiTouchEnabled = parcel.readByte() != 0.toByte()
        maxZoom = parcel.readInt()
        initialCropWindowPaddingRatio = parcel.readFloat()
        fixAspectRatio = parcel.readByte() != 0.toByte()
        aspectRatioX = parcel.readInt()
        aspectRatioY = parcel.readInt()
        borderLineThickness = parcel.readFloat()
        borderLineColor = parcel.readInt()
        borderCornerThickness = parcel.readFloat()
        borderCornerOffset = parcel.readFloat()
        borderCornerLength = parcel.readFloat()
        borderCornerColor = parcel.readInt()
        guidelinesThickness = parcel.readFloat()
        guidelinesColor = parcel.readInt()
        backgroundColor = parcel.readInt()
        minCropWindowWidth = parcel.readInt()
        minCropWindowHeight = parcel.readInt()
        minCropResultWidth = parcel.readInt()
        minCropResultHeight = parcel.readInt()
        maxCropResultWidth = parcel.readInt()
        maxCropResultHeight = parcel.readInt()
        activityTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel).toString()
        activityMenuIconColor = parcel.readInt()
        outputUri = parcel.readParcelable(Uri::class.java.classLoader)
        outputCompressFormat = Bitmap.CompressFormat.valueOf(parcel.readString())
        outputCompressQuality = parcel.readInt()
        outputRequestWidth = parcel.readInt()
        outputRequestHeight = parcel.readInt()
        outputRequestSizeOptions = CropImageView.RequestSizeOptions.values()[parcel.readInt()]
        noOutputImage = parcel.readByte() != 0.toByte()
        initialCropWindowRectangle = parcel.readParcelable(Rect::class.java.classLoader)
        initialRotation = parcel.readInt()
        allowRotation = parcel.readByte() != 0.toByte()
        allowFlipping = parcel.readByte() != 0.toByte()
        allowCounterRotation = parcel.readByte() != 0.toByte()
        rotationDegrees = parcel.readInt()
        flipHorizontally = parcel.readByte() != 0.toByte()
        flipVertically = parcel.readByte() != 0.toByte()
        cropMenuCropButtonTitle = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel) as Nothing
        cropMenuCropButtonIcon = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cropShape.ordinal)
        parcel.writeFloat(snapRadius)
        parcel.writeFloat(touchRadius)
        parcel.writeInt(guidelines.ordinal)
        parcel.writeInt(scaleType.ordinal)
        parcel.writeByte((if (showCropOverlay) 1 else 0) as Byte)
        parcel.writeByte((if (showProgressBar) 1 else 0) as Byte)
        parcel.writeByte((if (autoZoomEnabled) 1 else 0) as Byte)
        parcel.writeByte((if (multiTouchEnabled) 1 else 0) as Byte)
        parcel.writeInt(maxZoom)
        parcel.writeFloat(initialCropWindowPaddingRatio)
        parcel.writeByte((if (fixAspectRatio) 1 else 0) as Byte)
        parcel.writeInt(aspectRatioX)
        parcel.writeInt(aspectRatioY)
        parcel.writeFloat(borderLineThickness)
        parcel.writeInt(borderLineColor)
        parcel.writeFloat(borderCornerThickness)
        parcel.writeFloat(borderCornerOffset)
        parcel.writeFloat(borderCornerLength)
        parcel.writeInt(borderCornerColor)
        parcel.writeFloat(guidelinesThickness)
        parcel.writeInt(guidelinesColor)
        parcel.writeInt(backgroundColor)
        parcel.writeInt(minCropWindowWidth)
        parcel.writeInt(minCropWindowHeight)
        parcel.writeInt(minCropResultWidth)
        parcel.writeInt(minCropResultHeight)
        parcel.writeInt(maxCropResultWidth)
        parcel.writeInt(maxCropResultHeight)
        TextUtils.writeToParcel(activityTitle, parcel, flags)
        parcel.writeInt(activityMenuIconColor)
        parcel.writeParcelable(outputUri, flags)
        parcel.writeString(outputCompressFormat.name)
        parcel.writeInt(outputCompressQuality)
        parcel.writeInt(outputRequestWidth)
        parcel.writeInt(outputRequestHeight)
        parcel.writeInt(outputRequestSizeOptions.ordinal)
        parcel.writeInt(if (noOutputImage) 1 else 0)
        parcel.writeParcelable(initialCropWindowRectangle, flags)
        parcel.writeInt(initialRotation)
        parcel.writeByte((if (allowRotation) 1 else 0) as Byte)
        parcel.writeByte((if (allowFlipping) 1 else 0) as Byte)
        parcel.writeByte((if (allowCounterRotation) 1 else 0) as Byte)
        parcel.writeInt(rotationDegrees)
        parcel.writeByte((if (flipHorizontally) 1 else 0) as Byte)
        parcel.writeByte((if (flipVertically) 1 else 0) as Byte)
        TextUtils.writeToParcel(cropMenuCropButtonTitle, parcel, flags)
        parcel.writeInt(cropMenuCropButtonIcon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CropImageOptions> {
        override fun createFromParcel(parcel: Parcel): CropImageOptions {
            return CropImageOptions(parcel)
        }

        override fun newArray(size: Int): Array<CropImageOptions?> {
            return arrayOfNulls(size)
        }
    }

    fun validate() {
        if (maxZoom < 0)
            throw IllegalArgumentException("Cannot set max zoom to a number < 1")
        if (touchRadius < 0)
            throw IllegalArgumentException("Cannot set touch radius value to a number <= 0 ")
        if (initialCropWindowPaddingRatio < 0 || initialCropWindowPaddingRatio >= 0.5)
            throw IllegalArgumentException("Cannot set initial crop window padding value to a number < 0 or >= 0.5")
        if (aspectRatioX <= 0)
            throw IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.")
        if (aspectRatioY <= 0)
            throw IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.")
        if (borderLineThickness < 0)
            throw IllegalArgumentException("Cannot set line thickness value to a number less than 0.")
        if (borderCornerThickness < 0)
            throw IllegalArgumentException("Cannot set corner thickness value to a number less than 0.")
        if (guidelinesThickness < 0)
            throw IllegalArgumentException("Cannot set guidelines thickness value to a number less than 0.")
        if (minCropWindowHeight < 0)
            throw IllegalArgumentException("Cannot set min crop window height value to a number < 0 ")
        if (minCropResultWidth < 0)
            throw IllegalArgumentException("Cannot set min crop result width value to a number < 0 ")
        if (minCropResultHeight < 0)
            throw IllegalArgumentException("Cannot set min crop result height value to a number < 0 ")
        if (maxCropResultWidth < minCropResultWidth)
            throw IllegalArgumentException("Cannot set max crop result width to smaller value than min crop result width")
        if (maxCropResultHeight < minCropResultHeight)
            throw IllegalArgumentException("Cannot set max crop result height to smaller value than min crop result height")
        if (outputRequestWidth < 0)
            throw IllegalArgumentException("Cannot set request width value to a number < 0 ")
        if (outputRequestHeight < 0)
            throw IllegalArgumentException("Cannot set request height value to a number < 0 ")
        if (rotationDegrees < 0 || rotationDegrees > 360)
            throw IllegalArgumentException("Cannot set rotation degrees value to a number < 0 or > 360")
    }
}