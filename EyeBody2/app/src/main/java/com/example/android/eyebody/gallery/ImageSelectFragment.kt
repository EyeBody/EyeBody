package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.example.android.eyebody.R

class ImageSelectFragment : Fragment() {
    lateinit var menu: Menu
    lateinit var imageSelectView: RecyclerView
    lateinit var collage: CollageActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        collage = activity as CollageActivity

        //데이터는 onCreate에, view관련 코드는 onCreateView에 작성
        //activity.photoList = arguments.getParcelableArrayList("photoList")
        //activity.selectedPhotoList = arguments.getIntegerArrayList("selectedPhotoList")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.fragment_image_select, container, false)

        //RecyclerView
        imageSelectView = view.findViewById(R.id.imageSelectView)
        imageSelectView.hasFixedSize()
        imageSelectView.adapter = CollageAdapter(activity, collage.photoList, collage.selectedPhotoList, this)

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
                var fragmentTransaction = fragmentManager.beginTransaction()
                var imageEditFragment = ImageEditFragment()
                var bundle = Bundle()

                bundle.putIntegerArrayList("selectedPhotoList", collage.selectedPhotoList)
                imageEditFragment.arguments = bundle

                //fragmentTransaction.hide(this)
                fragmentTransaction
                        .replace(R.id.fragment_container, imageEditFragment)
                        .addToBackStack(null)
                        .commit()

                //Toast.makeText(activity, "test", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun actionEditImage_setVisible(bool: Boolean){
        menu.findItem(R.id.action_editImage).setVisible(bool)
    }
}