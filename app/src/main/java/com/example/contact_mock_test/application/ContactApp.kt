package com.example.contact_mock_test.application

import android.app.Application
import com.example.contact_mock_test.model.database.AppDatabase
import com.example.contact_mock_test.model.Contact
import com.example.contact_mock_test.model.repository.ContactRepository

class ContactApp: Application() {
    lateinit var database: AppDatabase
    lateinit var contactRepository: ContactRepository

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        contactRepository = ContactRepository(database.contactDao())

    }

    suspend fun initApp(){
        contactRepository.insertContact(Contact( name ="Thanh1", phoneNumber = "123", email =  "thanh1@", avatar = "/home/thanhtb5/Downloads/avt/avt.jpg"))
        contactRepository.insertContact(Contact( name ="Thanh2", phoneNumber = "234", email =  "thanh2@", avatar = "/home/thanhtb5/Downloads/avt/avt1.jpg"))
        contactRepository.insertContact(Contact( name ="Thanh3", phoneNumber = "345", email =  "thanh3@", avatar = "/home/thanhtb5/Downloads/avt/avt2.jpg"))
        contactRepository.insertContact(Contact( name ="Thanh4", phoneNumber = "456", email =  "thanh4@", avatar = "/home/thanhtb5/Downloads/avt/avt3.jpg"))
        contactRepository.insertContact(Contact( name ="Thanh5", phoneNumber = "567", email =  "thanh5@", avatar = "/home/thanhtb5/Downloads/avt/avt4.png"))
        contactRepository.insertContact(Contact( name ="Thanh6", phoneNumber = "678", email =  "thanh6@", avatar = "/home/thanhtb5/Downloads/avt/avt5.png"))
        contactRepository.insertContact(Contact( name ="Thanh7", phoneNumber = "789", email =  "thanh7@", avatar = "/home/thanhtb5/Downloads/avt/avt6.jpg"))
    }
    fun isFirstRun(): Boolean {
        val prefs = getSharedPreferences("app_preferences", MODE_PRIVATE)
        return prefs.getBoolean("is_first_run", true)
    }

    fun setFirstRunComplete() {
        val prefs = getSharedPreferences("app_preferences", MODE_PRIVATE)
        prefs.edit().putBoolean("is_first_run", false).apply()
    }
}