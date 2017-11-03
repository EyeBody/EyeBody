package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
<<<<<<< HEAD
import android.graphics.Color
=======
>>>>>>> origin/develop
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
=======

        collage = activity as CollageActivity

        //데이터는 onCreate에, view관련 코드는 onCreateView에 작성
        //activity.photoList = arguments.getParcelableArrayList("photoList")
        //activity.selectedPhotoList = arguments.getIntegerArrayList("selectedPhotoList")

>>>>>>> origin/develop
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
<<<<<<< HEAD
        inflater.inflate(R.menu.menu_image_select, menu)
        this.menu = menu
=======
        inflater.inflate(R.menu.menu_collage, menu)

        this.menu = menu

        menu.findItem(R.id.action_editImage).setVisible(false)
        menu.findItem(R.id.action_share).setVisible(false)
>>>>>>> origin/develop
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
<<<<<<< HEAD
            R.id.action_image_edit -> {
                //ImageEditFragment로 교체
=======
            R.id.action_editImage -> {
                //TODO 프래그먼트 교체
                var fragmentTransaction = fragmentManager.beginTransaction()
>>>>>>> origin/develop
                var imageEditFragment = ImageEditFragment()
                var bundle = Bundle()

                bundle.putIntegerArrayList("selectedPhotoList", collage.selectedPhotoList)
                imageEditFragment.arguments = bundle

<<<<<<< HEAD
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, imageEditFragment)
                        .addToBackStack(null)
                        .commit()
=======
                //fragmentTransaction.hide(this)
                fragmentTransaction
                        .replace(R.id.fragment_container, imageEditFragment)
                        .addToBackStack(null)
                        .commit()

                //Toast.makeText(activity, "test", Toast.LENGTH_SHORT).show()
>>>>>>> origin/develop
            }
        }
        return super.onOptionsItemSelected(item)
    }

<<<<<<< HEAD
    fun setSelected(itemView: View, pos: Int, isSelected: Boolean){
        if(isSelected){
            itemView.setBackgroundColor(Color.BLUE)
            //TODO 선택한 순서를 유지할지 말지 고민중
            if(!collage.selectedPhotoList.contains(pos)) collage.selectedPhotoList.add(pos)  //없으면 추가

            if(collage.selectedPhotoList.size > 0){    //선택한 이미지가 하나 이상일 때 이미지편집 메뉴 아이콘 보여주기
                menu.findItem(R.id.action_image_edit).setVisible(true)  //TODO bugfix 다시 돌아왔을 때 아이콘 표시가 안됨
            }
        } else {
            itemView.setBackgroundColor(Color.RED)
            collage.selectedPhotoList.remove(pos)

            if(collage.selectedPhotoList.size == 0){    //선택한 이미지가 하나도 없을 때 이미지편집 메뉴 아이콘 숨기기
                try{
                    menu.findItem(R.id.action_image_edit).setVisible(false)
                } catch (e: Exception) {
                    //메뉴가 초기화 되기 전 리스트를 먼저 초기화 해서 kotlin.UninitializedPropertyAccessException: lateinit property menu has not been initialized 오류 발생
                }
            }
        }
=======
    fun actionEditImage_setVisible(bool: Boolean){
        menu.findItem(R.id.action_editImage).setVisible(bool)
>>>>>>> origin/develop
    }
}