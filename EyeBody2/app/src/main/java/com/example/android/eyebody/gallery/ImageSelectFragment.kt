package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
<<<<<<< HEAD
=======
import android.graphics.Color
>>>>>>> gallery
import android.support.v7.widget.RecyclerView
import android.view.*
import com.example.android.eyebody.R

class ImageSelectFragment : Fragment() {
    lateinit var menu: Menu
    lateinit var imageSelectView: RecyclerView
    lateinit var collage: CollageActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
<<<<<<< HEAD

        collage = activity as CollageActivity

        //데이터는 onCreate에, view관련 코드는 onCreateView에 작성
        //activity.photoList = arguments.getParcelableArrayList("photoList")
        //activity.selectedPhotoList = arguments.getIntegerArrayList("selectedPhotoList")

=======
        collage = activity as CollageActivity
>>>>>>> gallery
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater.inflate(R.layout.fragment_image_select, container, false)

        //RecyclerView
        imageSelectView = view.findViewById(R.id.imageSelectView)
        imageSelectView.hasFixedSize()
<<<<<<< HEAD
        imageSelectView.adapter = CollageAdapter(activity, collage.photoList, collage.selectedPhotoList, this)
=======
        imageSelectView.adapter = CollageAdapter(activity, collage.photoList, collage.selectedIndexList, this)
>>>>>>> gallery

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
<<<<<<< HEAD
        inflater.inflate(R.menu.menu_collage, menu)

        this.menu = menu

        menu.findItem(R.id.action_editImage).setVisible(false)
        menu.findItem(R.id.action_share).setVisible(false)
=======
        inflater.inflate(R.menu.menu_image_select, menu)
        this.menu = menu

        menu.findItem(R.id.action_edit_image).setVisible(false)
        menu.findItem(R.id.action_share).setVisible(false)

        if(collage.selectedIndexList.size > 0){    //선택한 이미지가 하나 이상일 때 이미지편집 메뉴 아이콘 보여주기
            menu.findItem(R.id.action_edit_image).setVisible(true)
        }
>>>>>>> gallery
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
<<<<<<< HEAD
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
=======
            R.id.action_edit_image -> {
                //ImageEditFragment로 교체
                var imageEditFragment = ImageEditFragment()

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, imageEditFragment)
                        .addToBackStack(null)
                        .commit()
>>>>>>> gallery
            }
        }
        return super.onOptionsItemSelected(item)
    }

<<<<<<< HEAD
    fun actionEditImage_setVisible(bool: Boolean){
        menu.findItem(R.id.action_editImage).setVisible(bool)
=======
    fun setSelected(itemView: View, pos: Int, isSelected: Boolean){
        //TODO 최대 선택갯수 설정, 선택할 때 번호매기기
        if(isSelected){
            itemView.setBackgroundColor(Color.BLUE)
            if(!collage.selectedIndexList.contains(pos)) collage.selectedIndexList.add(pos)  //없으면 추가

            if(collage.selectedIndexList.size > 0){    //선택한 이미지가 하나 이상일 때 이미지편집 메뉴 아이콘 보여주기
                menu.findItem(R.id.action_edit_image).setVisible(true)
            }
        } else {
            itemView.setBackgroundColor(Color.RED)
            collage.selectedIndexList.remove(pos)

            if(collage.selectedIndexList.size == 0){    //선택한 이미지가 하나도 없을 때 이미지편집 메뉴 아이콘 숨기기
                try{
                    menu.findItem(R.id.action_edit_image).setVisible(false)
                } catch (e: Exception) {
                    //메뉴가 null일 때
                }
            }
        }
>>>>>>> gallery
    }
}