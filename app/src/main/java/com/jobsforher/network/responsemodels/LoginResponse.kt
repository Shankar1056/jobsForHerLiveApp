package entertainment.minersinc.tfhy.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse
{

    @SerializedName("auth")
    @Expose
    var auth:Auth? = null

    @SerializedName("user")
    @Expose
    var user:User? = null
}



class Auth
{
    var access_token: String = ""
    var client_id: String = ""
    var expires_in: String = ""
    var refresh_token: String = ""
    var scope: String = ""
    var token_type: String = ""
}




class User
{

    var created_on: String = ""
    var email: String = ""
    var ip: String = ""
    var message: String = ""
    var modified_on: String = ""
    var profile_icon: String = ""
    var profile_image: String = ""
    var reference_url: String = ""
    var role: String = ""
    var status: String = ""
    var user_key: String = ""
    var username: String = ""
}


