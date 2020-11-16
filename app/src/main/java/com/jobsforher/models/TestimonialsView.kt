package com.jobsforher.models

class TestimonialsView {

    var id: Int? = null
    var testimonial: String? = null
    var created_on: String? = null
    var designation: String? = null
    var company_name: String? = null
    var name: String? = null
    var created_by: Int? = null
    var modified_on: String? = null
    var company_id: Int? = null
    var image_icon: String? = null


    constructor() {}

    constructor(id:Int, testimonial: String, created_on: String, designation: String, company_name: String,
                name:String,created_by: Int, modified_on: String,company_id: Int, image_icon: String) {

        this.id = id
        this.testimonial = testimonial
        this.created_on = created_on
        this.designation= designation
        this.company_name = company_name
        this.name = name
        this.created_by = created_by
        this.modified_on = modified_on
        this.company_id = company_id
        this.image_icon = image_icon
    }
}