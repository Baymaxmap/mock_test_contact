package com.example.contact_mock_test.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contact_mock_test.model.repository.ContactRepository
import com.example.contact_mock_test.viewmodel.ContactAddViewModel
import com.example.contact_mock_test.viewmodel.ContactDetailViewModel
import com.example.contact_mock_test.viewmodel.ContactEditViewModel
import com.example.contact_mock_test.viewmodel.ContactListViewModel

class ContactViewModelFactory(private val _repository: ContactRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(ContactListViewModel::class.java) -> (ContactListViewModel(_repository) as T)
            modelClass.isAssignableFrom(ContactAddViewModel::class.java) -> (ContactAddViewModel(_repository) as T)
            modelClass.isAssignableFrom(ContactDetailViewModel::class.java) -> (ContactDetailViewModel(_repository) as T)
            modelClass.isAssignableFrom(ContactEditViewModel::class.java) -> (ContactEditViewModel(_repository) as T)
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}