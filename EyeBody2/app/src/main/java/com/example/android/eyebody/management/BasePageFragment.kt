package com.example.android.eyebody.management

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by YOON on 2017-11-11.
 * 정석대로 해보려고 기본으로 제공하는 Fragment를 생성해보았음.
 */
open class BasePageFragment : Fragment(){

    protected var pageNumber: Int? = null

    private var mFragmentInteractionListener: OnFragmentInteractionListener? = null

    fun onButtonPressed(uri: Uri) {
        if (mFragmentInteractionListener != null) {
            mFragmentInteractionListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        throw RuntimeException("fragment must override onCreateView return not null")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mFragmentInteractionListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mFragmentInteractionListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }
}
