package com.example.contact_mock_test.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.databinding.FragmentContactEditBinding
import com.example.contact_mock_test.viewmodel.ContactEditViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactEditFragment : Fragment(R.layout.fragment_contact_edit) {
    private lateinit var _contactViewModel: ContactEditViewModel
    private lateinit var _binding: FragmentContactEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactEditBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize ViewModel
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        _contactViewModel = ViewModelProvider(this, contactViewModelFactory).get(ContactEditViewModel::class.java)

        val contact = ContactEditFragmentArgs.fromBundle(requireArguments()).contact    // get contact from arguments
        //bind viewmodel and view
        _binding.viewModel = _contactViewModel
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.contact = contact  //display UI

        //*************************** SELECT IMAGE FROM DEVICE ******************************
        _contactViewModel.selectImageEvent.observe(viewLifecycleOwner) { shouldSelectImage ->
            if (shouldSelectImage == true) {
                selectImageFromGallery()
                _contactViewModel.doneSelectingImage() // Reset trạng thái
            }
        }

        //*************************** NAVIGATE BACK TO CONTACT DETAIL FRAGMENT ******************************
        _contactViewModel.navigateBack.observe(viewLifecycleOwner){shouldNavigate->
            if(shouldNavigate == true){
                findNavController().navigateUp()
            }
        }
    }


    //LOAD IMAGE
    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            val realPath = getRealPathFromUri(selectedImageUri!!)
            _binding.contact?.avatar = realPath // Cập nhật avatar trong contact
            _binding.invalidateAll() // Làm mới giao diện
        }
    }

    private fun getRealPathFromUri(uri: Uri): String {
        var path = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            path = cursor.getString(index)
            cursor.close()
        }
        return path
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1
    }
}
