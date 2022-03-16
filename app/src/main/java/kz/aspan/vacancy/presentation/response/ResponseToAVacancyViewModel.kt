package kz.aspan.vacancy.presentation.response

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.aspan.vacancy.domain.model.ResumeData
import kz.aspan.vacancy.domain.repository.VacancyRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ResponseToAVacancyViewModel @Inject constructor(
    private val repository: VacancyRepository
) : ViewModel() {


    fun sedFileRequest(resume: ResumeData) {
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

        fillPartIfNotNull(body, resume.useProfilePhoto.toString(), "UseProfilePhoto")
        fillPartIfNotNull(body, resume.email, "Email")
        fillPartIfNotNull(body, resume.skills, "Skills")
        fillPartIfNotNull(body, resume.awards, "Awards")
        fillPartIfNotNull(body, resume.experience, "Experience")
        fillPartIfNotNull(body, resume.phoneNumber, "PhoneNumber")
        fillPartIfNotNull(body, resume.avgGPA, "AvgGPA")
        fillPartIfNotNull(body, resume.vacancyId, "VacancyId")

        if (!resume.useProfilePhoto && resume.photo != null) {
            val stream = ByteArrayOutputStream()
            resume.photo.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val byteArray = stream.toByteArray()
            body.addFormDataPart(
                "Photo",
                "avatar",
                byteArray.toRequestBody("image/*".toMediaTypeOrNull(), 0, byteArray.size)
            )
        }

        println("RESUME TEST   $resume")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.generateCv(body.build())
                println("Test result $result")

            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }

    private fun fillPartIfNotNull(body: MultipartBody.Builder, data: String?, param: String) {
        if (!data.isNullOrBlank()) {
            body.addFormDataPart(param, data)
        }
    }
}