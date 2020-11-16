package com.jobsforher.models

class CompanyDetailsData {


    var industry:String?=null
    var view_count: Int?=null
    var banner_image: String? = null
    var website: String?=null
    var status:String?=null
    var cities: String?=null
    var deleted:Boolean?=null
    var sac_hsc_no:String?=null
    var created_on:String?=null
    var established_date:Int?=null
    var name:String?=null
    var company_profile_url: String?=null
    var id: Int = 0
    var company_profile_percentage:String?=null
    var team_size: String?=null
    var diversity: String?=null
    var culture: String?=null
    var created_by: Int?=null
    var featured: Boolean?=null
    var gstin_no: String?=null
    var modified_on: String?=null
    var about_us: String?=null
    var logo: String?=null
    var company_type: String? = null
    var featured_end_date: String?=null
    var active_jobs_count: Int?= null
    var locations: List<Location>?=null
    var policies: Policies?=null
    var jobs: List<Jobs>?=null
    var employers: List<Employers>?=null
    var images: CompanyImages?=null
    var followers: Int?=null
    var groups: List<CompanyGroups>? =null
    var videos: List<CompanyVideos>?=null
    var events: List<Events>?=null
    var testimonials: List<Testimonials>?=null
    var blogs: List<Blogs>?= null
    var link_companies_name: List<EventCompanies>?=null

    constructor() {}

    constructor(
        industry: String, view_count: Int,banner_image: String,website: String, status: String, cities:String, deleted: Boolean, sac_hsc_no: String, created_on: String,
        established_date: Int, name: String,company_profile_url: String,id: Int,company_profile_percentage: String,
        team_size: String,diversity: String,culture: String,created_by: Int,featured: Boolean,gstin_no: String,
        modified_on: String,about_us: String,logo: String,company_type: String,featured_end_date: String,active_jobs_count: Int,locations: List<Location>,policies: Policies,
        jobs: List<Jobs>,employers: List<Employers>,images: CompanyImages, followers: Int,groups: List<CompanyGroups>,
        videos: List<CompanyVideos>, events: List<Events>,testimonials: List<Testimonials>, blogs: List<Blogs>,
        link_companies_name:List<EventCompanies>) {

        this.link_companies_name = link_companies_name
        this.industry = industry
        this.view_count = view_count
        this.banner_image = banner_image
        this.website = website
        this.status = status
        this.cities = cities
        this.deleted = deleted
        this.sac_hsc_no = sac_hsc_no
        this.created_on = created_on
        this.established_date = established_date
        this.name = name
        this.company_profile_url = company_profile_url
        this.id = id
        this.company_profile_percentage = company_profile_percentage
        this.team_size = team_size
        this.diversity = diversity
        this.culture =culture
        this.created_by = created_by
        this.featured = featured
        this.gstin_no = gstin_no
        this.modified_on = modified_on
        this.about_us = about_us
        this.logo = logo
        this.company_type = company_type
        this.featured_end_date = featured_end_date
        this.active_jobs_count = active_jobs_count
        this.locations = locations
        this.policies = policies
        this.jobs=jobs
        this.employers = employers
        this.images = images
        this.followers = followers
        this.groups = groups
        this.videos = videos
        this.events = events
        this.testimonials = testimonials
        this.blogs = blogs
    }
}

class Location{

    var created_on: String? = null
    var hide_on_web: Boolean? = null
    var id: Int? = null
    var state_id: Int? = null
    var state_name: String? = null
    var city_id: Int? = null
    var city_name: String? = null
    var country_id: Int? = null
    var pincode: Int? = null
    var country_name: String? = null
    var location_type: String? = null
    var address_2: String? = null
    var company_id: Int? = null
    var address: String? = null
    var modified_on: String? = null

    constructor() {}

    constructor(created_on: String, hide_on_web: Boolean,id: Int, state_id: Int, state_name:String,city_id:Int, city_name:String,
                country_id:Int, pincode:Int, country_name:String, location_type:String, address_2:String,company_id:Int ,address:String,
                modified_on:String) {
        this.created_on = created_on
        this.hide_on_web = hide_on_web
        this.id = id
        this.state_id = state_id
        this.state_name = state_name
        this.city_id = city_id
        this.city_name = city_name
        this.country_id = country_id
        this.pincode = pincode
        this.country_name = country_name
        this.location_type = location_type
        this.address_2 = address_2
        this.company_id = company_id
        this.address = address
        this.modified_on = modified_on

    }
}

class Policies{

    var maternity: List<PolicyData>? = null
    var paternity: List<PolicyData>? = null
    var childcare: List<PolicyData>? = null
    var harassment: List<PolicyData>? = null
    var transportation: List<PolicyData>? = null
    var flexi: List<PolicyData>? = null
    var other: List<PolicyData>? = null
    var transport: List<PolicyData>? = null

    constructor() {}

    constructor(maternity: List<PolicyData>, paternity: List<PolicyData>, childcare:List<PolicyData>,harassment:List<PolicyData>,
                transportation:List<PolicyData>,flexi:List<PolicyData>, other:List<PolicyData>, transport:List<PolicyData>   ) {
        this.maternity = maternity
        this.paternity = paternity
        this.childcare = childcare
        this.harassment = harassment
        this.transportation = transportation
        this.flexi = flexi
        this.other = other
        this.transport = transport

    }
}

class PolicyData {

    var created_on: String? = null
    var modified_on: String? = null
    var description: String? = null
    var id: Int? = null
    var title: String? = null
    var company_id: Int? = null
    var policy_type: String? = null
    var image_url: String? = null

    constructor() {}

    constructor(
        created_on: String, modified_on: String, description: String, id: Int,
        title: String, company_id: Int, policy_type: String, image_url: String
    ) {
        this.created_on = created_on
        this.modified_on = modified_on
        this.description = description
        this.id = id
        this.title = title
        this.company_id = company_id
        this.policy_type = policy_type
        this.image_url = image_url
    }
}

class Jobs{

    var view_count: Int? = null
    var status: String? = null
    var location_id: Int?= null
    var boosted: Boolean?=null
    var min_year : Int?= null
    var created_on: String?= null
    var location_name: String? = null
    var resume_required: Boolean?= null
    var title: String? = null
    var id: Int = 0
    var specialization_id: Int? = null
    var user_id: Int? = null
    var max_year: Int? = null
    var company_id: Int? = null
    var modified_on: String? = null
    var redirect_url: String? = null
    var co_owners: String? = null
    var description: String? = null
    var published_on: String? = null
    var job_posting_type: String? = null
    var vacancy: String? = null
    var application_notification_type: String? = null
    var salary: String? = null
    var min_qualification: String? = null

    constructor() {}

    constructor( view_count: Int, status: String, location_id: Int, boosted: Boolean,min_year: Int,
                 created_on: String, location_name: String, resume_required: Boolean, title: String, id: Int,specialization_id: Int,
                 user_id: Int, max_year: Int, company_id: Int, modified_on: String, redirect_url: String,
                 co_owners: String, description: String, published_on: String, job_posting_type: String, vacancy: String, application_notification_type: String,
                 salary: String,min_qualification: String) {

        this.view_count = view_count
        this.status = status
        this.location_id =location_id
        this.boosted = boosted
        this.min_year = min_year
        this.created_on = created_on
        this.location_name = location_name
        this.resume_required = resume_required
        this.title  = title
        this.id = id
        this.specialization_id  =specialization_id
        this.user_id = user_id
        this.max_year = max_year
        this.company_id = company_id
        this.modified_on = modified_on
        this.redirect_url = redirect_url
        this.co_owners  =co_owners
        this.description = description
        this.published_on = published_on
        this.job_posting_type = job_posting_type
        this.vacancy = vacancy
        this.application_notification_type = application_notification_type
        this.salary = salary
        this.min_qualification = min_qualification
    }
}

class Employers{

    var designation: String? = null
    var created_on: String? = null
    var boost_count: Int?= null
    var employer_role: String?=null
    var id: Int = 0
    var share_job_access : Boolean?= null
    var notification_type: String?= null
    var package_job_count: Int? = null
    var email_verified: Int?= null
    var job_count: Int? = null
    var application_access: Boolean? = null
    var user_id: Int? = null
    var subscription: Int? = null
    var paid: Boolean? = null
    var deleted: Boolean? = null
    var transfer_job_access: Boolean? = null
    var employer_status: String? = null
    var company_id: Int? = null
    var modified_on: String? = null

    constructor() {}

    constructor( designation: String, created_on: String, boost_count: Int, employer_role: String,id: Int,
                 share_job_access: Boolean, notification_type: String, package_job_count: Int, email_verified: Int, job_count: Int,application_access: Boolean,
                 user_id: Int, subscription: Int, paid: Boolean, deleted: Boolean, transfer_job_access: Boolean,
                 employer_status: String, company_id: Int, modified_on: String) {

        this.designation = designation
        this.created_on = created_on
        this.boost_count = boost_count
        this.employer_role = employer_role
        this.id = id
        this.share_job_access = share_job_access
        this.notification_type = notification_type
        this.package_job_count = package_job_count
        this.email_verified = email_verified
        this.job_count = job_count
        this.application_access = application_access
        this.user_id = user_id
        this.subscription = subscription
        this.paid = paid
        this.deleted = deleted
        this.transfer_job_access = transfer_job_access
        this.employer_status = employer_status
        this.company_id = company_id
        this.modified_on = modified_on

    }
}

class CompanyImages{

    var about_us: List<ImagesData>? = null
    var featured_images: List<ImagesData>? = null
    var culture: List<ImagesData>? = null
    var diversity: List<ImagesData>? = null

    constructor() {}

    constructor(about_us:List<ImagesData>,featured_images: List<ImagesData>,culture: List<ImagesData>,diversity: List<ImagesData>){

        this.about_us = about_us
        this.featured_images = featured_images
        this.culture = culture
        this.diversity = diversity
    }

}

class ImagesData {

    var created_on: String? = null
    var modified_on: String? = null
    var id: Int? = null
    var image_category: String? = null
    var company_id: Int? = null
    var image_url: String? = null

    constructor() {}

    constructor(
        created_on: String, modified_on: String, id: Int,
        image_category: String, company_id: Int, image_url: String
    ) {

        this.created_on = created_on
        this.modified_on = modified_on
        this.id = id
        this.image_category = image_category
        this.company_id = company_id
        this.image_url = image_url
    }
}

class CompanyGroups{

    var id: Int? = null
    var name: String? = null
    var icon_url: String? = null
    var banner_url: String? = null
    var excerpt: String? = null
    var description: String? = null
    var group_type: String? = null
    var opened_to: String? = null
    var group_creation: String? = null
    var group_assocation: String? = null
    var created_by: Int? = null
    var visiblity_type: String? = null
    var no_of_members: Int? = null
    var notification: Boolean? = null
    var featured: Boolean? = null
    var feature_start_date: String? = null
    var feature_end_date: String? = null
    var status: String? = null
    var categories: String? = null
    var cities: String? = null
    var notes: String? = null
    var phone_number: String? = null
    var external_group: Int? = null
    var created_on: String? = null
    var modified_on: String? = null

    constructor() {}

    constructor(
        id: Int, name: String, icon_url: String,banner_url: String, excerpt: String, description: String,
        group_type: String, opened_to: String, group_creation: String,group_assocation: String, created_by: Int, visiblity_type: String,
        no_of_members: Int, notification: Boolean, featured: Boolean,feature_start_date: String, feature_end_date: String, status: String,
        categories: String, cities: String, notes: String,phone_number: String, external_group: Int, created_on: String,modified_on:String){

        this.id = id
        this.icon_url =icon_url
        this.banner_url = banner_url
        this.excerpt = excerpt
        this.description = description
        this.group_type = group_type
        this.opened_to = opened_to
        this.group_creation = group_creation
        this.group_assocation = group_assocation
        this.created_by = created_by
        this.visiblity_type = visiblity_type
        this.no_of_members = no_of_members
        this.notification = notification
        this.featured = featured
        this.feature_start_date = feature_start_date
        this.feature_end_date = feature_end_date
        this.status = status
        this.categories = categories
        this.cities =cities
        this.notes= notes
        this.phone_number = phone_number
        this.external_group = external_group
        this.created_on = created_on
        this.modified_on = modified_on

    }
}

class CompanyVideos {

    var created_on: String? = null
    var name: String? = null
    var description: String? = null
    var video_url: String? = null
    var id: Int? = null
    var created_by: Int? = null
    var company_id: Int? = null
    var modified_on: String? = null

    constructor() {}

    constructor(created_on: String, name: String, description: String,video_url: String, id: Int, created_by: Int,
                company_id:Int, modified_on:String) {
        this.created_on = created_on
        this.name = name
        this.description = description
        this.video_url = video_url
        this.id = id
        this.created_by = created_by
        this.company_id = company_id
        this.modified_on = modified_on
    }
}

class Events{

    var event_category: String? = null
    var registered: Boolean?=null
    var interested: Boolean?= null
    var event_locations: List<EventLocation>?=null
    var modified_by: String? = null
    var interested_count: Int? = null
    var view_count: Int? = null
    var share_count: Int? = null
    var payment: Boolean? = null
    var status: String? = null
    var priority_order: Boolean? = null
    var excerpt: String? = null
    var event_register_start_date_time: String? = null
    var created_on: String? = null
    var author_id: String? = null
    var is_online: Boolean? = null
    var gtm_id: Int? = null
    var author_name: String? = null
    var is_private: Boolean? = null
    var register_count: String? = null
    var title: String? = null
    var event_end_date_time: String? = null
    var slug: String? = null
    var event_register_end_date_time: String? = null
    var event_by: String? = null
    var id: Int? = null
    var post_for: String? = null
    var publish_date: String? = null
    var event_start_date_time: String? = null
    var created_by: Int? = null
    var company_id: Int? = null
    var address: String? = null
    var modified_on: String? = null
    var payment_note: String? = null
    var description: String? =null
    var city: String? = null
    var event_url:String?=null
    var show_on_search: Boolean?=null
    var image_url:String? =null
    var thumbnail_url:String?=null

    constructor() {}

    constructor(event_category:String,interested:Boolean,registered:Boolean,event_locations:List<EventLocation>,modified_by: String, interested_count: Int, view_count: Int,share_count: Int, payment: Boolean, status: String,
                priority_order:Boolean, excerpt:String,event_register_start_date_time: String, created_on: String, author_id: String,is_online: Boolean,
                gtm_id: Int, author_name: String,is_private: Boolean, register_count: String, title: String,event_end_date_time: String,
                slug: String, event_register_end_date_time: String,event_by: String, id: Int, post_for: String,publish_date: String,
                event_start_date_time: String, created_by: Int,company_id: Int, address: String, modified_on: String,payment_note: String,
                description: String, city:String, event_url:String, show_on_search:Boolean, image_url:String, thumbnail_url:String) {

        this.event_category = event_category
        this.interested = interested
        this.registered = registered
        this.event_locations = event_locations
        this.modified_by = modified_by
        this.interested_count = interested_count
        this.view_count= view_count
        this.share_count = share_count
        this.payment = payment
        this.status = status
        this.priority_order = priority_order
        this.excerpt = excerpt
        this.event_register_start_date_time = event_register_start_date_time
        this.created_on = created_on
        this.author_id = author_id
        this.is_online = is_online
        this.gtm_id = gtm_id
        this.author_name = author_name
        this.is_private = is_private
        this.register_count = register_count
        this.title = title
        this.event_end_date_time = event_end_date_time
        this.slug =slug
        this.event_register_end_date_time = event_register_end_date_time
        this.event_by = event_by
        this.id = id
        this.post_for = post_for
        this.publish_date= publish_date
        this.event_start_date_time = event_start_date_time
        this.created_by = created_by
        this.company_id = company_id
        this.address = address
        this.modified_on = modified_on
        this.payment_note = payment_note
        this.description = description
        this.city = city
        this.event_url = event_url
        this.show_on_search = show_on_search
        this.image_url = image_url
        this.thumbnail_url = thumbnail_url
    }
}

class Testimonials{

    var designation: String? = null
    var created_on: String? = null
    var name: String? = null
    var id: Int? = null
    var image_icon: String? = null
    var created_by: Int? = null
    var company_name: String? = null
    var testimonial: String? = null
    var company_id: Int? = null
    var modified_on: String? = null

    constructor() {}

    constructor(designation: String, created_on: String, name: String,id: Int, image_icon: String, created_by: Int,
                company_name:String, testimonial:String, company_id:Int,modified_on:String ) {
        this.designation = designation
        this.created_on = created_on
        this.name = name
        this.id = id
        this.image_icon = image_icon
        this.created_by = created_by
        this.company_name = company_name
        this.testimonial = testimonial
        this.company_id = company_id
        this.modified_on = modified_on
    }
}

class Blogs{

    var blog_view: String? = null
    var status: String? = null
    var excerpt: String? = null
    var show_register_message: Boolean? = null
    var created_on: String? = null
    var author_id: Int? = null
    var author_name: String? = null
    var category: String? = null
    var title: String? = null
    var slug: String? = null
    var id: Int? = null
    var blog_like: String? = null
    var post_for: String? = null
    var publish_date: String? = null
    var admin_id: String? = null
    var created_by: Int? = null
    var company_id: Int? = null
    var modified_on: String? = null
    var description: String? = null
    var modified_id: String? = null
    var blog_link: String?=null
    var image_url: String?= null
    var blog_share: String? = null
    var city:String? = null

    constructor() {}

    constructor(blog_view: String, status: String, excerpt: String,show_register_message: Boolean, created_on: String, author_id: Int,
                author_name:String, category:String, title:String,slug:String ,id:Int, blog_like:String,post_for:String, publish_date:String,
                admin_id:String, created_by:Int,company_id:Int,modified_on:String, description: String, modified_id: String, blog_link:String,
                image_url: String, blog_share:String, city:String) {
        this.blog_view = blog_view
        this.status = status
        this.excerpt = excerpt
        this.show_register_message= show_register_message
        this.created_on =created_on
        this.author_id = author_id
        this.author_name = author_name
        this.category = category
        this.title = title
        this.slug = slug
        this.id = id
        this.blog_like = blog_like
        this.post_for = post_for
        this.publish_date = publish_date
        this.admin_id  = admin_id
        this.created_by = created_by
        this.company_id = company_id
        this.modified_on = modified_on
        this.description = description
        this.modified_id = modified_id
        this.blog_link = blog_link
        this.image_url = image_url
        this.blog_share = blog_share
        this.city = city
    }
}