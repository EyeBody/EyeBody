package com.example.android.eyebody.gallery

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.fragment_image_edit.view.*

//https://github.com/niravkalola/Android-StickerView
class ImageEditFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.fragment_image_edit, container, false)
        //view.asdf.text = (activity as CollageActivity).selectedPhotoList.toString()
        return view
    }
}