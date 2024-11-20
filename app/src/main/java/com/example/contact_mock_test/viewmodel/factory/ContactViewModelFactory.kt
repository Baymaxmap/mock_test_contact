package com.example.contact_mock_test.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contact_mock_test.model.repository.ContactRepository
import com.example.contact_mock_test.viewmodel.ContactViewModel

class ContactViewModelFactory(private val mRepository: ContactRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ContactViewModel::class.java)){
            return ContactViewModel(mRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}