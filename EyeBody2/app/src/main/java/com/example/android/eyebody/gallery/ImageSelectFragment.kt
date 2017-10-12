package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.example.android.eyebody.R

class ImageSelectFragment : Fragment() {
    lateinit var menu: Menu
    var selectedPhotoList: ArrayList<Int> = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_image_select, container, false)
        var photoList: ArrayList<Photo> = arguments.getParcelableArrayList("photoList")
        selectedPhotoList = arguments.getIntegerArrayList("selectedPhotoList")

        //RecyclerView
        var imageSelectView: RecyclerView = view.findViewById(R.id.imageSelectView)
        imageSelectView.hasFixedSize()
        imageSelectView.adapter = CollageAdapter(activity, photoList, selectedPhotoList, this)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_collage, menu)

        this.menu = menu

        menu.findItem(R.id.action_editImage).setVisible(false)
        menu.findItem(R.id.action_share).setVisible(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_editImage -> {
                //TODO 프래그먼트 교체
                Toast.makeText(activity, "이미지 꾸미기: " + selectedPhotoList.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun actionEditImage_setVisible(bool: Boolean){
        menu.findItem(R.id.action_editImage).setVisible(bool)
    }
}