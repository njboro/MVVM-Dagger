package com.example.app_mvvm_compose.utils

import com.example.app_mvvm_compose.model.Desh

fun getDummyList() : List<Desh> {
    return listOf(
        Desh("India", capital = "New Delhi", flag = "https://raw.githubusercontent.com/DevTides/countries/master/ind.png"),
        Desh("China", capital = "Beijing", flag = "https://raw.githubusercontent.com/DevTides/countries/master/mac.png"),
        Desh("Pakistan", capital = "Islamabad", flag = "https://raw.githubusercontent.com/DevTides/countries/master/pak.png"),
    )
}