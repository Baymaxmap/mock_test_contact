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

class ContactDetailViewModel(private val _repository: ContactRepository): ViewModel() {
    private var _contact: MutableLiveData<Contact> = MutableLiveData()
    val contact: LiveData<Contact>
        get() = _contact

    private var _navigateToEditFragment: MutableLiveData<Contact?> = MutableLiveData<Contact?>()
    val navigateToEditFragment
        get() = _navigateToEditFragment

    private val _navigateToListFragment = MutableLiveData<Boolean>()
    val navigateToListFragment: LiveData<Boolean>
        get() = _navigateToListFragment

    private val _navigateToCall = MutableLiveData<Boolean>()
    val navigateToCall: LiveData<Boolean>
        get() = _navigateToCall

    fun fetchContactById(id: Int){
        viewModelScope.launch {
            val contact = withContext(Dispatchers.IO){
                _repository.getContactById(id)
            }
            _contact.postValue(contact)
        }
    }

    fun onEditButtonClicked(contact: Contact){
        _navigateToEditFragment.value = contact
    }

    fun onEditFragmentNavigated() {
        _navigateToEditFragment.value = null
    }

    fun onDeleteButtonClicked(contact: Contact) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _repository.deleteContact(contact)
            }
            _navigateToListFragment.value = true
        }
    }

    fun onCallButtonClicked(){
        viewModelScope.launch {
            _navigateToCall.value = true
        }
    }
    fun onCallFinished(){
        viewModelScope.launch {
            _navigateToCall.value = false
        }
    }
}