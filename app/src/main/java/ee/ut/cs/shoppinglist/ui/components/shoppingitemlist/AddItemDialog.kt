package ee.ut.cs.shoppinglist.ui.components.shoppingitemlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ee.ut.cs.shoppinglist.R
import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory
import ee.ut.cs.shoppinglist.ui.viewmodels.list.AddItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(vm: AddItemViewModel, onAdd: () -> Unit) {
    if (!vm.showAddDialog) return
    val uiState by vm.newItem.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var nameTapped by remember { mutableStateOf(false) }
    var quantityTapped by remember { mutableStateOf(false) }

    val nameError = nameTapped && uiState.name.isBlank()
    val qty = uiState.quantity.toIntOrNull()
    val qtyError = quantityTapped && (qty == null || qty < 1)

    val addEnabled = !nameError && !qtyError


    AlertDialog(
        onDismissRequest = { vm.closeAdd() },
        confirmButton = {
            TextButton(
                enabled = addEnabled,
                onClick = {
                    onAdd()
                    vm.closeAdd()
                }) { Text(stringResource(R.string.btn_add)) }
        },
        dismissButton = {
            TextButton(onClick = { vm.closeAdd() }) { Text(stringResource(R.string.btn_cancel)) }
        },
        title = { Text(stringResource(R.string.title_new_item_dialog)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = {
                        vm.updateName(it)
                        nameTapped = true
                    },
                    label = { Text(stringResource(R.string.item_name)) },
                    isError = nameError,
                    trailingIcon = {
                        if (nameError) Icon(Icons.Default.Warning, contentDescription = null)
                    },
                    supportingText = {
                        if (nameError) Text(stringResource(R.string.item_name_error))
                    },
                    singleLine = true
                )
                OutlinedTextField(
                    value = uiState.quantity,
                    onValueChange = {
                        vm.updateQuantity(it)
                        quantityTapped = true
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    trailingIcon = {
                        if (qtyError) Icon(Icons.Default.Warning, contentDescription = null)
                    },
                    label = { Text(stringResource(R.string.item_quantity)) },
                    singleLine = true,
                    isError = qtyError,
                    supportingText = {
                        if (qtyError) Text(stringResource(R.string.item_quantity_error))
                    }
                )

                // Category dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = uiState.category.toString(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.item_category)) },
                        trailingIcon = { TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.exposedDropdownSize()
                    ) {
                        ShoppingCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.toString()) },
                                onClick = {
                                    vm.updateCategory(category)
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
            }
        }
    )
}