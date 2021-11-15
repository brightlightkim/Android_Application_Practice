package com.example.familymap

import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean)
    //textResId: hold the resource ID (always in int)
    //compiler does the extra work for data classes such as equals and hashCode and toString

