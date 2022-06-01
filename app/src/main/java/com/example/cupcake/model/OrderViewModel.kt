package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_PER_DAY_PICKUP = 3.00

class OrderViewModel : ViewModel() {

    private val _orderQuantity = MutableLiveData<Int>()
    val orderQuantity: LiveData<Int> =_orderQuantity

    private val _cupcakeFlavor = MutableLiveData<String>()
    val cupcakeFlavor: LiveData<String> = _cupcakeFlavor

    private val _pickupDate = MutableLiveData<String>()
    val pickupDate: LiveData<String> = _pickupDate

    private val _price = MutableLiveData<Double>()
    val price: LiveData<String> = Transformations.map(_price) {
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickupOptions()


    init {
        resetOrder()
    }

    fun setQuantity(numberCupcakes: Int){
        _orderQuantity.value = numberCupcakes
        updatePrice()
    }
    fun setFlavor(desiredFlavor: String){
        _cupcakeFlavor.value = desiredFlavor
    }
    fun setDate(pickupDate: String){
        _pickupDate.value = pickupDate
        updatePrice()
    }
    fun hasNoFlavorSet(): Boolean{
        return _cupcakeFlavor.value.isNullOrEmpty()
    }
    private fun getPickupOptions(): List<String>{
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        // Create a list of dates starting with the current date and the following 3 dates
        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return options
    }
    fun resetOrder(){
        _orderQuantity.value = 0
        _cupcakeFlavor.value = ""
        _pickupDate.value = dateOptions[0]
        _price.value = 0.0
    }

    private fun updatePrice(){
        var calculatedPrice = (_orderQuantity.value?: 0) * PRICE_PER_CUPCAKE
        if(dateOptions[0] == _pickupDate.value){
            calculatedPrice += PRICE_PER_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

}