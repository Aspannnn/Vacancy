package kz.aspan.vacancy.data.repository

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.aspan.vacancy.data.remote.VacancyApi
import kz.aspan.vacancy.domain.model.*
import kz.aspan.vacancy.domain.repository.VacancyRepository
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class VacancyRepositoryImpl @Inject constructor(
    private val context: Context,
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

    override suspend fun getStudent(): Student {
        return api.getStudent()
    }

    override suspend fun sendResume(id: String) {
        return api.sendResume(id)
    }

    override suspend fun downloadPDF(url: String, fileName: String): String =
        withContext(Dispatchers.IO) {
            val file = createFile(fileName)
            val response = api.downloadPdf(url)
            val inputStream =
                response.byteStream() ?: throw DownloadException("Download body is null")
            inputStream.use { input ->
                FileOutputStream(file).use { output -> input.copyTo(output) }
            }
            file.absolutePath
        }

    private fun createFile(fileName: String): File = try {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        File.createTempFile(fileName, ".pdf", directory)
    } catch (e: IOException) {
        throw DownloadException("Error while creating download file", e)
    }

    private fun InputStream.copyTo(output: FileOutputStream) {
        val buffer = ByteArray(4 * 1024)
        while (true) {
            val byteCount = read(buffer)
            if (byteCount < 0) break
            output.write(buffer, 0, byteCount)
        }
        output.flush()
    }

    private class DownloadException(message: String, cause: Throwable? = null) :
        RuntimeException(message, cause)
}