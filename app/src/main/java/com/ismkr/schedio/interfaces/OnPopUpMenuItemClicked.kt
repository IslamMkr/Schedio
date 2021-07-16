package com.ismkr.schedio.interfaces

import com.ismkr.schedio.models.Action
import com.ismkr.schedio.models.Activity

interface OnPopUpMenuItemClicked {

    fun menuItemClicked(position: Int, activity: Activity, action: Action)

}