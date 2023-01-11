package com.ashomok.eNumbers.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.legacy.app.FragmentCompat
import com.ashomok.eNumbers.BuildConfig
import com.ashomok.eNumbers.R
import com.ashomok.eNumbers.activities.ocr_task.OCRAnimationActivity
import com.ashomok.eNumbers.activities.ocr_task.OCRFirstRunDialogFragment
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTask
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageAsyncTask.OnTaskCompletedListener
import com.ashomok.eNumbers.activities.ocr_task.RecognizeImageStandalone
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchByCodeFragment : ENListKeyboardFragment(), OnTaskCompletedListener,
    FragmentCompat.OnRequestPermissionsResultCallback {
    private var img_path: String? = null
    private var recognizeImageAsyncTask: RecognizeImageAsyncTask? = null
    private var context: Activity? = null
    private val takePictureActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                startOCRtask(img_path)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.start_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            val fab = view.findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener(FabClickHandler())
            context = activity
        } catch (e: Exception) {
            Log.e(this.javaClass.canonicalName, e.message!!)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == OCRAnimationActivity_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            recognizeImageAsyncTask!!.cancel(true)
        }
    }

    private fun startOCRtask(img_path: String?) {
        //run animation
        val intent = Intent(context, OCRAnimationActivity::class.java)
        intent.putExtra("image", img_path)
        requireActivity().startActivityForResult(intent, OCRAnimationActivity_REQUEST_CODE)
        try {
            recognizeImageAsyncTask = RecognizeImageStandalone(context, img_path)
            recognizeImageAsyncTask!!.setOnTaskCompletedListener(this)
            recognizeImageAsyncTask!!.execute()
        } catch (e: Exception) {
            Log.e(TAG, e.message!!)
        }
    }

    private val takePicture: Runnable = Runnable {
        ImageUtils.createImageFile(requireActivity())?.also {
            val imageUri = FileProvider.getUriForFile(
                requireActivity(),
                BuildConfig.APPLICATION_ID + ".provider",
                it
            )
            img_path = it.absolutePath
            takePictureActivityResultLauncher.launch(imageUri)
        }
    }

    /**
     * to get high resolution image from camera
     */
    private fun startCameraApp() {
        takePicture.run()
    }

    override fun onTaskCompleted(result: Array<String>) {
        requireActivity().finishActivity(OCRAnimationActivity_REQUEST_CODE)
        getInfoByENumbersArray(result)
    }

    override fun loadData() {
        showAllData()
    }

    private inner class FabClickHandler : View.OnClickListener {
        override fun onClick(v: View) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val firstOpened = preferences.getBoolean("first_opened", true)
            if (firstOpened) {
                showWelcomeScreen()
                val editor = preferences.edit()
                editor.putBoolean("first_opened", false)
                editor.apply()
            } else {
                startCameraApp()
            }
        }

        private fun showWelcomeScreen() {
            val dialog = OCRFirstRunDialogFragment()
            dialog.show(fragmentManager!!, "dialog")
            dialog.setOnSubmitListener { startCameraApp() }
        }
    }

    companion object {
        private const val OCRAnimationActivity_REQUEST_CODE = 2
        private const val TAG = "SearchByCodeFragment"
    }
}