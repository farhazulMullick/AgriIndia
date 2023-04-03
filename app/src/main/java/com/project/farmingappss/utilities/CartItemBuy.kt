package com.project.farmingappss.utilities

interface CartItemBuy {
    fun addToOrders(productId: String, quantity: Int, itemCost: Int, deliveryCost: Int)
}