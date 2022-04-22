package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import java.lang.Exception
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback<List<Post>> {
            override fun onSuccess(value: List<Post>) {
                _data.postValue(FeedModel(posts = value, empty = value.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        edited.value?.let {
                repository.save(it, object : PostRepository.GetAllCallback<Post>{
                    override fun onSuccess(value: Post) {
                        _postCreated.postValue(Unit)
                    }

                    override fun onError(e: Exception) {
                        super.onError(e)
                    }
                })
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(id: Long) {
        repository.likeById(id, object : PostRepository.GetAllCallback<Long> {

            override fun onSuccess(value: Long) {
                _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty().map{ post ->
                    if (post.id == id){
                        post.copy(likes = post.likes + 1, likedByMe = !post.likedByMe)
                    }else
                        post
                })
                )
            }
        })
    }


fun deliteLike(id: Long) {
        repository.deliteLike(id, object : PostRepository.GetAllCallback<Long>{

            override fun onSuccess(value: Long) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty().map { post ->
                        if (post.id == id) {
                            post.copy(likes = post.likes - 1, likedByMe = !post.likedByMe)
                        } else
                            post
                    })
                )
            }
        })


}

fun removeById(id: Long) {
    val old = _data.value?.posts.orEmpty()
    repository.removeById(id, object : PostRepository.GetAllCallback<Long> {
        override fun onSuccess(value: Long) {
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
        }

        override fun onError(e: Exception) {
            _data.postValue(_data.value?.copy(posts = old))
        }

    })

}
}
