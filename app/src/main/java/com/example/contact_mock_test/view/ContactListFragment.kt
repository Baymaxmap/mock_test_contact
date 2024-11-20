package com.example.contact_mock_test.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var adapter: ContactAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_contacts)
        adapter = ContactAdapter(emptyList()) { contact ->
            // Điều hướng sang ContactDetailFragment khi click vào liên hệ
            val action = ContactListFragmentDirections.actionContactListFragmentToContactDetailFragment(contact)
            findNavController().navigate(action)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(this, contactViewModelFactory).get(ContactViewModel::class.java)

        contactViewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            adapter.updateData(contacts)
        }

        contactViewModel.fetchContacts()
    }
}
