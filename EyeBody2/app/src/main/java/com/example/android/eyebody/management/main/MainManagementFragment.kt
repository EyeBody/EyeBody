package com.example.android.eyebody.management.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            pageNumber = arguments.getInt(ARG_PAGE_NUMBER)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // TODO 재사용같은거 할 수 있나 (나중에 memory 밖으로 벗어날때??)
        val mView = inflater!!.inflate(R.layout.fragment_management_main, container, false)

        val listview: ListView = mView.findViewById(R.id.listview_main_management)

        contents = MainManagementContent.getMainManagementContents(context)
        if (contents != null) {
            listview.adapter = MainManagementAdapter(context, contents!!)

            //for log
            for(content in contents!!){
                Log.d(TAG, "content : ${content.isInProgress} , ${content.startDate}, ${content.endDate}, ${content.startWeight}, ${content.endWeight}, ${content.DataList}")
            }
        }
        else{
            listview.adapter = MainManagementAdapter(context, arrayListOf())
        }

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
