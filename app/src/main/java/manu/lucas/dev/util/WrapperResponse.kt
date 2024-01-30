package manu.lucas.dev.util

sealed class WrapperResponse<T>(val data:T? = null, val message: String? = null){
    class Sucess<T>(data:T): WrapperResponse<T>(data)
    class Error<T>(message: String,data:T ? = null): WrapperResponse<T>(data,message)
    class Loading<T>(data: T?= null): WrapperResponse<T>(data)
}
