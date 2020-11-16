package com.jobsforher.models

class LifeExperiences {

    var status: Boolean = true
    var name: String? = null
    var id: Int = 0

    constructor() {}

    constructor(status: Boolean, name: String, id: Int) {

        this.status = status
        this.name = name
        this.id = id

    }
}
