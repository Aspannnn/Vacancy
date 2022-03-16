package kz.aspan.vacancy.presentation.response

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.vacancy.R
import kz.aspan.vacancy.common.ImagePicker
import kz.aspan.vacancy.databinding.FragmentResponsToAVacancyBinding
import kz.aspan.vacancy.domain.model.ResumeData

@AndroidEntryPoint
class ResponseToAVacancyFragment : Fragment(R.layout.fragment_respons_to_a_vacancy) {
    private var _binding: FragmentResponsToAVacancyBinding? = null
    private val binding: FragmentResponsToAVacancyBinding
        get() = _binding!!

    private lateinit var imagePicker: ImagePicker

    private val viewModel: ResponseToAVacancyViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResponsToAVacancyBinding.bind(view)


        var bitmap: Bitmap? = null
        imagePicker = ImagePicker(requireActivity().activityResultRegistry) { imageUri ->
            imageUri?.let { bitmap = createBitmapFromUri(it) }
            binding.avatarEditor.avatarIv.load(imageUri)
        }
        lifecycle.addObserver(imagePicker)


        binding.avatarEditor.apply {
            editTitle.text = getString(R.string.photo)
            editSB.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    editTv.visibility = View.VISIBLE
                    avatarIv.visibility = View.VISIBLE
                } else {
                    editTv.visibility = View.GONE
                    avatarIv.visibility = View.GONE
                }
            }
            editTv.setOnClickListener {
                imagePicker.selectImage()
            }
        }


        //email
        binding.emailEditor.apply {
            editTitle.text = getString(R.string.email)
            editSB.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    infoTv.visibility = View.VISIBLE
                    editTv.visibility = View.VISIBLE
                } else {
                    infoTv.visibility = View.GONE
                    editTv.visibility = View.GONE
                }
            }
        }

        //phone number
        binding.phoneNumberEditor.apply {
            editTitle.text = getText(R.string.phone_number)
            editSB.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    infoTv.visibility = View.VISIBLE
                    editTv.visibility = View.VISIBLE
                } else {
                    infoTv.visibility = View.GONE
                    editTv.visibility = View.GONE
                }
            }
        }

        //gpa
        binding.gpaEditor.apply {
            editTitle.text = getText(R.string.avg_gpa)
            editTv.visibility = View.GONE
            editSB.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    infoTv.visibility = View.VISIBLE
                } else {
                    infoTv.visibility = View.GONE
                }
            }
        }


        //skill
        binding.skillEditor.apply {
            editTitle.text = getText(R.string.skill)
            editSB.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    editInfoEt.visibility = View.VISIBLE
                } else {
                    editInfoEt.visibility = View.GONE
                }
            }
        }

        //achievements
        binding.achievementsEditor.apply {
            editTitle.text = getText(R.string.achievements)
            editSB.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    editInfoEt.visibility = View.VISIBLE
                } else {
                    editInfoEt.visibility = View.GONE
                }
            }
        }


        //experience
        binding.experienceEditor.apply {
            editTitle.text = getText(R.string.experience)
            editSB.setOnCheckedChangeListener { _, isChecked ->
                if (!isChecked) {
                    editInfoEt.visibility = View.VISIBLE
                } else {
                    editInfoEt.visibility = View.GONE
                }
            }
        }


        binding.generateCVButton.setOnClickListener {
            val resume = ResumeData(photo = bitmap)

            if (!binding.avatarEditor.editSB.isChecked) {

            }

            if (!binding.experienceEditor.editSB.isChecked) {
                resume.experience = binding.experienceEditor.editInfoEt.text.toString()
            }
            if (!binding.achievementsEditor.editSB.isChecked) {
                resume.awards = binding.achievementsEditor.editInfoEt.text.toString()
            }
            if (!binding.skillEditor.editSB.isChecked) {
                resume.skills = binding.skillEditor.editInfoEt.text.toString()
            }
            if (!binding.emailEditor.editSB.isChecked) {
                resume.email = binding.emailEditor.infoTv.text.toString()
            }
            if (!binding.phoneNumberEditor.editSB.isChecked) {
                resume.phoneNumber = binding.phoneNumberEditor.infoTv.text.toString()
            }
            if (!binding.gpaEditor.editSB.isChecked) {
                resume.avgGPA = binding.gpaEditor.infoTv.text.toString()
            }
            viewModel.sedFileRequest(resume)
        }
    }


    private fun createBitmapFromUri(uri: Uri): Bitmap? {
        val contentResolver = requireActivity().contentResolver
        return try {
            if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}