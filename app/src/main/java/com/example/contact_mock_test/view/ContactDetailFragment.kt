package com.example.contact_mock_test.view

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
import com.example.contact_mock_test.viewmodel.ContactViewModel
import com.example.contact_mock_test.viewmodel.factory.ContactViewModelFactory

class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {
    private lateinit var contactViewModel: ContactViewModel
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

        val avatarImageView = view.findViewById<ImageView>(R.id.avatar_image_view)
        val nameTextView = view.findViewById<TextView>(R.id.contact_name_text)
        val phoneTextView = view.findViewById<TextView>(R.id.contact_phone_text)
        val emailTextView = view.findViewById<TextView>(R.id.contact_email_text)

        val contact = ContactDetailFragmentArgs.fromBundle(requireArguments()).contact

        avatarImageView.setImageResource(R.drawable.icon_avatar_background)

        //when data change by editing, UI is updated
        val repository = (requireActivity().application as ContactApp).contactRepository
        val contactViewModelFactory = ContactViewModelFactory(repository)
        contactViewModel = ViewModelProvider(requireActivity(), contactViewModelFactory).get(ContactViewModel::class.java)

        //bind viewmodel and view => when database change, UI change
        _binding.viewModel = contactViewModel
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.contact = contact

        //navigate to edit fragment
        val editButton = view.findViewById<ImageView>(R.id.editButton)
        editButton.setOnClickListener {
            // navigate ContactEditFragment
            val action = ContactDetailFragmentDirections.actionContactDetailFragmentToContactEditFragment(contact)
            findNavController().navigate(action)
        }
    }
}
