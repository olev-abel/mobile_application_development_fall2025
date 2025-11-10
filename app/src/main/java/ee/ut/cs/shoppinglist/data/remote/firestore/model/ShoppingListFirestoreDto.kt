package ee.ut.cs.shoppinglist.data.remote.firestore.model

data class ShoppingListFirestoreDto(
    val id: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val category: String = "",
    val bought: Boolean = false,
    val image: String? = null
)