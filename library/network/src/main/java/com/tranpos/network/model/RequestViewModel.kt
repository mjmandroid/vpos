package com.tranpos.network.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class RequestViewModel : ViewModel() {

    open val apiException : MutableLiveData<Throwable> = MutableLiveData()
    open val apiLoading : MutableLiveData<Boolean> = MutableLiveData()

    private fun <T> api(apiDsl : ViewModelDsl<T>.() ->Unit){
        ViewModelDsl<T>().apply(apiDsl).launch(viewModelScope)
    }

    @JvmOverloads
    protected fun <T> apiCallback(
        request : suspend ()->T,
        onResponse : ((T)->Unit)? = null,
        onStart : (()->Boolean?)? = null,
        onError : ((Exception) ->Boolean?)? = null,
        onFinally : (()->Boolean?)? = null
    ){
        api<T> {
            onRequest {
                request.invoke()
            }

            onStart {
                val override = onStart?.invoke() ?: false
                if (override){
                    onApiStart()
                }
                override
            }

            onResponse {
                onResponse?.invoke(it)
            }

            onError {
                val override = onError?.invoke(it) ?: false
                if(override){
                    onApiError(it)
                }
                override
            }

            onFinal {
                val override = onFinally?.invoke() ?: false
                if(override){
                    onApiFinally()
                }
                override
            }
        }
    }

    protected fun <T> apiDSL(apiDsl : ViewModelDsl<T>.()->Unit){
        api<T> {
            onRequest {
                ViewModelDsl<T>().apply(apiDsl).request()
            }
            onStart {
                val override = ViewModelDsl<T>().apply(apiDsl).onStart?.invoke() ?: false
                if(override){
                    onApiStart()
                }
                override
            }
            onResponse {
                ViewModelDsl<T>().apply(apiDsl).onResponse?.invoke(it)
            }

            onError {
                e->
                val override = ViewModelDsl<T>().apply(apiDsl).onError?.invoke(e) ?: false
                if(override){
                    onApiError(e)
                }
                override
            }

            onFinal {
                val override = ViewModelDsl<T>().apply(apiDsl).onFinal?.invoke() ?: false
                if(override){
                    onApiFinally()
                }
                override

            }
        }
    }


    protected fun <T> apiLiveData(
        context:CoroutineContext = EmptyCoroutineContext,
        timeout : Long = 10000L,
        request : suspend ()->T
    ) : LiveData<Result<T>>{
        return androidx.lifecycle.liveData(
            context,
            timeout
        ){
            emit(Result.Start())
            try {
                emit(
                    withContext(Dispatchers.IO){
                        Result.Response(request())
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error<T>(e))
            }finally {
                emit(Result.Finally())
            }
        }
    }

    protected open fun onApiStart() {
        apiLoading.value = true
    }
    protected open fun onApiError(e : Exception) {
        apiException.value = e
        apiLoading.value = false
    }

    protected open fun onApiFinally() {
        apiLoading.value = false
    }

    sealed class Result<T>{
        class Start<T> : Result<T>()
        class Finally<T> : Result<T>()
        data class Response<T>(val response: T) : Result<T>()
        data class Error<T>(val exception: Exception) : Result<T>()
    }
}