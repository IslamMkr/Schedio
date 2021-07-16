package com.ismkr.schedio.interfaces

import com.ismkr.schedio.models.Activity

interface OnItemClicked {

    fun itemClicked(position: Int, activity: Activity)

}