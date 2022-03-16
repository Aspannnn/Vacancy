package kz.aspan.vacancy.common

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kz.aspan.vacancy.common.Constants.MIMETYPE_IMAGES
import kz.aspan.vacancy.common.Constants.RESULT_REGISTRY_KEY

class ImagePicker(
    private val register: ActivityResultRegistry,
    private val callback: (imageUri: Uri?) -> Unit
) : DefaultLifecycleObserver {
    private lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        getContent = register.register(
            RESULT_REGISTRY_KEY,
            owner,
            ActivityResultContracts.GetContent(),
            callback
        )
    }


    fun selectImage() {
        getContent.launch(MIMETYPE_IMAGES)
    }
}