package ru.netology.nmedia.api

import androidx.room.Delete
import com.bumptech.glide.util.Util
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositoryImpl
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:9999/api/slow/"

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()

val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface PostApi{
    @GET("posts")
    fun getAll():Call<List<Post>>

    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long):Call<Unit>

    @DELETE("posts/{id}/likes")
    fun deleteLike(@Path("id") id: Long):Call<Unit>

    @DELETE("posts/{id}")
    fun removeById(@Path("id") id: Long):Call<Unit>

    @POST("posts")
    fun save(@Body post: Post): Call<Post>


}

object PostApiService{
    val api by lazy{
        retrofit.create(PostApi::class.java)
    }
}