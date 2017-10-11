package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.android.eyebody.R

class ImageSelectFragment : Fragment() {
    var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_image_select, container, false)
        var photoList: ArrayList<Photo> = arguments.getParcelableArrayList("photoList")
        var selectedPhotoList: ArrayList<Int> = arguments.getIntegerArrayList("selectedPhotoList")

        //RecyclerView
        var imageSelectView: RecyclerView = view.findViewById(R.id.imageSelectView)
        imageSelectView.hasFixedSize()
        imageSelectView.adapter = CollageAdapter(activity, photoList, selectedPhotoList, menu)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_collage, menu)

        this.menu = menu!!

        menu!!.findItem(R.id.action_editImage).setVisible(false)
        menu!!.findItem(R.id.action_share).setVisible(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_editImage -> {
                //TODO 프래그먼트 교체
            }
        }
        return super.onOptionsItemSelected(item)
    }
}