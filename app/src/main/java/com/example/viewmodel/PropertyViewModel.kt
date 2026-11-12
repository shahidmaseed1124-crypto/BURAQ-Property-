package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.EmiCalculationResult
import com.example.data.model.FurnishingStatus
import com.example.data.model.Property
import com.example.data.model.PropertyFilter
import com.example.data.model.PropertyPurpose
import com.example.data.model.PropertyType
import com.example.data.model.VisitBooking
import com.example.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.pow

enum class AppScreen {
  HOME,
  EXPLORE,
  POST_PROPERTY,
  SAVED,
  VISITS,
  CALCULATOR,
  DETAIL,
  COMPARE
}

class PropertyViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: PropertyRepository

  init {
    val db = AppDatabase.getDatabase(application)
    repository = PropertyRepository(db)
  }

  // Navigation
  private val _currentScreen = MutableStateFlow(AppScreen.HOME)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val screenBackStack = mutableListOf<AppScreen>()

  private val _selectedProperty = MutableStateFlow<Property?>(null)
  val selectedProperty: StateFlow<Property?> = _selectedProperty.asStateFlow()

  // Properties and Favorites
  val allProperties: StateFlow<List<Property>> = repository.getAllProperties()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val favoriteIds: StateFlow<List<String>> = repository.getFavoritePropertyIds()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val visitBookings: StateFlow<List<VisitBooking>> = repository.getVisitBookings()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Filter State
  private val _filter = MutableStateFlow(PropertyFilter())
  val filter: StateFlow<PropertyFilter> = _filter.asStateFlow()

  // Filtered Properties
  val filteredProperties: StateFlow<List<Property>> = combine(allProperties, _filter) { properties, f ->
    properties.filter { p ->
      val matchesPurpose = f.purpose == null || p.purpose == f.purpose
      val matchesType = f.type == PropertyType.ALL || p.type == f.type
      val matchesCity = f.city == "All Cities" || p.city.equals(f.city, ignoreCase = true)
      val matchesPrice = p.price <= f.maxPrice
      val matchesBeds = f.minBeds == 0 || p.bedrooms >= f.minBeds
      val matchesVerified = !f.verifiedOnly || p.isVerified
      val matchesSearch = f.searchQuery.isBlank() ||
        p.title.contains(f.searchQuery, ignoreCase = true) ||
        p.address.contains(f.searchQuery, ignoreCase = true) ||
        p.city.contains(f.searchQuery, ignoreCase = true) ||
        p.type.displayName.contains(f.searchQuery, ignoreCase = true)

      matchesPurpose && matchesType && matchesCity && matchesPrice && matchesBeds && matchesVerified && matchesSearch
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Saved Properties List
  val savedProperties: StateFlow<List<Property>> = combine(allProperties, favoriteIds) { props, favs ->
    props.filter { favs.contains(it.id) }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // User's own properties
  val myProperties: StateFlow<List<Property>> = allProperties.combine(favoriteIds) { props, _ ->
    props.filter { it.isUserCreated }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Compare properties (up to 2 properties)
  private val _compareList = MutableStateFlow<List<Property>>(emptyList())
  val compareList: StateFlow<List<Property>> = _compareList.asStateFlow()

  // Toast / Status Notification
  private val _snackbarMessage = MutableSharedFlow<String>()
  val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

  // Navigation handlers
  fun navigateTo(screen: AppScreen) {
    if (_currentScreen.value != screen) {
      screenBackStack.add(_currentScreen.value)
      _currentScreen.value = screen
    }
  }

  fun navigateBack(): Boolean {
    return if (screenBackStack.isNotEmpty()) {
      _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
      true
    } else if (_currentScreen.value != AppScreen.HOME) {
      _currentScreen.value = AppScreen.HOME
      true
    } else {
      false
    }
  }

  fun openPropertyDetail(property: Property) {
    _selectedProperty.value = property
    navigateTo(AppScreen.DETAIL)
  }

  fun setFilter(newFilter: PropertyFilter) {
    _filter.value = newFilter
  }

  fun updateSearchQuery(query: String) {
    _filter.value = _filter.value.copy(searchQuery = query)
  }

  fun setPurposeFilter(purpose: PropertyPurpose?) {
    _filter.value = _filter.value.copy(purpose = purpose)
  }

  fun setTypeFilter(type: PropertyType) {
    _filter.value = _filter.value.copy(type = type)
  }

  fun setCityFilter(city: String) {
    _filter.value = _filter.value.copy(city = city)
  }

  fun resetFilter() {
    _filter.value = PropertyFilter()
  }

  // Favorite toggle
  fun toggleFavorite(propertyId: String) {
    viewModelScope.launch {
      repository.toggleFavorite(propertyId)
      val isSaved = favoriteIds.value.contains(propertyId)
      _snackbarMessage.emit(if (!isSaved) "Added to Saved Homes ❤️" else "Removed from Saved Homes")
    }
  }

  // Comparison
  fun toggleCompare(property: Property) {
    val current = _compareList.value.toMutableList()
    if (current.any { it.id == property.id }) {
      current.removeAll { it.id == property.id }
      _compareList.value = current
      viewModelScope.launch { _snackbarMessage.emit("Removed from Comparison") }
    } else {
      if (current.size >= 2) {
        current.removeAt(0)
      }
      current.add(property)
      _compareList.value = current
      viewModelScope.launch { _snackbarMessage.emit("Added to Comparison (2/2)") }
    }
  }

  fun clearCompare() {
    _compareList.value = emptyList()
  }

  // Post Property
  fun postProperty(
    title: String,
    description: String,
    price: Long,
    purpose: PropertyPurpose,
    type: PropertyType,
    bedrooms: Int,
    bathrooms: Int,
    areaSqFt: Int,
    address: String,
    city: String,
    ownerName: String,
    ownerPhone: String,
    furnishing: FurnishingStatus,
    amenities: List<String>
  ) {
    viewModelScope.launch {
      repository.insertUserProperty(
        title = title,
        description = description,
        price = price,
        purpose = purpose,
        type = type,
        bedrooms = bedrooms,
        bathrooms = bathrooms,
        areaSqFt = areaSqFt,
        address = address,
        city = city,
        ownerName = ownerName,
        ownerPhone = ownerPhone,
        furnishing = furnishing,
        amenities = amenities
      )
      _snackbarMessage.emit("Property listed successfully on Apna Ghar! 🏡")
      navigateTo(AppScreen.EXPLORE)
    }
  }

  fun deleteUserProperty(id: String) {
    viewModelScope.launch {
      repository.deleteUserProperty(id)
      _snackbarMessage.emit("Property listing removed.")
    }
  }

  // Book Visit
  fun bookVisit(
    property: Property,
    date: String,
    timeSlot: String,
    visitType: String,
    visitorName: String,
    visitorPhone: String,
    notes: String
  ) {
    viewModelScope.launch {
      repository.bookVisit(
        property = property,
        date = date,
        timeSlot = timeSlot,
        visitType = visitType,
        visitorName = visitorName,
        visitorPhone = visitorPhone,
        notes = notes
      )
      _snackbarMessage.emit("Visit scheduled for $date ($timeSlot)! 🎉")
    }
  }

  fun cancelVisit(bookingId: String) {
    viewModelScope.launch {
      repository.cancelVisit(bookingId)
      _snackbarMessage.emit("Scheduled visit cancelled.")
    }
  }

  // Send Inquiry
  fun sendInquiry(
    property: Property,
    name: String,
    phone: String,
    message: String
  ) {
    viewModelScope.launch {
      repository.sendInquiry(property, name, phone, message)
      _snackbarMessage.emit("Inquiry sent to ${property.ownerName}! They will contact you shortly.")
    }
  }

  // EMI Calculator Calculation Helper
  fun calculateEmi(
    propertyPrice: Long,
    downPaymentPercent: Double = 20.0,
    annualInterestRatePercent: Double = 11.5,
    tenureYears: Int = 20
  ): EmiCalculationResult {
    val downPayment = (propertyPrice * (downPaymentPercent / 100.0)).toLong()
    val principal = propertyPrice - downPayment
    if (principal <= 0) {
      return EmiCalculationResult(0, 0, 0, 0, downPayment)
    }

    val monthlyRate = (annualInterestRatePercent / 12.0) / 100.0
    val months = tenureYears * 12

    val emi = if (monthlyRate > 0) {
      val factor = (1 + monthlyRate).pow(months.toDouble())
      (principal * monthlyRate * factor / (factor - 1)).toLong()
    } else {
      principal / months
    }

    val totalPayment = emi * months
    val totalInterest = (totalPayment - principal).coerceAtLeast(0)

    return EmiCalculationResult(
      monthlyEmi = emi,
      totalInterest = totalInterest,
      totalPayment = totalPayment,
      principalAmount = principal,
      downPayment = downPayment
    )
  }
}
