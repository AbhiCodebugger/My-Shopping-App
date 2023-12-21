package com.example.myshoppingapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int, var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp() {
    var kItem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var isDialogOpen by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { isDialogOpen = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(kItem) { item ->
                if (item.isEditing) {
                    EditShoppingItem(item = item, onEditComplete = { editedName, editedQuantity ->
                        kItem = kItem.map { it.copy(isEditing = false) }
                        val editedItem = kItem.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                } else {
                    ShoppingListItem(item = item, onEditClick = {
                        kItem = kItem.map {
                            it.copy(
                                isEditing = it.id == item.id)}
                    }, onDeleteCLick = {
                        kItem = kItem-item
                    })
                }

            }
        }
    }
    if (isDialogOpen) {
        AlertDialog(
            title = {
                Text("Add Shopping Item")
            },
            text = {
                Column {
                    OutlinedTextField(

                        value = itemName, onValueChange = { itemName = it },
                        placeholder = {
                            Text(text = "Item Name")
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity, onValueChange = { itemQuantity = it },
                        placeholder = {
                            Text(text = "Quantity")
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                }
            },
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        if (itemName.isNotBlank()) {
                            val newItem = ShoppingItem(
                                id = kItem.size + 1,
                                name = itemName,
                                quantity = itemQuantity.toInt(),
                            )
                            kItem = kItem + newItem
                            isDialogOpen = false
                            itemName = ""
                            itemQuantity = ""
                        }

                    }) {
                        Text(text = "Add")
                    }
                    Button(onClick = { isDialogOpen = false }) {
                        Text(text = "Cancel")
                    }
                }
            },
        )
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteCLick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xff018786)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.Absolute.SpaceAround,
    )
    {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty-${item.quantity}", modifier = Modifier.padding(8.dp))
        IconButton(onClick = onEditClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Black
            )
        }
        IconButton(onClick = onDeleteCLick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun EditShoppingItem(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEdited by remember { mutableStateOf(item.isEditing) }

    Row {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
            )
        }
        Button(onClick = {
            isEdited = false
            onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
        }) {
            Text("Save")
        }
    }
}