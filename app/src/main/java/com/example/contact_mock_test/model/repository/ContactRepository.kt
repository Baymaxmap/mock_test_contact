package com.example.contact_mock_test.model.repository

import androidx.lifecycle.LiveData
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.model.ContactDao

class ContactRepository(private val _contactDao: ContactDao) {

    suspend fun getAllContacts() : List<Contact>{
        return _contactDao.getAllContacts()
    }

    suspend fun insertContact(contact: Contact){
        _contactDao.insertContact(contact)
    }

    suspend fun updateContact(contact: Contact){
        _contactDao.updateContact(contact)
    }

    suspend fun deleteContact(contact: Contact){
        _contactDao.deleteContact(contact)
    }

    suspend fun getContactById(id: Int) : Contact{
        return _contactDao.getContactById(id)
    }
}