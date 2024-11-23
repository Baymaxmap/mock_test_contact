package com.example.contact_mock_test.model.repository

import androidx.lifecycle.LiveData
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.model.ContactDao

class ContactRepository(private val mContactDao: ContactDao) {
//    val contacts: List<Contact>
//        get() = mContactDao.getAllContacts()

    suspend fun getAllContacts() : List<Contact>{
        return mContactDao.getAllContacts()
    }

    suspend fun insertContact(contact: Contact){
        mContactDao.insertContact(contact)
    }

    suspend fun updateContact(contact: Contact){
        mContactDao.updateContact(contact)
    }

    suspend fun deleteContact(contact: Contact){
        mContactDao.deleteContact(contact)
    }

    suspend fun getContactById(id: Int) : Contact{
        return mContactDao.getContactById(id)
    }
}