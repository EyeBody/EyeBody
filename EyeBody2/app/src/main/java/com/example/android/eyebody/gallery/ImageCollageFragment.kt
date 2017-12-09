package com.example.android.eyebody.gallery

import android.app.Fragment
import android.os.Bundle
import android.view.*
import com.example.android.eyebody.R

/**
 * Created by Yeji on 2017-12-09.
 */
class ImageCollageFragment : Fragment() {
    lateinit var collage: CollageActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        collage = activity as CollageActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_image_collage, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_image_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_image_save -> {
                //이미지 저장하기
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
