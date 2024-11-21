package com.example.contact_mock_test.view.fragment

import android.os.Bundle
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
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactEditFragment : Fragment(R.layout.fragment_contact_edit) {
    private lateinit var contact: Contact
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var _binding: FragmentContactEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactEditBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get contact from arguments
        val contact = ContactEditFragmentArgs.fromBundle(requireArguments()).contact

        // initialize ViewModel
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(requireActivity(), contactViewModelFactory).get(ContactViewModel::class.java)

        //bind viewmodel and view
        _binding.viewModel = contactViewModel
        _binding.lifecycleOwner = viewLifecycleOwner
        contactViewModel.loadContactById(contact.id)
        contactViewModel.contact.observe(viewLifecycleOwner){updateContact->
            _binding.contact = updateContact
        }

        //handle event click button
        val saveButton = view.findViewById<Button>(R.id.saveButton)
        saveButton.setOnClickListener {
            // update inf
            val updatedContact = contact.copy(
                name = _binding.nameEditText.text.toString(),
                phoneNumber = _binding.phoneEditText.text.toString(),
                email = _binding.emailEditText.text.toString()
            )
            contactViewModel.updateContact(updatedContact)  // update into database
            findNavController().navigateUp()                // back to ContactListFragment
        }
    }
}
