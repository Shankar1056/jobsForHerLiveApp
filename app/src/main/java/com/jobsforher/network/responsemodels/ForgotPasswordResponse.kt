package entertainment.minersinc.tfhy.network.responsemodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForgotPasswordResponse
{

    @SerializedName("message")
    @Expose
    var message:Message? = null
}

class Message{

    @SerializedName("response_code")
    @Expose
    var response_code: Int? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}


