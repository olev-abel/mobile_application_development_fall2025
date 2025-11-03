package ee.ut.cs.shoppinglist.data.remote.model

import ee.ut.cs.shoppinglist.domain.model.ShoppingItem

data class ShoppingItemDto(
    val id: String,
    val name: String,
    val quantity: Int,
    val category: String,
    val isBought: Boolean,
    val image: String?
) {
    fun toDomain(): ShoppingItem = ShoppingItem(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = try {
            ee.ut.cs.shoppinglist.domain.model.ShoppingCategory.valueOf(this.category)
        } catch (e: IllegalArgumentException) {
            ee.ut.cs.shoppinglist.domain.model.ShoppingCategory.MISC
        },
        image = this.image,
        isBought = this.isBought
    )
}
