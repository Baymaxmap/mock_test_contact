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

class ContactEditViewModel(private val _repository: ContactRepository): ViewModel() {
    private var _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun updateContact(contact: Contact){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _repository.updateContact(contact)
            }
        }
    }

    fun saveContact(contact: Contact){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _repository.updateContact(contact)
            }
            _navigateBack.value = true
        }
    }

    fun onNavigated(){
        _navigateBack.value = false
    }
}