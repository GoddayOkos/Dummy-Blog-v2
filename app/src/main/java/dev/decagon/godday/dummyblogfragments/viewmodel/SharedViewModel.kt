package dev.decagon.godday.dummyblogfragments.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import dev.decagon.godday.dummyblogfragments.database.MainDatabase
import dev.decagon.godday.dummyblogfragments.model.Comment
import dev.decagon.godday.dummyblogfragments.model.Post
import dev.decagon.godday.dummyblogfragments.repository.DataRepository
import dev.decagon.godday.dummyblogfragments.ui.CommentsFragment
import kotlinx.coroutines.launch

/**
 * This view model is responsible for managing the data displayed by the two fragments
 * in the application. It interacts with the repository for its data
 */

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val dataRepository = DataRepository(MainDatabase.getInstance(application))
    var allComments = dataRepository.commentList
    lateinit var post: Post
    val posts = dataRepository.postList
    private val _filteredPost = MutableLiveData<List<Post>>()
    val filteredList = _filteredPost as LiveData<List<Post>>
    private val _searchPost = MutableLiveData<List<Post>>()
    val searchPost = _searchPost as LiveData<List<Post>>

    init {
        refreshPostFromRepository()
        refreshCommentsFromData()
    }

    /**
     * Method to refresh the posts in the repository keeping it up to date with the server
     */
    private fun refreshPostFromRepository() {
        viewModelScope.launch {
            dataRepository.refreshPost()
        }
    }

    /**
     * Method to filter post by userId
     */
    fun loadPosts(userId: Int) {
        _filteredPost.value = posts.value!!.filter { it.userId == userId }
    }

    /**
     * Method to search for posts based on words entered by the user
     */
    fun searchPosts(searchEntry: String) {
        _searchPost.value = posts.value!!.filter { it.body.contains(searchEntry, ignoreCase = true) ||
                it.title.contains(searchEntry, ignoreCase = true) }
    }

    /**
     *  Method to create a new post by inserting the post into the database
     *  using the helper method from the repository
     */
    fun createPost(post: Post) {
        viewModelScope.launch {
            dataRepository.insertPost(post)
        }
    }

    /**
     * Method to get post clicked on by the user and get all comments belonging to that post
     * from the database which is displayed on the comment page
     */
    fun setPost(post: Post): Boolean {
        this.post = post
       allComments = dataRepository.getComment(post.id!!)
        return true
    }

    /**
     * Method to refresh the comments in the repository keeping it up to date with the server
     */
    private fun refreshCommentsFromData() {
        viewModelScope.launch {
            dataRepository.refreshComment()
        }
    }

    /**
     *  Method to create a new comment by inserting the post into the database
     *  using the helper method from the repository
     */
    fun createComment(comment: Comment) {
        viewModelScope.launch {
            dataRepository.insertComment(comment)
        }
    }
}