package com.example.contact_mock_test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.model.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(private val mRepository: ContactRepository): ViewModel() {
    private val mContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    val contacts: LiveData<List<Contact>>
        get() = mContacts

    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact>
        get() = _contact

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

    //update a contact
    fun updateContact(contact: Contact){
        viewModelScope.launch {
            _contact.postValue(contact)
            mRepository.updateContact(contact)
            fetchContacts()
        }
    }

    //get a contact by ID
    fun getContactById(id: Int): Contact{
        var dataContact: Contact = Contact()
        viewModelScope.launch {
            dataContact = mRepository.getContactById(id)
        }
        return dataContact
    }

    fun loadContactById(id: Int) {
        viewModelScope.launch {
            val contact = mRepository.getContactById(id) // Trả về Contact
            _contact.postValue(contact)
        }
    }
}