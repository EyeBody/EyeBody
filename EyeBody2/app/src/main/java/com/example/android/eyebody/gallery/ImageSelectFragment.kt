package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
import android.graphics.Color
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import com.example.android.eyebody.R
import kotlinx.android.synthetic.main.list_image_select.view.*

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
        imageSelectView.adapter = ImageSelectAdapter(activity, collage.photoList, collage.selectedIndexList, this)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_image_select, menu)
        this.menu = menu

        menu.findItem(R.id.action_edit_image).setVisible(false)
        menu.findItem(R.id.action_share).setVisible(false)

        if(collage.selectedIndexList.size > 0){    //선택한 이미지가 하나 이상일 때 이미지편집 메뉴 아이콘 보여주기
            menu.findItem(R.id.action_edit_image).setVisible(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit_image -> {
                //ImageEditFragment로 교체
                var imageEditFragment = ImageEditFragment()

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
        var cnt = collage.selectedIndexList.size + 1

        if(isSelected){
            if(cnt > 5){
                Toast.makeText(activity, "5개까지만", Toast.LENGTH_SHORT).show()
                return
            }

            itemView.setBackgroundColor(Color.WHITE)
            itemView.date.setTextColor(R.color.gradientPurple)
            itemView.numberTextView.text = cnt.toString()
            itemView.numberTextView.visibility = View.VISIBLE

            if(!collage.selectedIndexList.contains(pos)) collage.selectedIndexList.add(pos)  //없으면 추가

            if(collage.selectedIndexList.size > 0){    //선택한 이미지가 하나 이상일 때 이미지편집 메뉴 아이콘 보여주기
                menu.findItem(R.id.action_edit_image).setVisible(true)
            }
        } else {
            itemView.setBackgroundColor(Color.TRANSPARENT)
            itemView.date.setTextColor(Color.WHITE)
            itemView.numberTextView.visibility = View.INVISIBLE

            collage.selectedIndexList.remove(pos)

            if(collage.selectedIndexList.size == 0){    //선택한 이미지가 하나도 없을 때 이미지편집 메뉴 아이콘 숨기기
                try{
                    menu.findItem(R.id.action_edit_image).setVisible(false)
                } catch (e: Exception) {
                    //메뉴가 null일 때
                }
            }
        }
    }
}