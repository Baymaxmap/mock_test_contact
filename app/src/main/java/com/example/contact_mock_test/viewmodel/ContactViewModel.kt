package com.example.contact_mock_test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.model.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(private val mRepository: ContactRepository): ViewModel() {
    private val mContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    val contacts: LiveData<List<Contact>>
        get() = mContacts

    //fetch all contacts
    fun fetchContacts(){
        viewModelScope.launch {
            val listContact = withContext(Dispatchers.IO) {
                mRepository.getAllContacts()
            }
            mContacts.postValue(listContact)
        }
    }

    //insert some contacts and also fetch data
    fun insertContact(contact: Contact){
        viewModelScope.launch {
            mRepository.insertContact(contact)
            fetchContacts()
        }
    }

    //delete contact and fetch data
    fun deleteContact(contact: Contact){
        viewModelScope.launch {
            mRepository.deleteContact(contact)
            fetchContacts()
        }
    }
}