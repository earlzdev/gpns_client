package com.earl.gpns.ui.profile

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.databinding.FragmentProfileBinding
import com.earl.gpns.ui.SearchFormsDetails
import com.earl.gpns.ui.about.AboutAppFragment
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.BitmapFromStringDecoder
import com.earl.gpns.ui.core.Keys
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private lateinit var viewModel: ProfileViewModel
    var encodedImage: String? = null

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentProfileBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        initViews()
    }

    private fun initViews() {
        with(binding.userAvatar) {
            val preferenceManagerImage = preferenceManager.getString(Keys.KEY_IMAGE) ?: ""
            if (preferenceManagerImage.isNotEmpty()) {
                setImageBitmap(BitmapFromStringDecoder().decode(preferenceManagerImage))
            }
        }
        binding.userName.text = preferenceManager.getString(Keys.KEY_NAME)
        binding.exitBtn.setOnClickListener {
            logOut()
        }
        binding.myTripForm.setOnClickListener {
            if (preferenceManager.getBoolean(Keys.HAS_SEARCH_FORM)) {
                if (preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                    viewModel.fetchDriverFormFromLocalDb()
                    viewModel.observeDriverTripFormLiveData(this) {
                        navigator.driverFormDetails(it.provideDriverFormDetailsUi() as SearchFormsDetails, OWN_TRIP_FORM, "")
                    }
                } else {
                    viewModel.fetchCompanionFormFromDb()
                    viewModel.observeCompanionTripFormLiveData(this) {
                        navigator.companionFormDetails(it.provideCompanionDetailsUi() as SearchFormsDetails, OWN_TRIP_FORM, "")
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.u_dont_have_comp_form), Toast.LENGTH_SHORT).show()
            }
        }
        binding.communicateWithDeveloper.setOnClickListener {
            if (preferenceManager.getBoolean(Keys.IS_DRIVER)) {
                viewModel.deleteDriverFormForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
            } else {
                viewModel.deleteCompanionForm(preferenceManager.getString(Keys.KEY_JWT) ?: "")
            }
            preferenceManager.putBoolean(Keys.HAS_SEARCH_FORM, false)
            preferenceManager.putBoolean(Keys.IS_DRIVER, false)
            preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, false)
        }
        binding.safePolitics.setOnClickListener { navigator.privacyPolicyFragment() }
        binding.aboutApp.setOnClickListener {
            AboutAppFragment.newInstance().show(parentFragmentManager.beginTransaction(), AboutAppFragment.TAG)
        }
        binding.shareBtn.setOnClickListener { share() }
        binding.rate.setOnClickListener { rate() }
        binding.communicateWithDeveloper.setOnClickListener { communicateWithDev() }
        binding.newAvatarBtn.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }
    }

    private fun logOut() {
        navigator.showProgressBar()
        preferenceManager.clear()
        preferenceManager.putBoolean(Keys.KEY_IS_SIGNED_UP, false)
        viewModel.logOut()
        navigator.hideProgressBar()
        navigator.start()
    }

    private fun share() {
        val email = Intent(Intent.ACTION_SEND)
        email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.search_comps_app))
        email.putExtra(
            Intent.EXTRA_TEXT, getString(R.string.recommend) +
                    "\n ${"https://play.google.com/store/apps/details?id=${requireActivity().packageName}"}")
        email.type = getString(R.string.msg_interface)
        startActivity(Intent.createChooser(email, getString(R.string.choose_email_clinet)))
    }

    private fun rate() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireActivity().packageName}")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${requireActivity().packageName}")))
        }
    }

    private fun communicateWithDev() {
        val intent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts(getString(R.string.mailto), getString(R.string.my_email), null)
        )
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.search_comps_appp))
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.choose_messenger)))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                requireActivity(),
                R.string.no_emails_client,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun encodeImage(bitmap: Bitmap): String {
        val previewWidth = 150
        val previewHeight = bitmap.height * previewWidth / bitmap.width
        val previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val imageUri = result.data!!.data
                try {
                    val inputStream =
                        requireContext().contentResolver.openInputStream(
                            imageUri!!
                        )
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.userAvatar.setImageBitmap(bitmap)
                    encodedImage = encodeImage(bitmap)
                    preferenceManager.putString(Keys.KEY_IMAGE, encodeImage(bitmap))
                    viewModel.updateUserAvatar(
                        preferenceManager.getString(Keys.KEY_JWT) ?: "",
                        encodeImage(bitmap)
                    )
                } catch (exception: FileNotFoundException) {
                    exception.printStackTrace()
                }
            }
        }
    }

    companion object {

        fun newInstance() = ProfileFragment()
        private const val OWN_TRIP_FORM = "OWN_TRIP_FORM"
    }
}