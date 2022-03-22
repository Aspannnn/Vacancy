package kz.aspan.vacancy.presentation

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.vacancy.R
import kz.aspan.vacancy.databinding.FragmentPDFViewerBinding
import java.io.File


@AndroidEntryPoint
class PDFViewerFragment : Fragment(R.layout.fragment_p_d_f_viewer) {
    private var _binding: FragmentPDFViewerBinding? = null
    private val binding: FragmentPDFViewerBinding
        get() = _binding!!

    private val viewModel: PDFViewerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPDFViewerBinding.bind(view)

        viewModel.resumeFilePathMutableLiveData.observe(viewLifecycleOwner) {
            val input = ParcelFileDescriptor.open(File(it), ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(input)
            val page = renderer.openPage(0)

            val bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            binding.pdfTest.setImageBitmap(bitmap)

            page.close()
            renderer.close()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}