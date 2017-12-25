package com.example.android.eyebody.gallery

import android.os.Bundle
import android.app.Fragment
import android.graphics.Color
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
        imageSelectView.scrollToPosition(collage.pos)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_image_select, menu)
        this.menu = menu

        menu.findItem(R.id.action_edit_image).setVisible(true)
        menu.findItem(R.id.action_share).setVisible(false)

        //갤러리에서 선택한 이미지
        if(collage.selectedIndexList.size == 0){
            collage.selectedIndexList.add(collage.pos)
            imageSelectView.adapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit_image -> {
                //ImageEditFragment로 교체
                var imageEditFragment = ImageEditFragment()
                //var imageEditFragment = ImageCollageFragment()

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, imageEditFragment)
                        .addToBackStack(null)
                        .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun itemViewClicked(itemView: View, pos: Int){
        var cnt = collage.selectedIndexList.size + 1
        var isSelected = collage.selectedIndexList.contains(pos)

        if(cnt > 5 && !isSelected){ //꽉 찼는데 더 추가하려고 할 때
            Toast.makeText(activity, "5개까지만 선택할 수 있습니다", Toast.LENGTH_SHORT).show()
        } else{
            setSelected(itemView, pos, !isSelected)   //없으면(false) 추가해줌(true)

        }
    }

    fun setSelected(itemView: View, pos: Int, isSelected: Boolean){
        if(isSelected){
            itemView.setBackgroundColor(Color.WHITE)
            itemView.date.setTextColor(R.color.eyebodyPurple)

            var idx = collage.selectedIndexList.indexOf(pos)
            if(idx < 0){
                itemView.numberTextView.text = (collage.selectedIndexList.size + 1).toString()  //새로 선택하는 경우
            }else{
                itemView.numberTextView.text = (idx + 1).toString() //뒤로가기 했을 때(이미 값이 있는 경우)
            }

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

            //선택 해제했을 때 번호 다시 매기기
            try {
                for (idx in collage.selectedIndexList.indices) {
                    var sel = collage.selectedIndexList[idx]
                    imageSelectView.findViewHolderForAdapterPosition(sel).itemView.numberTextView.text = (idx + 1).toString()
                }
            } catch(e: Exception){
                //초기화되지 않은 경우
            }

            if(collage.selectedIndexList.size == 0){    //선택한 이미지가 하나도 없을 때 이미지편집 메뉴 아이콘 숨기기
                try{
                    menu.findItem(R.id.action_edit_image).setVisible(false)
                } catch (e: Exception) {
                    //메뉴가 null일 때(초기화가 안됐을 때)
                }
            }
        }
    }
}