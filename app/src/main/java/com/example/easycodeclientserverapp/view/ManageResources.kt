package com.example.easycodeclientserverapp.view

import android.content.Context
import androidx.annotation.StringRes

interface ManageResources {

    fun string(@StringRes resourceId: Int) : String

    class Base(private val context: Context) : ManageResources {
        override fun string(resourceId: Int) = context.getString(resourceId)
    }

}