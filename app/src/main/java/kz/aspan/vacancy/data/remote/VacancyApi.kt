package kz.aspan.vacancy.data.remote

import kz.aspan.vacancy.domain.model.*
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.*
import java.io.InputStream

interface VacancyApi {

    @Streaming
    @GET
    suspend fun downloadPdf(@Url url: String): ResponseBody

    @POST("Authentication/Login")
    suspend fun login(@Body user: User): Token


    @GET("vacancy/professions")
    suspend fun getProfessions(): Simple

    @GET("vacancy/skills")
    suspend fun getSkills(): Simple

    @GET("vacancy/employers")
    suspend fun getEmployers(): Simple

    @GET("vacancy")
    suspend fun getProfession(
        @Query("ProfessionId") id: String,
    ): DataInfo

    @GET("vacancy")
    suspend fun getSkill(
        @Query("SkillId") id: String,
    ): DataInfo

    @GET("vacancy")
    suspend fun getEmployer(
        @Query("EmployerId") id: String,
    ): DataInfo

    @POST("resume")
    suspend fun generateCV(@Body body: RequestBody): Resume

    @GET("resume/info")
    suspend fun getStudent(): Student

    @POST("resume/{id}/send")
    suspend fun sendResume(@Path("id") id: String)
}