package com.jobsforher.models

class Degrees {

    var status: Boolean = true
    var id: Int = 0
    var sDegree: String? = null


    constructor() {}

    constructor(status: Boolean, id: Int, degree: String) {

        this.status = status
        this.id = id
        this.sDegree = degree

    }
}
