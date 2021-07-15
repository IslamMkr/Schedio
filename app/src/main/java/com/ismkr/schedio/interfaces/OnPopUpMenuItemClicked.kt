package com.ismkr.schedio.interfaces

import com.ismkr.schedio.models.Action

interface OnPopUpMenuItemClicked {

    fun menuItemClicked(position: Int, action: Action)

}