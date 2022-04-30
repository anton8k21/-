package ru.netology.nmedia.repository

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.RuntimeException
import kotlin.math.log


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.GetAllCallback<List<Post>>) {
        PostApiService.api.getAll()
            .enqueue(object : retrofit2.Callback<List<Post>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Post>>,
                    response: retrofit2.Response<List<Post>>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess(response.body().orEmpty())
                    } else
                        callback.onError(RuntimeException(response.message()))
                }

                override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }


            })

    }


    override fun likeById(id: Long, callback: PostRepository.GetAllCallback<Long>) {
        PostApiService.api.likeById(id)
            .enqueue(object : retrofit2.Callback<Unit>{
                override fun onResponse(call: retrofit2.Call<Unit>, response: retrofit2.Response<Unit>) {
                    if (response.isSuccessful){
                        callback.onSuccess(id)
                    }else
                        callback.onError(RuntimeException(response.message()))
                }

                override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }


            })

    }

    override fun deliteLike(id: Long, callback: PostRepository.GetAllCallback<Long>) {
        PostApiService.api.deleteLike(id)
            .enqueue(object : retrofit2.Callback<Unit>{
                override fun onResponse(call: retrofit2.Call<Unit>, response: retrofit2.Response<Unit>) {
                    if (response.isSuccessful){
                        callback.onSuccess(id)
                    }else
                        callback.onError(RuntimeException(response.message()))
                }

                override fun onFailure(call: retrofit2.Call<Unit>, t: Throwable) {
                    callback.onError(RuntimeException(t))
                }

            })
    }

    override fun save(post: Post, callback: PostRepository.GetAllCallback<Post>) {
       PostApiService.api.save(post)
           .enqueue(object : retrofit2.Callback<Post>{
               override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
                   if (response.isSuccessful){
                       callback.onSuccess(post)
                   }else
                       callback.onError(RuntimeException(response.message()))
               }

               override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
                   callback.onError(RuntimeException(t))
               }

           })
    }

    override fun removeById(id: Long, callback: PostRepository.GetAllCallback<Long>) {
       PostApiService.api.removeById(id)
           .enqueue(object : retrofit2.Callback<Unit>{
               override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                   if (response.isSuccessful()){
                       callback.onSuccess(id)
                   }else
                       callback.onError(RuntimeException(response.message()))
               }

               override fun onFailure(call: Call<Unit>, t: Throwable) {
                   callback.onError(RuntimeException(t))
               }

           })
    }
}


