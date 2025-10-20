package ee.ut.cs.shoppinglist

import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.ui.viewmodels.list.ShoppingListViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ShoppingListViewModelTest {

    @Test
    fun givenShoppingList_WhenAddNewItem_ThenItemAddedToList() {
        val vm = ShoppingListViewModel()
        assertEquals("Initial viewmodel items size",2, vm.items.value.size)
        vm.addItem(ShoppingItem(name = "Eggs", quantity = 6, category = ShoppingCategory.MISC))
        assertEquals("Item size after add one",2, vm.items.value.size)
    }
}