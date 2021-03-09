package dev.decagon.godday.dummyblogfragments.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.decagon.godday.dummyblogfragments.database.MainDatabase
import dev.decagon.godday.dummyblogfragments.model.Comment
import dev.decagon.godday.dummyblogfragments.model.Post
import dev.decagon.godday.dummyblogfragments.network.CommentService
import dev.decagon.godday.dummyblogfragments.network.PostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This class holds all the logic for working with the different data sources available.
 * It pulls the data from the server and feeds them into the local database. It them pulls
 * the data from the local database and feeds them to the viewModels.
 */
class DataRepository(private val database: MainDatabase) {

    val postList: LiveData<List<Post>> = database.postDao.getAllPosts()
    var commentList: LiveData<List<Comment>> = database.commentDao.getAllComments(0)
    private val TAG = "DataRepository"

    /**
     * Method to get comments whose post id matches the function parameter
     */
    fun getComment(id: Int): LiveData<List<Comment>> = database.commentDao.getAllComments(id)

    /**
     * This method gets the posts from the server and insert them into the database
     */
    suspend fun refreshPost() {
        withContext(Dispatchers.IO) {
            try {
                val posts = PostService.PostServiceClient.retrofitService.getPosts()
                database.postDao.insertAll(posts)
            } catch (e: Exception) {
                logIt("${e.message}")
            }
        }
    }

    /**
     * This method gets the comments from the server and insert them into the database
     */
    suspend fun refreshComment() {
        withContext(Dispatchers.IO) {
            try {
                val comments = CommentService.CommentServiceClient.retrofitService.getComment()
                database.commentDao.insertAll(comments)
            } catch (e: Exception) {
                logIt("${e.message}")
            }
        }
    }

    /**
     * This method is used to insert a post into the database
     */
    suspend fun insertPost(post: Post) {
        withContext(Dispatchers.IO) {
            database.postDao.insert(post)
        }
    }

    /**
     * This method is used to insert a comment into the database
     */
    suspend fun insertComment(comment: Comment) {
        withContext(Dispatchers.IO) {
            database.commentDao.insert(comment)
        }
    }

    private fun logIt(msg: String) {
        Log.d(TAG, msg)
    }
}