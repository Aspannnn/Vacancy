package kz.aspan.vacancy.domain.model

import android.graphics.Bitmap

data class ResumeData(
    var useProfilePhoto: Boolean = true,
    val photo: Bitmap? = null,
    var email: String? = null,
    var skills: String? = null,
    var awards: String? = null,
    var experience: String? = null,
    var phoneNumber: String? = null,
    var avgGPA: String? = null,
    var vacancyId: String? = null
)
