package com.example.contact_mock_test.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.contact_mock_test.R

class BindingAdapters {
    companion object {
        @JvmStatic
        @BindingAdapter("app:loadImage")
        fun loadImage(view: ImageView, imagePath: String?) {
            if (!imagePath.isNullOrEmpty()) {
                Glide.with(view.context)
                    .load(imagePath) // Đường dẫn hoặc URL ảnh
                    .placeholder(R.drawable.icon_avatar_background) // Ảnh mặc định khi đang load
                    .error(R.drawable.icon_avatar_background) // Ảnh lỗi
                    .into(view) // Đổ ảnh vào ImageView
            } else {
                view.setImageResource(R.drawable.icon_avatar_background) // Ảnh mặc định khi không có ảnh
            }
        }
    }
}