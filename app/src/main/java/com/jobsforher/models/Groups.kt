package com.jobsforher.models

class Groups {
    var id: Int = 0
    var text: String? = null
    var image: String?=null

    constructor() {}

    constructor(id: Int, text: String, image: String) {
        this.id = id
        this.text = text
        this.image = image
    }
}