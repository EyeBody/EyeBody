package com.example.android.eyebody.management

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.android.eyebody.R
import com.example.android.eyebody.camera.CameraActivity
import com.example.android.eyebody.gallery.GalleryActivity
import com.example.android.eyebody.management.config.ConfigManagementFragment
import com.example.android.eyebody.management.gallery.GalleryManagementFragment
import com.example.android.eyebody.management.food.FoodManagementFragment
import com.example.android.eyebody.management.main.MainManagementFragment
import kotlinx.android.synthetic.main.activity_management.*

/**
 * Created by YOON on 2017-11-10
 */
class ManagementActivity : AppCompatActivity(), BasePageFragment.OnFragmentInteractionListener {

    private val TAG = "mydbg_manage"

    private val buttonToMain by lazy { management_button_main_management }
    private val buttonToExercise by lazy { management_button_exercise_management }
    private val buttonToFood by lazy { management_button_food_management }
    private val buttonToConfig by lazy { management_button_config_management }

    private val BUTTON_TAG_MAIN = 0
    private val BUTTON_TAG_EXERCISE = 1
    private val BUTTON_TAG_FOOD = 2
    private val BUTTON_TAG_CONFIG = 3

    override fun onFragmentInteraction(uri: Uri) {
        Toast.makeText(this, "$uri", Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> Toast.makeText(this, "home button clicked", Toast.LENGTH_LONG).show()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)

        /* actionbar, statusbar 연습 */
        //supportActionBar?.hide()
        //supportActionBar?.title = "Main"
        //supportActionBar?.subtitle = "메인관리창"
        //supportActionBar?.setBackgroundDrawable(getDrawable(R.drawable.samplebackground))
        val actionbar = supportActionBar
        actionbar?.setDisplayShowCustomEnabled(true)
        actionbar?.setDisplayShowTitleEnabled(false)
        actionbar?.setDisplayHomeAsUpEnabled(false)
        actionbar?.setDisplayShowHomeEnabled(false)
        actionbar?.setDisplayUseLogoEnabled(false)
        actionbar?.setCustomView(R.layout.actionbar_management)
        val customView = actionbar?.customView
        if(customView?.parent is Toolbar?) {
            val toolbar: Toolbar? = customView?.parent as Toolbar?
            toolbar?.setContentInsetsAbsolute(0, 0)
        } else if (customView?.parent is android.widget.Toolbar?){
            val toolbar: android.widget.Toolbar? = customView?.parent as android.widget.Toolbar?
            toolbar?.setContentInsetsAbsolute(0, 0)
        }
        customView?.findViewById<ImageView>(R.id.goto_camera)?.setOnClickListener {
            val mIntent = Intent(this,CameraActivity::class.java)
            startActivity(mIntent)
        }
        customView?.findViewById<ImageView>(R.id.goto_gallery)?.setOnClickListener {
            val mIntent = Intent(this, GalleryActivity::class.java)
            startActivity(mIntent)
        }

        window.statusBarColor = 0x7f100030 //0xAARRGGBB ~ format(7f ff ff ff) ~ (127, 255, 255, 255) ~ alpha 는 0이 투명 , resources.getColor(R.color.colorAccent)

        /* end of practices */


        val viewpager = management_viewpager
        val dotIndicator = management_dot_indicator

        // 점으로 탭 표시하는거
        dotIndicator.setupWithViewPager(viewpager)

        viewpager.offscreenPageLimit = 3

        // 현재 아이템은 (메인관리)이며 각각의 번호를 부여함.
        viewpager.currentItem = BUTTON_TAG_MAIN
        mappingButtonSelected(BUTTON_TAG_MAIN)

        buttonToMain.tag = BUTTON_TAG_MAIN
        buttonToExercise.tag = BUTTON_TAG_EXERCISE
        buttonToFood.tag = BUTTON_TAG_FOOD
        buttonToConfig.tag = BUTTON_TAG_CONFIG


        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                /*
                state
                 0 -> idle
                 1 -> dragging
                 2 -> settling
                */
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                /* */
            }
            override fun onPageSelected(position: Int) {
                mappingButtonSelected(position)
            }
        })

        // 버튼을 클릭하면 viewpager의 currentItem 을 바꿔줌.
        val buttonToPageChangeListener = View.OnClickListener { view ->
            viewpager.currentItem = view.tag as Int
            mappingButtonSelected(view.tag as Int)
        }
        buttonToMain.setOnClickListener(buttonToPageChangeListener)
        buttonToExercise.setOnClickListener(buttonToPageChangeListener)
        buttonToFood.setOnClickListener(buttonToPageChangeListener)
        buttonToConfig.setOnClickListener(buttonToPageChangeListener)


        // getStoredInt : currentItem에 따라서 반환하는 Fragment를 각각 지정해줌.
        // getCount : 4개의 아이템만 사용할 것임.
        viewpager.adapter =
                object : FragmentStatePagerAdapter(supportFragmentManager) {
                    override fun getItem(buttonTag: Int) = // fragmentAdapter's position = buttonTag
                            when (buttonTag) {
                                BUTTON_TAG_MAIN -> MainManagementFragment.newInstance(BUTTON_TAG_MAIN)
                                BUTTON_TAG_EXERCISE -> GalleryManagementFragment.newInstance(BUTTON_TAG_EXERCISE)
                                BUTTON_TAG_FOOD -> FoodManagementFragment.newInstance(BUTTON_TAG_FOOD)
                                BUTTON_TAG_CONFIG -> ConfigManagementFragment.newInstance(BUTTON_TAG_CONFIG)
                                else -> {
                                    // TODO - 이 에러메시지는 getItem이 아닌 보이는 뷰에 대해서 해야함. 로딩은 좌우 둘다 하므로 (현재는 모든 화면 로딩 상태)
                                    Log.e(TAG, "getStoredInt in FragmentStatePageAdapter return not in 0,1,2,3 !!")
                                    Toast.makeText(applicationContext, "something wrong in viewpager.adapter", Toast.LENGTH_LONG).show()
                                    MainManagementFragment.newInstance(BUTTON_TAG_MAIN)
                                }
                            }

                    override fun getCount() =
                            4
                }
    } // end of onCreate

    private fun mappingButtonSelected(position: Int) {
        for (i in 0..4)
            when (i) {
                BUTTON_TAG_MAIN -> buttonToMain.isSelected = (position == i)
                BUTTON_TAG_EXERCISE -> buttonToExercise.isSelected = (position == i)
                BUTTON_TAG_FOOD -> buttonToFood.isSelected = (position == i)
                BUTTON_TAG_CONFIG -> buttonToConfig.isSelected = (position == i)
            }
    }
}