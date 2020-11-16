package com.jobsforher.models

class Months {

    var monthsId: Int = 0
    var months: String? = null


    constructor() {}

    constructor(monthsId: Int, months: String) {

        this.monthsId = monthsId
        this.months = months

    }
}
