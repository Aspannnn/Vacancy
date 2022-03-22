package kz.aspan.vacancy.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.vacancy.data.remote.BasicAuthInterceptor
import kz.aspan.vacancy.databinding.ActivityMainBinding
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
    }
}