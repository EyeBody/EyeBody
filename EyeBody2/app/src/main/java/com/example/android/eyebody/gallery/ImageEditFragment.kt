package com.example.android.eyebody.gallery

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.fragment_image_edit.view.*

class ImageEditFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.fragment_image_edit, container, false)
        view.asdf.text = (activity as CollageActivity).selectedPhotoList.toString()
        return view
    }
}