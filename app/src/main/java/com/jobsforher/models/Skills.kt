package com.jobsforher.models

class Skills {

    var approve: Boolean = true
    var id: Int = 0
    var category_type: String? = null
    var name: String? = null


    constructor() {}

    constructor(approve: Boolean, id: Int, category_type: String,name: String) {

        this.approve = approve
        this.id = id
        this.category_type = category_type
        this.name = name

    }

    constructor(name: String) {

        this.name = name

    }


}
