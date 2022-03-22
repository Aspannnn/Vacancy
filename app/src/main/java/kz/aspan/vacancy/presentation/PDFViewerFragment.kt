package kz.aspan.vacancy.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import kz.aspan.vacancy.R
import kz.aspan.vacancy.databinding.FragmentPDFViewerBinding

class PDFViewerFragment : Fragment(R.layout.fragment_p_d_f_viewer) {
    private var _binding: FragmentPDFViewerBinding? = null
    private val binding: FragmentPDFViewerBinding
        get() = _binding!!

    private val args: PDFViewerFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPDFViewerBinding.bind(view)

        binding.pdfwv.apply {
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.setSupportZoom(true)
            loadUrl("https://docs.google.com/gview?embedded=true&url=${args.pdfUrl}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}