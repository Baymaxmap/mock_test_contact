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
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var adapter: ContactAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //create recycler view to display UI
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_contacts)
        adapter = ContactAdapter(emptyList()) { contact ->
            // navigate to ContactDetailFragment when clicking item
            val action = ContactListFragmentDirections.actionContactListFragmentToContactDetailFragment(contact)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //create viewmodel to register observers when database changed
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(requireActivity(), contactViewModelFactory).get(ContactViewModel::class.java)

        //register observers to display all contacts on recycler view
        contactViewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            adapter.updateData(contacts)
        }

        // Observe filtered contacts to display specific contacts searched
        contactViewModel.filteredContacts.observe(viewLifecycleOwner) { contacts ->
            adapter.updateData(contacts)
        }

        //initialize UI when fragment is created, fetchContacts will call onChange of observer
        contactViewModel.fetchContacts()
    }

    override fun onResume() {
        super.onResume()
        contactViewModel.searchContacts("")
        if (contactViewModel.filteredContacts.value.isNullOrEmpty()) {
            contactViewModel.fetchContacts() // Lấy lại toàn bộ danh sách nếu không có kết quả tìm kiếm
        }
    }


    fun onSearchQuery(query: String) {
        contactViewModel.searchContacts(query)
    }

}
