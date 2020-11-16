package com.jobsforher.models


class Colleges {

    var status: Boolean = true
    var id: Int = 0
    var college: String? = null


    constructor() {}

    constructor(status: Boolean, id: Int, college: String) {

        this.status = status
        this.id = id
        this.college = college

    }
}