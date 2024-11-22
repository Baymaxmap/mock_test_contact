package com.example.contact_mock_test.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactAddFragment : Fragment(R.layout.fragment_contact_add) {

    private lateinit var contactViewModel: ContactViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(requireActivity(), contactViewModelFactory).get(ContactViewModel::class.java)

        // Bind UI elements
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val phoneEditText = view.findViewById<EditText>(R.id.phoneEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        // Handle Save Button Click
        saveButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a new Contact object
            val newContact = Contact(name = name, phoneNumber = phone, email = email, avatar = "")

            // Insert contact into the database
            contactViewModel.insertContact(newContact)

            // Navigate back to ContactListFragment
            findNavController().navigateUp()
        }
    }
}
