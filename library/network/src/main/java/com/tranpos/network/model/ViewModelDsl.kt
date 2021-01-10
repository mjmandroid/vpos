package com.tranpos.network.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelDsl<T> {

    lateinit var request : suspend () -> T

     var onStart : (() -> Boolean?)? = null

     var onResponse : ((T) ->Unit)?  = null

     var onError : ((Exception) ->Boolean?)? = null

     var onFinal : (()->Boolean?)? = null

    infix fun onStart(onStart : (() -> Boolean?)){
        this.onStart = onStart
    }

    infix fun onRequest(request :suspend ()->T){
        this.request = request
    }

    infix fun onResponse (onResponse : ((T) ->Unit)){
        this.onResponse = onResponse
    }

    infix fun onError(onError : ((Exception) ->Boolean)){
        this.onError = onError
    }

    infix fun onFinal(onFinal : (()->Boolean?)){
        this.onFinal = onFinal
    }

    fun launch(scope : CoroutineScope){
        scope.launch (context = Dispatchers.Main){
            onStart?.invoke()
            try {
                val result = withContext(context = Dispatchers.IO){
                    request()
                }
                onResponse?.invoke(result)
            } catch (e: Exception) {
                e.printStackTrace()
                onError?.invoke(e)
            } finally {
                onFinal?.invoke()
            }
        }
    }
}