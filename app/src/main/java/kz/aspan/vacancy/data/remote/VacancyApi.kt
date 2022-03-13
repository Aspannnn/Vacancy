package kz.aspan.vacancy.data.remote

import kz.aspan.vacancy.domain.model.DataInfo
import kz.aspan.vacancy.domain.model.Simple
import kz.aspan.vacancy.domain.model.Token
import kz.aspan.vacancy.domain.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VacancyApi {

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


}