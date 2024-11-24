package com.example.contact_mock_test.view.fragment

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.databinding.FragmentContactDetailBinding
import com.example.contact_mock_test.viewmodel.ContactDetailViewModel
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

        //*************************** NAVIGATE TO LIST FRAGMENT ******************************
        contactViewModel.navigateToListFragment.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                findNavController().navigateUp()
            }
        }

        //*************************** CALL ACTION ******************************`
        contactViewModel.navigateToCall.observe(viewLifecycleOwner){shouldCall->
            if(shouldCall==true){
                showCallConfirmationDialog(contact.phoneNumber){
                    contactViewModel.onCallFinished()
                }
            }
        }
    }



    // Hiển thị hộp thoại xác nhận trước khi gọi
    private fun showCallConfirmationDialog(phoneNumber: String, onDialogFinished: ()->Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Call Contact")
            .setMessage("Do you want to call $phoneNumber?")
            .setPositiveButton("Yes") { _, _ ->
                callPhoneNumber(phoneNumber)
            }
            .setNegativeButton("No", null)
            .show()
        onDialogFinished()
    }

    // Mở ứng dụng gọi điện thoại với ACTION_DIAL
    private fun callPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "No dialer app found", Toast.LENGTH_SHORT).show()
        }
    }
}
