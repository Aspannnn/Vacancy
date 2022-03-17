package kz.aspan.vacancy.presentation.response

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.aspan.vacancy.domain.model.Resume
import kz.aspan.vacancy.domain.model.ResumeData
import kz.aspan.vacancy.domain.model.Student
import kz.aspan.vacancy.domain.repository.VacancyRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class ResponseToAVacancyViewModel @Inject constructor(
    private val repository: VacancyRepository
) : ViewModel() {

    private val _studentLiveData = MutableLiveData<Student>()
    val studentLiveData: LiveData<Student> = _studentLiveData

    private val _resumeLiveData = MutableLiveData<Resume>()
    val resumeLiveData: LiveData<Resume> = _resumeLiveData

    private val _isResumeSent = MutableLiveData<Boolean>()
    val isResumeSent: LiveData<Boolean> = _isResumeSent

    init {
        getStudent()
    }

    fun sendResume(id: String) {
        println("TEST SEND  $id")
        viewModelScope.launch {
            try {
                repository.sendResume(id)
                _isResumeSent.postValue(true)
//                }
            } catch (e: Exception) {
                _isResumeSent.postValue(false)
            }
        }
    }

    private fun getStudent() {
        viewModelScope.launch {
            try {
                val result = repository.getStudent()
                _studentLiveData.postValue(result)
            } catch (e: Exception) {

            }
        }
    }

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.generateCv(body.build())
                _resumeLiveData.postValue(result)
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