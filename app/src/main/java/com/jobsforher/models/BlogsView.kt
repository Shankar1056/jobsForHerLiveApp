package com.jobsforher.models

class BlogsView {

    var author_name: String? = null
    var post_for: String? = null
    var company_id: Int? = null
    var slug: String? = null
    var city: String? = null
    var title: String? = null
    var id: Int? = null
    var blog_view: String? = null
    var created_on: String? = null
    var created_by: Int? = null
    var blog_share: String? = null
    var publish_date: String? = null
    var category: String? = null
    var status: String? = null
    var blog_link: String? = null
    var modified_id: String? = null
    var blog_like: String? = null
    var excerpt: String? = null
    var author_id: Int? = null
    var admin_id: String? = null
    var description: String? = null
    var modified_on: String? = null
    var show_register_message: Boolean? = null
    var image_url: String?= null


    constructor() {}

    constructor(author_name: String, post_for: String, company_id: Int,slug: String, city: String, title: String,
                id:Int, blog_view:String,created_on: String, created_by: Int, blog_share: String,publish_date: String,
                category: String, status: String,blog_link: String, modified_id: String, blog_like: String,excerpt: String,
                author_id: Int, admin_id: String,description: String, modified_on: String, show_register_message: Boolean,image_url: String) {

        this.author_name = author_name
        this.post_for = post_for
        this.company_id= company_id
        this.slug = slug
        this.city = city
        this.title = title
        this.id = id
        this.blog_view = blog_view
        this.created_on = created_on
        this.created_by = created_by
        this.blog_share = blog_share
        this.publish_date = publish_date

        this.category = category
        this.status = status
        this.blog_link = blog_link
        this.modified_id = modified_id
        this.blog_like = blog_like
        this.excerpt = excerpt

        this.author_id =author_id
        this.admin_id = admin_id
        this.description = description
        this.modified_on = modified_on
        this.show_register_message = show_register_message
        this.image_url= image_url
    }
}