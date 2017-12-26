package com.example.android.eyebody.management.main

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ListView
import com.example.android.eyebody.R
import com.example.android.eyebody.management.BasePageFragment
import kotlinx.android.synthetic.main.fragment_management_main.*

/**
 * Created by YOON on 2017-11-11
 */
class MainManagementFragment : BasePageFragment() {

    private val TAG = "mydbg_MainTabFrag"
    private var contents: ArrayList<MainManagementContent>? = null
    private var listviewGlobal: ListView? = null
    private var listviewStatus: Parcelable? = null

    val contentOriginHeight by lazy { activity.findViewById<ConstraintLayout>(R.id.managementWholeConstraint).height }
    val originButtonTop by lazy { addButton.top }
    val activateActionbar by lazy {
        activity
            .getSharedPreferences(getString(R.string.getSharedPreference_configuration_Only_Int),MODE_PRIVATE)
            .getInt(getString(R.string.sharedPreference_activateActionbar),0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            pageNumber = arguments.getInt(ARG_PAGE_NUMBER)
        }
    }

    override fun onPause() {
        super.onPause()
        listviewStatus = listviewGlobal?.onSaveInstanceState()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO 재사용같은거 할 수 있나 (나중에 memory 밖으로 벗어날때??)
        val mView = inflater!!.inflate(R.layout.fragment_management_main, container, false)
        listviewGlobal = mView.findViewById(R.id.listview_main_management)
        val listview: ListView = listviewGlobal ?: return mView

        contents = MainManagementContent.getMainManagementContentArrayListForAdapterFromJson(context)
        if (contents != null) {
            listview.adapter = MainManagementAdapter(context, contents!!)

            //for log
            for (content in contents!!) Log.d(TAG, "content : ${content.isInProgress} , ${content.startDate}, ${content.desireDate}, ${content.startWeight}, ${content.desireWeight}, ${content.dateDataList}")
        } else {
            listview.adapter = MainManagementAdapter(context, arrayListOf())
        }

        if (listviewStatus != null) {
            listview.onRestoreInstanceState(listviewStatus)
        }

        listview.setOnScrollListener(object : AbsListView.OnScrollListener {
            var oldUnitPositionY = 0
            var oldVisibleItemPos = 0
            var isUpScroll: Boolean? = null

            fun scrollCheck(){
                val unitPositionY = -listview.getChildAt(0).top
                val visibleItemPos = listview.getPositionForView(listview.getChildAt(0))

                if (oldVisibleItemPos < visibleItemPos) {
                    isUpScroll = true
                } else if (oldVisibleItemPos > visibleItemPos) {
                    isUpScroll = false
                } else {
                    if (oldUnitPositionY < unitPositionY) {
                        isUpScroll = true
                    } else if (oldUnitPositionY > unitPositionY) {
                        isUpScroll = false
                    }
                }
                oldVisibleItemPos = visibleItemPos
                oldUnitPositionY = unitPositionY
            }

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (listview.childCount >= 1) {
                    scrollCheck()
                    when (isUpScroll) {
                        true -> {
                            addButton.alpha -= 0.05f
                            if (addButton.alpha < 0.3f)
                                addButton.visibility = View.GONE
                            if (addButton.alpha < 0)
                                addButton.alpha = 0f
                        }
                        false -> {
                            addButton.alpha += 0.1f
                            if (addButton.alpha >= 0.3f)
                                addButton.visibility = View.VISIBLE
                            if (addButton.alpha > 1f)
                                addButton.alpha = 1f
                        }
                    }


                    if(activateActionbar==1) {
                        val actionbar = (activity as AppCompatActivity).supportActionBar
                        val content = activity.findViewById<ConstraintLayout>(R.id.managementWholeConstraint)
                        if (actionbar != null) {
                            content.top = actionbar.height - actionbar.hideOffset
                        }

                        when(isUpScroll){
                            true -> {
                                if (actionbar != null) {
                                    actionbar.hideOffset += 15//(actionbar.height*0.05f).toInt()
                                    if (actionbar.hideOffset > actionbar.height)
                                        actionbar.hideOffset = actionbar.height
                                    content.top = actionbar.height - actionbar.hideOffset
                                }
                            }
                            false -> {
                                if (actionbar != null) {
                                    actionbar.hideOffset = 0// (actionbar.height*0.1f).toInt()
                                    if (actionbar.hideOffset < 0)
                                        actionbar.hideOffset = 0
                                    content.top = actionbar.height - actionbar.hideOffset
                                }
                            }
                        }
                    }
                }
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            }

        })

        return mView
    }

    override fun onStart() {
        super.onStart()
        addButton.setOnClickListener {
            val mInt = Intent(context, NewMainContentActivity::class.java)
            startActivity(mInt)
        }
    }

    companion object {
        private val ARG_PAGE_NUMBER = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param pn PageNumber (Int)
         * @return A new instance of fragment MainManagementFragment.
         */
        fun newInstance(pn: Int): MainManagementFragment {
            val fragment = MainManagementFragment()
            val args = Bundle()
            args.putInt(ARG_PAGE_NUMBER, pn)
            fragment.arguments = args
            return fragment
        }

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            /* fragment가 visible 해질 때 action을 부여하고 싶은 경우 */
        } else {
            /* fragment가 visible 하지 않지만 load되어 있는 경우 */
        }
    }


}// Required empty public constructor
