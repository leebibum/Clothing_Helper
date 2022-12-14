package com.leebeebeom.clothinghelperdomain.model

enum class Sort {
    NAME, CREATE, EDIT
}

enum class Order {
    ASCENDING, DESCENDING
}

data class SortPreferences(
    val sort: Sort = Sort.NAME,
    val order: Order = Order.ASCENDING
)