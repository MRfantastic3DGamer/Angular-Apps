package com.dhruv.angularapps.data.models

import com.dhruv.angularapps.data.BR2

data class AppData (
    var packageName : String = "",
    var name : String = "",
){
    companion object{
        fun AppData.toStr (): String{
            return packageName + BR2 + name
        }

        fun fromStr (value: String) : AppData {
            val appValues = value.split(BR2)
            return AppData(
                packageName = appValues[0],
                name = appValues[1]
            )
        }
    }
}