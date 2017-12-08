package com.example.android.eyebody.management

import android.content.Context
import android.widget.BaseAdapter


abstract class BasePageAdapter(val context: Context, val contents: Array<Any>) : BaseAdapter() {

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = contents.size
}
