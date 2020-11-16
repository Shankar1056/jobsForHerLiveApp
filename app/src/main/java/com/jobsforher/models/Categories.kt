package com.jobsforher.models

class Categories {
    var category_id: Int = 0
    var category: String? = null

    constructor() {}

    constructor(category_id: Int, category: String) {
        this.category_id = category_id
        this.category = category

    }
}