package dev.decagon.godday.dummyblogfragments.network

import dev.decagon.godday.dummyblogfragments.model.Post
import retrofit2.http.GET

/**
 * This is the interface/API for getting posts from the server
 */
interface PostService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    object PostServiceClient {
        val retrofitService: PostService by lazy {
            ApiClients.retrofit.create(PostService::class.java)
        }
    }
}