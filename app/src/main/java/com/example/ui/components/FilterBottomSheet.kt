package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PropertyFilter
import com.example.data.model.PropertyPurpose
import com.example.data.model.PropertyType
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
  initialFilter: PropertyFilter,
  onDismiss: () -> Unit,
  onApplyFilter: (PropertyFilter) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var purpose by remember { mutableStateOf(initialFilter.purpose) }
  var propertyType by remember { mutableStateOf(initialFilter.type) }
  var selectedCity by remember { mutableStateOf(initialFilter.city) }
  var maxPriceValue by remember { mutableFloatStateOf(initialFilter.maxPrice.toFloat()) }
  var minBeds by remember { mutableIntStateOf(initialFilter.minBeds) }
  var verifiedOnly by remember { mutableStateOf(initialFilter.verifiedOnly) }

  val cities = listOf("All Cities", "Islamabad", "Lahore", "Karachi", "Rawalpindi")

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = MaterialTheme.colorScheme.surface,
    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 12.dp)
        .navigationBarsPadding()
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.FilterAlt,
            contentDescription = null,
            tint = EmeraldTeal
          )
          Text(
            text = "Filter Properties",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
        }

        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Section: Purpose (Buy / Rent / All)
      Text(
        text = "Purpose",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        FilterChip(
          selected = purpose == null,
          onClick = { purpose = null },
          label = { Text("All Purpose") },
          modifier = Modifier.testTag("filter_purpose_all"),
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldTeal,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
          )
        )
        FilterChip(
          selected = purpose == PropertyPurpose.BUY,
          onClick = { purpose = PropertyPurpose.BUY },
          label = { Text("Buy") },
          modifier = Modifier.testTag("filter_purpose_buy"),
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldTeal,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
          )
        )
        FilterChip(
          selected = purpose == PropertyPurpose.RENT,
          onClick = { purpose = PropertyPurpose.RENT },
          label = { Text("Rent") },
          modifier = Modifier.testTag("filter_purpose_rent"),
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = EmeraldTeal,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
          )
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section: City
      Text(
        text = "City",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        cities.forEach { city ->
          FilterChip(
            selected = selectedCity.equals(city, ignoreCase = true),
            onClick = { selectedCity = city },
            label = { Text(city) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldTeal,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section: Property Type
      Text(
        text = "Property Type",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        PropertyType.values().forEach { type ->
          FilterChip(
            selected = propertyType == type,
            onClick = { propertyType = type },
            label = { Text(type.displayName) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldTeal,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Section: Max Budget Slider
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Max Price Budget",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )
        val formattedMax = when {
          maxPriceValue >= 10_000_000 -> "Rs. ${(maxPriceValue / 10_000_000).toInt()} Crore"
          maxPriceValue >= 100_000 -> "Rs. ${(maxPriceValue / 100_000).toInt()} Lac"
          else -> "Rs. ${maxPriceValue.toLong()}"
        }
        Text(
          text = formattedMax,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = EmeraldTeal
        )
      }
      Slider(
        value = maxPriceValue,
        onValueChange = { maxPriceValue = it },
        valueRange = 50_000f..100_000_000f,
        steps = 20,
        colors = SliderDefaults.colors(
          thumbColor = EmeraldTeal,
          activeTrackColor = EmeraldTeal
        )
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Section: Bedrooms
      Text(
        text = "Bedrooms",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf(0 to "Any", 1 to "1+ BHK", 2 to "2+ BHK", 3 to "3+ BHK", 4 to "4+ BHK").forEach { (beds, label) ->
          FilterChip(
            selected = minBeds == beds,
            onClick = { minBeds = beds },
            label = { Text(label) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldTeal,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Verified Only Toggle
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Verified Listings Only",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
          )
          Text(
            text = "Show only 100% verified properties & clear titles",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Switch(
          checked = verifiedOnly,
          onCheckedChange = { verifiedOnly = it },
          colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = EmeraldTeal)
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Action Buttons: Reset & Apply
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedButton(
          onClick = {
            purpose = null
            propertyType = PropertyType.ALL
            selectedCity = "All Cities"
            maxPriceValue = 100_000_000f
            minBeds = 0
            verifiedOnly = false
          },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
          Text("Reset")
        }

        Button(
          onClick = {
            onApplyFilter(
              PropertyFilter(
                purpose = purpose,
                type = propertyType,
                city = selectedCity,
                maxPrice = maxPriceValue.toLong(),
                minBeds = minBeds,
                verifiedOnly = verifiedOnly,
                searchQuery = initialFilter.searchQuery
              )
            )
            onDismiss()
          },
          modifier = Modifier
            .weight(1.5f)
            .testTag("apply_filter_btn"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
        ) {
          Text("Apply Filters", fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}
