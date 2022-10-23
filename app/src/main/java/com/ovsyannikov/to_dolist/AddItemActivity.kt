package com.ovsyannikov.to_dolist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ovsyannikov.to_dolist.databinding.ActivityAddItemBinding
import com.ovsyannikov.to_dolist.item.Item

class AddItemActivity : AppCompatActivity() {


    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priorityCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        titleEditText = findViewById(R.id.titleEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        priorityCheckBox = findViewById(R.id.priorityCheckBox)

        val item = intent.getSerializableExtra(ITEM_EXTRA_KEY) as? Item
        if (item != null) {
            title = "Edit Item"
            titleEditText.setText(item.title)
            descriptionEditText.setText(item.description)
            priorityCheckBox.isChecked = item.priority
        } else {
            title = "Add Item"
        }
    }

    private fun saveItem() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val checked = priorityCheckBox.isChecked

        if (title.isBlank() || description.isBlank()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }
        val result = Item(title, description, checked)
        val id = (intent.getSerializableExtra(ITEM_EXTRA_KEY) as? Item)?.id
        if (id != null) {
            result.id = id
        }
        val data = Intent()
        data.putExtra(ITEM_EXTRA_KEY, result)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            R.id.save_item -> {
                saveItem()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val ITEM_EXTRA_KEY = "ru.asselinux.workshop"
    }

}