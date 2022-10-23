package com.ovsyannikov.to_dolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ovsyannikov.to_dolist.item.Item
import com.ovsyannikov.to_dolist.model.ItemViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var itemViewModel: ItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        val recylerView: RecyclerView = findViewById(R.id.recycler_view)
        recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.setHasFixedSize(true)
        val adapter = ItemAdapter()
        recylerView.adapter = adapter

        val buttonAddItem: FloatingActionButton = findViewById(R.id.button_add_item)
        buttonAddItem.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, ADD_ITEM_REQUEST)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                itemViewModel.delete(adapter.getItemAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Item deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recylerView)

        adapter.setOnItemClickListener(object :
        ItemAdapter.OnItemClickListener {
            override fun onItemClick(item: Item?) {
                if (item != null) {
                    val intent = Intent(this@MainActivity, AddItemActivity::class.java)
                    intent.putExtra(AddItemActivity.ITEM_EXTRA_KEY, item)
                    startActivityForResult(intent, EDIT_ITEM_REQUEST)
                }
            }
        })

        itemViewModel.allItems.observe(
            this
        ) { items -> adapter.setItems(items) }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ITEM_REQUEST &&
                resultCode == RESULT_OK &&
                data != null) {
            val item = data.getSerializableExtra(AddItemActivity.ITEM_EXTRA_KEY) as Item
            itemViewModel.insert(item)
        } else if (requestCode == EDIT_ITEM_REQUEST &&
                resultCode === RESULT_OK &&
                data != null) {
            val item = data.getSerializableExtra(AddItemActivity.ITEM_EXTRA_KEY) as Item
            itemViewModel.update(item)
        } else {
            Toast.makeText(this, "Item not saved", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val ADD_ITEM_REQUEST = 1
        const val EDIT_ITEM_REQUEST = 2
    }
}