package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FurnishingStatus
import com.example.data.model.PropertyPurpose
import com.example.data.model.PropertyType
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import com.example.viewmodel.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostPropertyScreen(
  viewModel: PropertyViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.navigateBack()
  }

  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var priceText by remember { mutableStateOf("") }
  var purpose by remember { mutableStateOf(PropertyPurpose.BUY) }
  var propertyType by remember { mutableStateOf(PropertyType.HOUSE) }
  var bedrooms by remember { mutableIntStateOf(3) }
  var bathrooms by remember { mutableIntStateOf(3) }
  var areaText by remember { mutableStateOf("2250") }
  var city by remember { mutableStateOf("Islamabad") }
  var address by remember { mutableStateOf("") }
  var ownerName by remember { mutableStateOf("") }
  var ownerPhone by remember { mutableStateOf("") }
  var furnishing by remember { mutableStateOf(FurnishingStatus.SEMI_FURNISHED) }

  val availableAmenities = listOf(
    "24/7 Security", "Covered Parking", "Power Backup", "Gym",
    "Lush Garden", "Modular Kitchen", "Solar Panels", "Servant Room"
  )
  val selectedAmenities = remember { mutableStateListOf("24/7 Security", "Covered Parking") }

  var errorMessage by remember { mutableStateOf<String?>(null) }

  val cities = listOf("Islamabad", "Lahore", "Karachi", "Rawalpindi", "Peshawar", "Multan")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Post Your Property",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        },
        navigationIcon = {
          IconButton(onClick = { viewModel.navigateBack() }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
      )
    },
    modifier = modifier.fillMaxSize()
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 12.dp)
        .navigationBarsPadding()
    ) {
      // Intro Banner
      Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Icon(
            imageVector = Icons.Default.AddHome,
            contentDescription = null,
            tint = EmeraldTeal,
            modifier = Modifier.size(32.dp)
          )
          Column {
            Text(
              text = "Sell or Rent on Apna Ghar",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
              text = "Connect directly with verified buyers and tenants with zero broker commission.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 1. Purpose & Property Type
      Text("I want to:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        FilterChip(
          selected = purpose == PropertyPurpose.BUY,
          onClick = { purpose = PropertyPurpose.BUY },
          label = { Text("Sell Property") },
          colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
        )
        FilterChip(
          selected = purpose == PropertyPurpose.RENT,
          onClick = { purpose = PropertyPurpose.RENT },
          label = { Text("Rent Out Property") },
          colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text("Property Type:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(
          PropertyType.HOUSE,
          PropertyType.APARTMENT,
          PropertyType.PENTHOUSE,
          PropertyType.PLOT,
          PropertyType.COMMERCIAL
        ).forEach { type ->
          FilterChip(
            selected = propertyType == type,
            onClick = { propertyType = type },
            label = { Text(type.displayName) },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // 2. Title & Pricing
      OutlinedTextField(
        value = title,
        onValueChange = { title = it; errorMessage = null },
        label = { Text("Listing Title *") },
        placeholder = { Text("e.g., Luxury 5-Marla Designer House in DHA") },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("post_title_input"),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = priceText,
        onValueChange = { priceText = it.filter { char -> char.isDigit() }; errorMessage = null },
        label = { Text(if (purpose == PropertyPurpose.BUY) "Total Demand Price (in Rs.) *" else "Monthly Rent Demand (in Rs.) *") },
        placeholder = { Text("e.g. 25000000") },
        leadingIcon = { Icon(imageVector = Icons.Default.AttachMoney, contentDescription = null) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("post_price_input"),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(16.dp))

      // 3. Location
      Text("City:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        cities.forEach { c ->
          FilterChip(
            selected = city == c,
            onClick = { city = c },
            label = { Text(c) },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = address,
        onValueChange = { address = it; errorMessage = null },
        label = { Text("Society / Sector / Street Address *") },
        placeholder = { Text("e.g. Sector G-13/2, Street 14") },
        leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("post_address_input"),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(16.dp))

      // 4. Specs: Beds, Baths, Area
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = areaText,
          onValueChange = { areaText = it.filter { c -> c.isDigit() } },
          label = { Text("Area (sq. ft.)") },
          leadingIcon = { Icon(imageVector = Icons.Default.SquareFoot, contentDescription = null) },
          modifier = Modifier.weight(1f),
          singleLine = true
        )
      }

      if (propertyType != PropertyType.PLOT && propertyType != PropertyType.COMMERCIAL) {
        Spacer(modifier = Modifier.height(12.dp))
        Text("Bedrooms:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          (1..5).forEach { num ->
            FilterChip(
              selected = bedrooms == num,
              onClick = { bedrooms = num },
              label = { Text("$num Bed") },
              colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Bathrooms:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          (1..5).forEach { num ->
            FilterChip(
              selected = bathrooms == num,
              onClick = { bathrooms = num },
              label = { Text("$num Bath") },
              colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 5. Furnishing
      Text("Furnishing Status:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FurnishingStatus.values().forEach { f ->
          FilterChip(
            selected = furnishing == f,
            onClick = { furnishing = f },
            label = { Text(f.displayName) },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 6. Amenities
      Text("Select Amenities Included:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        availableAmenities.forEach { amenity ->
          val isSelected = selectedAmenities.contains(amenity)
          FilterChip(
            selected = isSelected,
            onClick = {
              if (isSelected) selectedAmenities.remove(amenity) else selectedAmenities.add(amenity)
            },
            label = { Text(amenity) },
            leadingIcon = if (isSelected) {
              { Icon(imageVector = Icons.Default.Check, contentDescription = null) }
            } else null,
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 7. Description
      OutlinedTextField(
        value = description,
        onValueChange = { description = it },
        label = { Text("Detailed Description") },
        placeholder = { Text("Highlight key features, location benefits, nearby schools, parks, etc.") },
        modifier = Modifier.fillMaxWidth(),
        minLines = 3,
        maxLines = 5
      )

      Spacer(modifier = Modifier.height(20.dp))

      // 8. Contact Details
      Text("Your Contact Information:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = ownerName,
        onValueChange = { ownerName = it; errorMessage = null },
        label = { Text("Your Full Name *") },
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("post_owner_name_input"),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = ownerPhone,
        onValueChange = { ownerPhone = it; errorMessage = null },
        label = { Text("Your Phone / WhatsApp Number *") },
        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("post_owner_phone_input"),
        singleLine = true
      )

      if (errorMessage != null) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = errorMessage ?: "",
          color = MaterialTheme.colorScheme.error,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      Button(
        onClick = {
          val price = priceText.toLongOrNull() ?: 0L
          val area = areaText.toIntOrNull() ?: 1000
          if (title.isBlank()) {
            errorMessage = "Please enter a listing title."
          } else if (price <= 0) {
            errorMessage = "Please enter a valid price."
          } else if (address.isBlank()) {
            errorMessage = "Please enter the society or street address."
          } else if (ownerName.isBlank() || ownerPhone.isBlank()) {
            errorMessage = "Please provide your contact name and phone number."
          } else {
            viewModel.postProperty(
              title = title.trim(),
              description = if (description.isNotBlank()) description.trim() else "Brand new property in $city.",
              price = price,
              purpose = purpose,
              type = propertyType,
              bedrooms = bedrooms,
              bathrooms = bathrooms,
              areaSqFt = area,
              address = address.trim(),
              city = city,
              ownerName = ownerName.trim(),
              ownerPhone = ownerPhone.trim(),
              furnishing = furnishing,
              amenities = selectedAmenities.toList()
            )
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp)
          .testTag("submit_post_property_btn"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
      ) {
        Icon(imageVector = Icons.Default.AddHome, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text("Publish Listing on Apna Ghar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
