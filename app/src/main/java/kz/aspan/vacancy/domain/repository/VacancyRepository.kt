package kz.aspan.vacancy.domain.repository

import kz.aspan.vacancy.domain.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody

interface VacancyRepository {
    suspend fun getToken(user: User): Token

    suspend fun getProfessions(): Simple
    suspend fun getSkills(): Simple
    suspend fun getEmployers(): Simple

    suspend fun getProfessionById(id: String): DataInfo
    suspend fun getSkillById(id: String): DataInfo
    suspend fun getEmployerById(id: String): DataInfo

    suspend fun generateCv(part: RequestBody): Resume
    suspend fun getStudent(): Student

    suspend fun sendResume(id: String)

    suspend fun downloadPDF(url: String, fileName: String): String
}