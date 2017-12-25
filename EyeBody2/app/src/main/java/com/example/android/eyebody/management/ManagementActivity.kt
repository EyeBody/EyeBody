package com.example.android.eyebody.management

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import com.example.android.eyebody.R
import com.example.android.eyebody.camera.CameraActivity
import com.example.android.eyebody.gallery.GalleryActivity
import com.example.android.eyebody.management.config.ConfigManagementFragment
import com.example.android.eyebody.management.exercise.ExerciseManagementFragment
import com.example.android.eyebody.management.food.FoodManagementFragment
import com.example.android.eyebody.management.main.MainManagementFragment
import kotlinx.android.synthetic.main.activity_management.*
import android.content.ComponentName
import android.app.ActivityManager.RunningTaskInfo
import android.content.Context.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.content.Context


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

    val originScaleX by lazy { if (buttonToMain.scaleX == buttonToExercise.scaleX) buttonToMain.scaleX else buttonToConfig.scaleX }
    val originScaleY by lazy { if (buttonToMain.scaleY == buttonToExercise.scaleY) buttonToMain.scaleY else buttonToConfig.scaleY }
    val activateActionbar by lazy {
        getSharedPreferences(getString(R.string.getSharedPreference_configuration_Only_Int), Context.MODE_PRIVATE)
                .getInt(getString(R.string.sharedPreference_activateActionbar), 0)
    }

    override fun onFragmentInteraction(uri: Uri) {
        Toast.makeText(this, "$uri", Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> Toast.makeText(this, "home button clicked", Toast.LENGTH_LONG).show()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "Create")
        if (activateActionbar == 1)
            supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
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
        if (customView?.parent is Toolbar?) {
            val toolbar: Toolbar? = customView?.parent as Toolbar?
            toolbar?.setContentInsetsAbsolute(0, 0)
        } else if (customView?.parent is android.widget.Toolbar?) {
            val toolbar: android.widget.Toolbar? = customView?.parent as android.widget.Toolbar?
            toolbar?.setContentInsetsAbsolute(0, 0)
        }
        customView?.findViewById<ImageView>(R.id.goto_camera)?.setOnClickListener {
            val mIntent = Intent(this, CameraActivity::class.java)
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
                                BUTTON_TAG_EXERCISE -> ExerciseManagementFragment.newInstance(BUTTON_TAG_EXERCISE)
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
        val buttonArray = arrayOf(buttonToMain, buttonToExercise, buttonToFood, buttonToConfig)
        val minX = originScaleX * 0.8f
        val minY = originScaleY * 0.8f

        for (i in 0 until 4) {
            buttonArray[i].isSelected = (position == i)
            if (position == i) {
                buttonArray[i].scaleX = minX
                buttonArray[i].scaleY = minY
                buttonArray[i].alpha = 0.5f
                buttonArray[i].animate()
                        .scaleX(originScaleX)
                        .scaleY(originScaleY)
                        .alpha(1f)
                        .setDuration(300)
                        .start()
            } else {
                buttonArray[i].animate().cancel()
                buttonArray[i].scaleX = minX
                buttonArray[i].scaleY = minY
                buttonArray[i].alpha = 0.5f
            }
        }
    }
}