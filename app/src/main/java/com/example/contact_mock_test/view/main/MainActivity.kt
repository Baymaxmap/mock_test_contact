package com.example.contact_mock_test.view.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.view.fragment.ContactListFragment
import com.example.contact_mock_test.viewmodel.ContactViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tìm Toolbar trong layout và thiết lập làm ActionBar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Thiết lập Navigation Component
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Liên kết ActionBar với NavController
        setupActionBarWithNavController(navController)

        // Thay đổi icon khi destination thay đổi
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.contactListFragment -> {
                    // Icon menu cho ContactListFragment
                    toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.icon_menu)
                }
                else -> {
                    // Icon back cho các Fragment khác
                    toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.icon_back)
                }
            }
        }
        val app = applicationContext as ContactApp
        lifecycleScope.launch {
            if (app.isFirstRun()) {
                app.initApp() // Chỉ khởi tạo dữ liệu mẫu một lần
                app.setFirstRunComplete()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
    }

    // Gắn menu vào Toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu?.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as? SearchView

        // Hiển thị bàn phím ngay khi SearchView được kích hoạt
        searchView?.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchView.post {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT)
                }
            }
        }

        // Lắng nghe sự kiện tìm kiếm
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            private fun performSearch(query: String) {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
                val currentFragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
                if (currentFragment is ContactListFragment) {
                    currentFragment.onSearchQuery(query)
                }
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return true
            }
        })

        return true
    }







    // Xử lý sự kiện khi chọn menu item khác
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
