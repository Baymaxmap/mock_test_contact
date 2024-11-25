package com.example.contact_mock_test.view.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.view.ContactAdapter
import com.example.contact_mock_test.viewmodel.ContactListViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    private lateinit var _contactViewModel: ContactListViewModel
    private lateinit var _adapter: ContactAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //******************************** DISPLAY CONTACT LIST **********************************
        //create recycler view to display UI
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_contacts)
        _adapter = ContactAdapter(emptyList()) { contact ->
            // navigate to ContactDetailFragment when clicking item
            val action = ContactListFragmentDirections.actionContactListFragmentToContactDetailFragment(contact)
            findNavController().navigate(action)
        }
        recyclerView.adapter = _adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //******************************** CONTACT LIST CHANGE UI WHEN ANY CONTACT UPDATED **********************************
        //create viewmodel to register observers when database changed
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        _contactViewModel = ViewModelProvider(this, contactViewModelFactory).get(ContactListViewModel::class.java)

        //register observers to display all contacts on recycler view when contact get updated
        _contactViewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            _adapter.updateData(contacts)
        }

        //******************************** CONTACT SEARCH TO DISPLAY UI **********************************
        // Observe filtered contacts to display specific contacts searched
        _contactViewModel.filteredContacts.observe(viewLifecycleOwner) { contacts ->
            _adapter.updateData(contacts)
        }

        //initialize UI when fragment is created, fetchContacts will call onChange of observer
        _contactViewModel.fetchContacts()
    }


    override fun onResume() {
        super.onResume()
        _contactViewModel.fetchContacts()
    }

    fun onSearchQuery(query: String) {
        _contactViewModel.searchContacts(query)
    }

}
