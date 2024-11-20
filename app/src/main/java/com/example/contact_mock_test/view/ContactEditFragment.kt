package com.example.contact_mock_test.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactEditFragment : Fragment(R.layout.fragment_contact_edit) {
    private lateinit var contact: Contact
    private lateinit var contactViewModel: ContactViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy dữ liệu contact từ arguments
        contact = arguments?.getParcelable("contact") ?: return

        // Khởi tạo ViewModel
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(this, contactViewModelFactory).get(ContactViewModel::class.java)

        // Gán dữ liệu vào form
        val nameEditText = view.findViewById<EditText>(R.id.nameEditText)
        val phoneEditText = view.findViewById<EditText>(R.id.phoneEditText)
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        nameEditText.setText(contact.name)
        phoneEditText.setText(contact.phoneNumber)
        emailEditText.setText(contact.email)

        // Nút lưu
        val saveButton = view.findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            // Cập nhật thông tin
            val updatedContact = contact.copy(
                name = nameEditText.text.toString(),
                phoneNumber = phoneEditText.text.toString(),
                email = emailEditText.text.toString()
            )

            // Lưu vào database
            contactViewModel.updateContact(updatedContact)

            // Quay lại ContactDetailFragment
            findNavController().navigateUp()
        }
    }
}
