package com.dhruv.angularapps.data.models

import com.dhruv.angularapps.data.BR3
import com.dhruv.angularapps.data.BR4
import com.dhruv.angularapps.data.NILL

data class Group(
    val key: String = "",
    val name: String,
    val apps: List<String>
){
    companion object{
        fun Group.toStr (): String{
            return key + BR4 + name + BR4 + if (apps.isEmpty()) NILL else apps.joinToString(BR3) { it }
        }

        fun fromStr (value: String) : Group {

            val groupValues = value.split(BR4)
            val apps = if (groupValues[2] == NILL) emptyList() else groupValues[2].split(BR3).map { it }
            return Group(
                key = groupValues[0],
                name = groupValues[1],
                apps = apps
            )
        }
    }
}