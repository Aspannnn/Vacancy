package kz.aspan.vacancy.presentation.response

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.vacancy.R
import kz.aspan.vacancy.common.ImagePicker
import kz.aspan.vacancy.common.extensions.hideKeyboard
import kz.aspan.vacancy.common.extensions.onDone
import kz.aspan.vacancy.databinding.FragmentResponsToAVacancyBinding
import kz.aspan.vacancy.databinding.ItemResumeEditorTvBinding
import kz.aspan.vacancy.domain.model.Resume
import kz.aspan.vacancy.domain.model.ResumeData
import java.io.File

@AndroidEntryPoint
class ResponseToAVacancyFragment : Fragment(R.layout.fragment_respons_to_a_vacancy) {
    private var _binding: FragmentResponsToAVacancyBinding? = null
    private val binding: FragmentResponsToAVacancyBinding
        get() = _binding!!

    private lateinit var imagePicker: ImagePicker
    private var downloadId: Long = 0

    private val viewModel: ResponseToAVacancyViewModel by viewModels()
    private val args: ResponseToAVacancyFragmentArgs by navArgs()

    private lateinit var resume: Resume


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentResponsToAVacancyBinding.bind(view)

        requireActivity().registerReceiver(
            br,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        subscribeToObservers()

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
            textInputEditText
            editTitle.text = getString(R.string.email)
            editTv.setOnClickListener {
                val text = infoTv.text.toString()
                infoTv.visibility = View.GONE
                textInputEditText.visibility = View.VISIBLE
                textInputEditText.setText(text)
            }
            textInputEditText.onDone {
                edit(this)
                requireContext().hideKeyboard(textInputEditText)
            }
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
            editTv.setOnClickListener {
                val text = infoTv.text.toString()
                infoTv.visibility = View.GONE
                textInputEditText.visibility = View.VISIBLE
                textInputEditText.setText(text)
            }
            textInputEditText.onDone {
                edit(this)
                requireContext().hideKeyboard(textInputEditText)
            }
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
            val resume = if (!binding.avatarEditor.editSB.isChecked) {
                if (bitmap != null) {
                    ResumeData(useProfilePhoto = false, photo = bitmap)
                } else {
                    ResumeData(photo = bitmap)
                }
            } else {
                ResumeData(photo = null)
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
            resume.vacancyId = args.id
            viewModel.sedFileRequest(resume)
        }

        binding.viewCV.setOnClickListener {
            downloadFile(resume.resumeUrl)
        }

        binding.sendButton.setOnClickListener {
            viewModel.sendResume(resume.id)
        }
    }

    private val br = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val id = p1?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadId) {
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Download Completed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun downloadFile(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Document")
            .setDescription("Downloading")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or
                        DownloadManager.Request.NETWORK_MOBILE
            )
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Document")
            .setMimeType("application/pdf")

        val dm = requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = dm.enqueue(request)
    }


    private fun edit(test: ItemResumeEditorTvBinding) {
        val editText = test.textInputEditText.text.toString()
        test.textInputEditText.visibility = View.GONE
        test.infoTv.text = editText
        test.infoTv.visibility = View.VISIBLE
    }

    private fun subscribeToObservers() {
        viewModel.studentLiveData.observe(viewLifecycleOwner) {
            binding.emailEditor.infoTv.text = it.email
            binding.phoneNumberEditor.infoTv.text = it.phoneNumber
            binding.gpaEditor.infoTv.text = it.avgGPA

            if (it.photo != null) {
                binding.avatarEditor.avatarIv.load(it.photo)
            }
        }

        viewModel.resumeLiveData.observe(viewLifecycleOwner) {
            resume = it
            binding.cvButtons.visibility = View.VISIBLE
            binding.scrollView.post {
                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }

        viewModel.isResumeSent.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Ваше резюме отправлено работодателю",
                    Toast.LENGTH_LONG
                ).show()
            }
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