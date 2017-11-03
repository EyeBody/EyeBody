package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
import android.graphics.Color
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
        collage = activity as CollageActivity
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
        inflater.inflate(R.menu.menu_image_select, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_image_edit -> {
                //ImageEditFragment로 교체
                var imageEditFragment = ImageEditFragment()
                var bundle = Bundle()

                bundle.putIntegerArrayList("selectedPhotoList", collage.selectedPhotoList)
                imageEditFragment.arguments = bundle

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, imageEditFragment)
                        .addToBackStack(null)
                        .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
    }
}