package com.example.contact_mock_test.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity (tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val avatar: String = "" // Đường dẫn hoặc URL của ảnh đại diện
) : Parcelable