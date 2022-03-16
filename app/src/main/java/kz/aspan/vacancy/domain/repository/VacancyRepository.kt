package kz.aspan.vacancy.domain.repository

import kz.aspan.vacancy.domain.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface VacancyRepository {
    suspend fun getToken(user: User): Token

    suspend fun getProfessions(): Simple
    suspend fun getSkills(): Simple
    suspend fun getEmployers(): Simple

    suspend fun getProfessionById(id: String): DataInfo
    suspend fun getSkillById(id: String): DataInfo
    suspend fun getEmployerById(id: String): DataInfo

    suspend fun generateCv(part: RequestBody): Resume
}