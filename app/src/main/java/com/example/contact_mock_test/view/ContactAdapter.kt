package com.example.contact_mock_test.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contact_mock_test.R
import com.example.contact_mock_test.model.Contact
import java.io.File

class ContactAdapter(
    private var contacts: List<Contact>,
    private val onClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarImageView: ImageView = itemView.findViewById(R.id.avatarImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(contact: Contact) {
            // Gán dữ liệu vào View
            nameTextView.text = contact.name
            avatarImageView.setImageResource(R.drawable.icon_avatar_background)
            // Sử dụng ảnh từ URL hoặc file
            // Nếu avatar là URL, dùng Glide hoặc Picasso
            if (contact.avatar.isNotEmpty()) {
                // Sử dụng Glide để tải ảnh từ URL
                Glide.with(itemView.context)
                    .load(File(contact.avatar)) // URL hoặc đường dẫn ảnh
                    .placeholder(R.drawable.icon_avatar_background) // Ảnh mặc định
                    .into(avatarImageView)
            } else {
                // Hiển thị ảnh mặc định
                avatarImageView.setImageResource(R.drawable.icon_avatar_background)
            }

            // Xử lý click vào item
            itemView.setOnClickListener {
                onClick(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }
}
