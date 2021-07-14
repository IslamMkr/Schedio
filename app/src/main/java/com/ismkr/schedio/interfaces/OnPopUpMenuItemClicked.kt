package com.ismkr.schedio.interfaces

import com.ismkr.schedio.models.Action

interface OnPopUpMenuItemClicked {

    fun onMenuItemClicked(position: Int, action: Action)

}