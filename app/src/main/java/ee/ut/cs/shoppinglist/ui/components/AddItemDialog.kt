package ee.ut.cs.shoppinglist.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.ui.viewmodels.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(vm: ShoppingListViewModel) {
    if (!vm.showAddDialog) return

    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { vm.closeAdd() },
        confirmButton = {
            TextButton(onClick = { vm.saveNewItem() }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = { vm.closeAdd() }) { Text("Cancel") }
        },
        title = { Text("New item") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = vm.newItem.name,
                    onValueChange = { vm.newItem = vm.newItem.copy(name = it) },
                    label = { Text("Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = vm.newItem.quantity.toString(),
                    onValueChange = { s ->
                        s.toIntOrNull()?.let { vm.newItem = vm.newItem.copy(quantity = maxOf(1, it)) }
                    },
                    label = { Text("Quantity") },
                    singleLine = true
                )

                // Category dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = vm.newItem.category.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        vm.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.toString()) },
                                onClick = {
                                    vm.newItem = vm.newItem.copy(category = category)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}