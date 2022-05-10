package com.example.submissionintermediate.Api

import com.example.submissionintermediate.pojo.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun signupUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseRegister>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call <ResponseLogin>

    @GET("stories")
    fun getStories(@Header("Authorization") token: String,
                   @Query("page") page: Int,
                   @Query("size") size: Int
        ): Call <ResponseGetAllStoryNew<ListStoryItem>>

    @GET("stories")
    fun getStoriesLocation(@Header("Authorization") token: String): Call <ResponseGetAllStory>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseStory>



}
