package ee.ut.cs.shoppinglist.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val quantity: Int,
    val isBought: Boolean,
    val image: Int?,
    val category: String
)