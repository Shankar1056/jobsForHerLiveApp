package com.jobsforher.models

class JobTypeView {
    var image: String? = null
    var name: String? = null


    constructor() {}

    constructor(
        name: String, image:String) {

        this.image = image
        this.name = name

    }
}