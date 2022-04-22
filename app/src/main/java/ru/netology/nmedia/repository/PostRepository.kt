package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.dto.Post
import java.lang.Exception

interface PostRepository {
    fun likeById(id: Long,callback: GetAllCallback<Long>)
    fun deliteLike(id: Long,callback: GetAllCallback<Long>)
    fun save(post: Post, callback: GetAllCallback<Post>)
    fun removeById(id: Long, callback: GetAllCallback<Long>)

    fun getAllAsync(callback: GetAllCallback<List<Post>>)

    interface GetAllCallback <T>{
        fun onSuccess(value: T){}
        fun onError(e: Exception){}
    }
}
