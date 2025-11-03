package ee.ut.cs.shoppinglist.data.remote.model


data class ShoppingItemDto(
    val id: String,
    val name: String,
    val quantity: Int,
    val category: String,
    val isBought: Boolean,
    val image: String?
)
