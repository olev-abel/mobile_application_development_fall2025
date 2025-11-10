package ee.ut.cs.shoppinglist.data.remote.firestore.mapper

import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity
import ee.ut.cs.shoppinglist.data.remote.firestore.model.ShoppingListFirestoreDto
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem


fun ShoppingListFirestoreDto.toEntity(): ShoppingItemEntity =
    ShoppingItemEntity(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = this.category,
        isBought = this.bought,
        image = this.image
    )

fun ShoppingItem.toFirestoreDto(): ShoppingListFirestoreDto =
    ShoppingListFirestoreDto(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = this.category.name,
        bought = this.isBought,
        image = this.image
    )
