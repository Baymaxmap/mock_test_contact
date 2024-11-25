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
import com.example.contact_mock_test.databinding.ItemContactBinding
import com.example.contact_mock_test.model.Contact
import java.io.File

class ContactAdapter(
    private var _contacts: List<Contact>,
    private val _onClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(private val _binding: ItemContactBinding) : RecyclerView.ViewHolder(_binding.root) {
        fun bind(contact: Contact) {
            // Gán dữ liệu cho binding
            _binding.contact = contact // Gán contact vào variable trong item_contact.xml
            _binding.executePendingBindings() // Thực thi binding ngay lập tức

            // Xử lý click vào item
            _binding.root.setOnClickListener {
                _onClick(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(_contacts[position])
    }

    override fun getItemCount(): Int = _contacts.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newContacts: List<Contact>) {
        _contacts = newContacts
        notifyDataSetChanged()
    }
}

