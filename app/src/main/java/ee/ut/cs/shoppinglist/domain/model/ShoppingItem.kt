package ee.ut.cs.shoppinglist.domain.model

import java.util.UUID

data class ShoppingItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: Int,
    val category: ShoppingCategory,
    val isBought: Boolean = false,
    val image: String? = null,
)
