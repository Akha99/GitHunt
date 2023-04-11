package com.example.githunt.api

import com.example.githunt.data.model.DetailUserResponse
import com.example.githunt.data.model.User
import com.example.githunt.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>


    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>


    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowers(
        @Path ("username") username:String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowing(
        @Path ("username") username:String
    ): Call<ArrayList<User>>
}