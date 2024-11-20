package com.example.contact_mock_test.view

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.contact_mock_test.R

class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val avatarImageView = view.findViewById<ImageView>(R.id.avatar_image_view)
        val nameTextView = view.findViewById<TextView>(R.id.contact_name_text)
        val phoneTextView = view.findViewById<TextView>(R.id.contact_phone_text)
        val emailTextView = view.findViewById<TextView>(R.id.contact_email_text)

        val contact = ContactDetailFragmentArgs.fromBundle(requireArguments()).contact

        nameTextView.text = contact.name
        phoneTextView.text = contact.phoneNumber
        emailTextView.text = contact.email
        avatarImageView.setImageResource(R.drawable.icon_avatar_background)

        val editButton = view.findViewById<ImageView>(R.id.editButton)
        editButton.setOnClickListener {
            // Điều hướng sang ContactEditFragment
            val action = ContactDetailFragmentDirections.actionContactDetailFragmentToContactEditFragment(contact)
            findNavController().navigate(action)
        }
    }
}
