package com.example.contact_mock_test.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.databinding.FragmentContactDetailBinding
import com.example.contact_mock_test.viewmodel.ContactDetailViewModel
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {
    private lateinit var contactViewModel: ContactDetailViewModel
    private lateinit var _binding : FragmentContactDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var contact = ContactDetailFragmentArgs.fromBundle(requireArguments()).contact

        //*************************** CREATING VIEWMODEL ******************************
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(this, contactViewModelFactory)
            .get(ContactDetailViewModel::class.java)

        //bind viewmodel and view
        _binding.viewModel = contactViewModel
        _binding.lifecycleOwner = viewLifecycleOwner

        //*************************** DISPLAY UI ******************************
        //fetch data to livedata in viewmodel => will call observer
        contactViewModel.fetchContactById(contact.id)

        //bind data between livedata and UI
        contactViewModel.contact.observe(viewLifecycleOwner){updateContact->
            _binding.contact = updateContact    //for displaying contact on detail fragment if data updated
        }

        //*************************** NAVIGATE TO CONTACT EDIT FRAGMENT ******************************
        contactViewModel.navigateToEditFragment.observe(viewLifecycleOwner){selectedContact->
            selectedContact?.let{
                val action = ContactDetailFragmentDirections.actionContactDetailFragmentToContactEditFragment(selectedContact)
                findNavController().navigate(action)
                contactViewModel.onEditFragmentNavigated()
            }
        }
    }
}
