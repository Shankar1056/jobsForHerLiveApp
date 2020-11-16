package com.bigappcompany.healthtunnel.data.network


abstract class ApiCallback {
    abstract fun onSuccess(obj: Any?)

    open fun onError(message: String?) {
        onHandledError()
    }

    open fun onUnAuthentic() {
        onHandledError()
    }

    open fun onNetworkError() {
        onHandledError()
    }

    abstract fun onHandledError()
}
