package com.jobsforher.models

class CityView {
    var id: Int = 0
    var name: String? = null


    constructor() {}

    constructor(
        id: Int, name: String) {

        this.id = id
        this.name = name

    }
}