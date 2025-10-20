package ee.ut.cs.shoppinglist.data.local.room.mapper

import ee.ut.cs.shoppinglist.domain.model.ShoppingCategory
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem
import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity

fun ShoppingItem.toEntity(): ShoppingItemEntity =
    ShoppingItemEntity(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = this.category.name,
        image = this.image,
        isBought = this.isBought
    )

fun ShoppingItemEntity.toDomain(): ShoppingItem =
    ShoppingItem(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = try {
            ShoppingCategory.valueOf(this.category)
        } catch (e: IllegalArgumentException) {
            ShoppingCategory.MISC
        },
        image = this.image,
        isBought = this.isBought
    )