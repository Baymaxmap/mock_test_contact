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
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.databinding.FragmentContactAddBinding
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactAddFragment : Fragment(R.layout.fragment_contact_add) {
    private lateinit var binding: FragmentContactAddBinding
    private lateinit var contactViewModel: ContactViewModel
    private var contact = Contact() // Local contact object

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactAddBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(requireActivity(), contactViewModelFactory).get(ContactViewModel::class.java)

        // Bind the contact object to the UI (one-way binding)
        binding.contact = contact
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle "Select Image" button click
        binding.selectImageButton.setOnClickListener {
            selectImageFromGallery()
        }

        // Handle "Save" button click
        binding.saveButton.setOnClickListener {
            // Update contact object with data from EditTexts
            contact.name = binding.nameEditText.text.toString()
            contact.phoneNumber = binding.phoneEditText.text.toString()
            contact.email = binding.emailEditText.text.toString()

            // Insert the new contact into the database
            contactViewModel.insertContact(contact)
            findNavController().navigateUp()
        }
    }

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
            contact.avatar = realPath // Update the contact object manually
            binding.contact = contact // Update UI
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
