package kz.aspan.vacancy.data.repository

import kz.aspan.vacancy.data.remote.VacancyApi
import kz.aspan.vacancy.domain.model.*
import kz.aspan.vacancy.domain.repository.VacancyRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class VacancyRepositoryImpl @Inject constructor(
    private val api: VacancyApi
) : VacancyRepository {
    override suspend fun getToken(user: User): Token {
        return api.login(user)
    }

    override suspend fun getProfessions(): Simple {
        return api.getProfessions()
    }

    override suspend fun getSkills(): Simple {
        return api.getSkills()
    }

    override suspend fun getEmployers(): Simple {
        return api.getEmployers()
    }

    override suspend fun getProfessionById(id: String): DataInfo {
        return api.getProfession(id)
    }

    override suspend fun getSkillById(id: String): DataInfo {
        return api.getSkill(id)
    }

    override suspend fun getEmployerById(id: String): DataInfo {
        return api.getEmployer(id)
    }

    override suspend fun generateCv(part: RequestBody): Resume {
        return api.generateCV(part)
    }
}