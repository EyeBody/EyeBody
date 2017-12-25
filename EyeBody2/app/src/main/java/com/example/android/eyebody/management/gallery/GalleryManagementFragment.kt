package com.example.android.eyebody.management.gallery

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityOptionsCompat
import java.io.File
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast

import com.example.android.eyebody.R
import com.example.android.eyebody.gallery.CollageActivity
import com.example.android.eyebody.gallery.Photo
import com.example.android.eyebody.gallery.PhotoFrameActivity
import com.example.android.eyebody.management.BasePageFragment

/**
 * Created by YOON on 2017-11-11
 * Modified by Yeji on 2017-12-24
 */
class GalleryManagementFragment : BasePageFragment() {
    var photoList = ArrayList<Photo>()
    lateinit var galleryView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            pageNumber = arguments.getInt(ARG_PAGE_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater!!.inflate(R.layout.fragment_management_gallery, container, false)
        galleryView = view.findViewById(R.id.galleryView)

        //photoList에 값 채워넣기(이미지 불러오기)
        LoadPhotos()

        //RecyclerView
        galleryView.hasFixedSize()
        galleryView.adapter = GalleryManagementAdapter(activity, this, photoList)

        return view
    }

    companion object {
        private val ARG_PAGE_NUMBER = "param1"

        fun newInstance(pn : Int) : GalleryManagementFragment {
            val fragment = GalleryManagementFragment()
            val args = Bundle()
            args.putInt(ARG_PAGE_NUMBER, pn)
            fragment.arguments = args
            return fragment
        }
    }

    fun LoadPhotos(){
        var state: String = Environment.getExternalStorageState()   //외부저장소(SD카드)가 마운트되었는지 확인
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //디렉토리 생성
            var filedir: String = activity.getExternalFilesDir(null).toString() + "/gallery_body"  //Android/data/com.example.android.eyebody/files/gallery_body
            var file: File = File(filedir)
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    //EXCEPTION 디렉토리가 만들어지지 않음
                }
            }

            for (f in file.listFiles()) {
                //TODO 이미지 파일이 아닌경우 예외처리
                //TODO 이미지를 암호화해서 저장해놓고 불러올 때만 복호화 하기
                photoList.add(Photo(f))
            }
        } else{
            //TODO EXCEPTION 외부저장소가 마운트되지 않아서 파일을 읽고 쓸 수 없음
        }
    }

    fun itemViewClicked(itemView: View, pos: Int){
        var intent = Intent(activity, PhotoFrameActivity::class.java)
        intent.putExtra("photoList", photoList)
        intent.putExtra("pos", pos)

        startActivity(intent)
    }

    fun itemViewLongClicked(itemView: View, pos: Int): Boolean{
        var intent = Intent(activity, CollageActivity::class.java)
        intent.putExtra("photoList", photoList)
        intent.putExtra("pos", pos)

        //공유 요소 전환
        //var options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, galleryView, "galleryView")
        startActivity(intent)

        return true
    }
}