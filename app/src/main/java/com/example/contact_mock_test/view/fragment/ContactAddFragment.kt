package com.example.contact_mock_test.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.databinding.FragmentContactAddBinding
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.viewmodel.ContactAddViewModel
import com.example.contact_mock_test.viewmodel.ContactEditViewModel
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactAddFragment : Fragment(R.layout.fragment_contact_add) {
    private lateinit var _binding: FragmentContactAddBinding
    private lateinit var contactViewModel: ContactAddViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactAddBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(this, contactViewModelFactory).get(ContactAddViewModel::class.java)

        val contact = Contact()
        // Bind the contact object to the UI (one-way binding)
        _binding.contact = contact  //display UI
        _binding.lifecycleOwner = viewLifecycleOwner

        // Handle "Select Image" button click
        _binding.selectImageButton.setOnClickListener {
            selectImageFromGallery()
        }

        //Handle "Save" button click
        _binding.saveButton.setOnClickListener {
            // Insert the new contact into the database
            contactViewModel.insertContact(contact)
            findNavController().navigateUp()
        }



//        contactViewModel.navigateBack.observe(viewLifecycleOwner){shouldNavigate->
//            if(shouldNavigate == true){
//                findNavController().navigateUp()
//                contactViewModel.doneSaveContact()
//            }
//        }
//
//        contactViewModel.selectImageEvent.observe(viewLifecycleOwner){selectedImage->
//                selectImageFromGallery()
//                contactViewModel.doneSelectingImage()
//        }

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
