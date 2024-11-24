package com.example.contact_mock_test.view.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.contact_mock_test.R
import com.example.contact_mock_test.application.ContactApp
import com.example.contact_mock_test.view.fragment.ContactListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // request permission of accessing photos in device when first time start
        requestStoragePermission()

        //find toolbar in layout and set up action bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // set up Navigation Component
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // link ActionBar with NavController
        setupActionBarWithNavController(navController)

        //change icon menu and back when destination changed
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.contactListFragment -> {
                    // Icon menu for ContactListFragment
                    toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.icon_menu)
                }
                else -> {
                    // Icon back for other Fragment
                    toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.icon_back)
                }
            }
        }

        //manage floating action button
        val fabAddContact = findViewById<FloatingActionButton>(R.id.fab_add_contact)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.contactListFragment -> {
                    // display FloatingActionButton in ContactListFragment
                    fabAddContact.show()
                }
                else -> {
                    // hide FloatingActionButton in other Fragments
                    fabAddContact.hide()
                    fabAddContact.visibility = View.GONE
                }
            }
        }

        //manage FloatingActionButton clicked => navigate to ContactAddFragment
        fabAddContact.setOnClickListener {
            navController.navigate(R.id.action_contactListFragment_to_contactAddFragment)
        }

        //initialize database when the app first time initialize
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

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_CODE
                )
            }
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE
                )
            }
        }
    }

}
