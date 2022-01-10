package com.example.firebasestudents.model

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class Student(

    var index: String? = "",
    var name: String? = "",
    var surname: String? = "",
    var phoneNumber: String? = "",
    var address: String? = ""

)
