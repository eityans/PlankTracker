package com.example.planktracker

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Plank : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var date: Date = Date()
    var sec: Int = 0
    var memo: String = ""
}