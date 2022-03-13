package kz.aspan.vacancy.domain.repository

import kz.aspan.vacancy.domain.model.DataInfo
import kz.aspan.vacancy.domain.model.Simple
import kz.aspan.vacancy.domain.model.Token
import kz.aspan.vacancy.domain.model.User

interface VacancyRepository {
    suspend fun getToken(user: User): Token

    suspend fun getProfessions(): Simple
    suspend fun getSkills(): Simple
    suspend fun getEmployers(): Simple

    suspend fun getProfessionById(id: String): DataInfo
    suspend fun getSkillById(id: String): DataInfo
    suspend fun getEmployerById(id: String): DataInfo
}