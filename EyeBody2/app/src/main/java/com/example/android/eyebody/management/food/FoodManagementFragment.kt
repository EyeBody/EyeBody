package com.example.android.eyebody.management.food

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.example.android.eyebody.R
import com.example.android.eyebody.management.BasePageFragment

/**
 * Created by YOON on 2017-11-11
 *
 * aka sms management
 */
class FoodManagementFragment : BasePageFragment() {

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            /* fragment가 visible 해질 때 action을 부여하고 싶은 경우 */
        } else {
            /* fragment가 visible 하지 않지만 load되어 있는 경우 */
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater!!.inflate(R.layout.fragment_management_food, container, false)

        val dbHelper = DbHelper(context, "bill.db", null, 1)
        val result = dbHelper.getResult()

        Log.d("sss", result.size.toString())
        val contents = arrayListOf<FoodManagementContent>()
        for(x in result){
            val list = x.split("/")
            contents.add(FoodManagementContent(list))
            Log.d("sss", x)
        }

        val listview : ListView = mView.findViewById(R.id.listview_food_management)
        listview.adapter = FoodManagementAdapter(context, contents)

        return mView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            pageNumber = arguments.getInt(ARG_PAGE_NUMBER)
        }
    }

    companion object {
        private val ARG_PAGE_NUMBER = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param pn PageNumber (Int)
         * @return A new instance of fragment FoodManagementFragment.
         */
        fun newInstance(pn : Int) : FoodManagementFragment {
            val fragment = FoodManagementFragment()
            val args = Bundle()
            args.putInt(ARG_PAGE_NUMBER, pn)
            fragment.arguments = args
            return fragment
        }

    }

}// Required empty public constructor
