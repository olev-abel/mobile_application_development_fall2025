package ee.ut.cs.shoppinglist.data.remote.mapper

import ee.ut.cs.shoppinglist.data.local.room.entity.ShoppingItemEntity
import ee.ut.cs.shoppinglist.data.remote.model.ShoppingItemDto
import ee.ut.cs.shoppinglist.domain.model.ShoppingItem

fun ShoppingItem.toDTO(): ShoppingItemDto =
    ShoppingItemDto(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = this.category.name,
        image = this.image,
        isBought = this.isBought
    )

fun ShoppingItemDto.toEntity(): ShoppingItemEntity =
    ShoppingItemEntity(
        id = this.id,
        name = this.name,
        quantity = this.quantity,
        category = this.category,
        image = this.image,
        isBought = this.isBought
    )