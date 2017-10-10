package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyebody.R

class ImageSelectFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_image_select, container, false)
        var photoList: ArrayList<Photo> = arguments.getParcelableArrayList("photoList")
        var selectedPhotoList: ArrayList<Int> = arguments.getIntegerArrayList("selectedPhotoList")

        //RecyclerView
        var imageSelectView: RecyclerView = view.findViewById(R.id.imageSelectView)
        imageSelectView.hasFixedSize()
        imageSelectView.adapter = CollageAdapter(activity, photoList, selectedPhotoList)

        return view
    }
}