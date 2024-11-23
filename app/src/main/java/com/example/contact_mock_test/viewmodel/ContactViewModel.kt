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
    //attributes for display list of contact on UI
    private val mContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    val contacts: LiveData<List<Contact>>
        get() = mContacts

    //attribute to display a specific contact on UI
    private val _contact = MutableLiveData<Contact>()
    val contact: LiveData<Contact>
        get() = _contact

    //attribute to search a specific contact
    private val _filteredContacts = MutableLiveData<List<Contact>>() // Filtered contacts
    val filteredContacts: LiveData<List<Contact>> = _filteredContacts

    //************ FETCH CONTACTS FROM DATABASE TO LIVEDATA ATTRIBUTE************
    fun fetchContacts(){
        viewModelScope.launch {
            val listContact = withContext(Dispatchers.IO) {
                mRepository.getAllContacts()
            }
            mContacts.postValue(listContact)
            _filteredContacts.postValue(listContact)
        }
    }

    //************ UPDATE CONTACT TO DATABASE ************
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

    //load contact to notify all observers
    fun loadContactById(id: Int) {
        viewModelScope.launch {
            val contact = withContext(Dispatchers.IO) {
                mRepository.getContactById(id)
            }
            _contact.postValue(contact)
        }
    }

    //search a specific contact
    fun searchContacts(query: String) {
        val currentContacts = mContacts.value ?: emptyList()
        _filteredContacts.value = if (query.isBlank()) {
            currentContacts // Show all if query is empty
        } else {
            currentContacts.filter { contact ->
                contact.name.contains(query, ignoreCase = true) ||
                        contact.phoneNumber.contains(query, ignoreCase = true) ||
                        contact.email.contains(query, ignoreCase = true)
            }
        }
    }
}