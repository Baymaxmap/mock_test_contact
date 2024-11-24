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

class ContactAddViewModel(private val _repository: ContactRepository): ViewModel() {
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    private val _selectImageEvent = MutableLiveData<Boolean>()
    val selectImageEvent: LiveData<Boolean> get() = _selectImageEvent

    fun updateContact(contact: Contact){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _repository.updateContact(contact)
            }
        }
    }

    fun insertContact(contact: Contact){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                _repository.insertContact(contact)
                _navigateBack.postValue(true)
            }
        }
    }

    fun onSaveContactClick(contact: Contact){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _repository.insertContact(contact)
            }
            _navigateBack.postValue(true)
        }
    }

    fun doneSaveContact(){
        _navigateBack.value = false
    }

    fun onSelectImageClick() {
        _selectImageEvent.value = true
    }

    fun doneSelectingImage() {
        _selectImageEvent.value = false
    }
}