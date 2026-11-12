package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.model.PropertyPurpose
import com.example.data.model.PropertyType
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.PropertyCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import com.example.viewmodel.PropertyViewModel

@Composable
fun ExploreScreen(
  viewModel: PropertyViewModel,
  modifier: Modifier = Modifier
) {
  val properties by viewModel.filteredProperties.collectAsState()
  val favoriteIds by viewModel.favoriteIds.collectAsState()
  val filter by viewModel.filter.collectAsState()

  var showFilterSheet by remember { mutableStateOf(false) }

  val activeFilterCount = remember(filter) {
    var count = 0
    if (filter.purpose != null) count++
    if (filter.type != PropertyType.ALL) count++
    if (filter.city != "All Cities") count++
    if (filter.maxPrice < 100_000_000L) count++
    if (filter.minBeds > 0) count++
    if (filter.verifiedOnly) count++
    count
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Header & Search Box
    Surface(
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 2.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = filter.searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = { Text("Search location, title, city...", fontSize = 13.sp) },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = EmeraldTeal)
            },
            trailingIcon = {
              if (filter.searchQuery.isNotEmpty()) {
                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                  Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                }
              }
            },
            modifier = Modifier
              .weight(1f)
              .testTag("explore_search_bar"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
              unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
              focusedBorderColor = EmeraldTeal,
              unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true
          )

          // Filter Button with Badge
          BadgedBox(
            badge = {
              if (activeFilterCount > 0) {
                Badge(
                  containerColor = AmberGold,
                  contentColor = Color.White
                ) {
                  Text("$activeFilterCount")
                }
              }
            }
          ) {
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = if (activeFilterCount > 0) EmeraldTeal else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier
                .size(50.dp)
                .testTag("explore_filter_btn")
            ) {
              IconButton(onClick = { showFilterSheet = true }, modifier = Modifier.fillMaxSize()) {
                Icon(
                  imageVector = Icons.Default.FilterList,
                  contentDescription = "Filters",
                  tint = if (activeFilterCount > 0) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Purpose Quick Chips (All / Buy / Rent)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = filter.purpose == null,
            onClick = { viewModel.setPurposeFilter(null) },
            label = { Text("All") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldTeal,
              selectedLabelColor = Color.White
            )
          )
          FilterChip(
            selected = filter.purpose == PropertyPurpose.BUY,
            onClick = { viewModel.setPurposeFilter(PropertyPurpose.BUY) },
            label = { Text("Buy") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldTeal,
              selectedLabelColor = Color.White
            )
          )
          FilterChip(
            selected = filter.purpose == PropertyPurpose.RENT,
            onClick = { viewModel.setPurposeFilter(PropertyPurpose.RENT) },
            label = { Text("Rent") },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = EmeraldTeal,
              selectedLabelColor = Color.White
            )
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Property Type Quick Row
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(PropertyType.values()) { type ->
            FilterChip(
              selected = filter.type == type,
              onClick = { viewModel.setTypeFilter(type) },
              label = { Text(type.displayName, fontSize = 12.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
              )
            )
          }
        }
      }
    }

    // Results Count & Active Info
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${properties.size} Properties Available",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      if (activeFilterCount > 0) {
        Text(
          text = "Reset All",
          fontSize = 12.sp,
          color = AmberGold,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.testTag("reset_filters_label")
        )
      }
    }

    // Property List or Empty State
    if (properties.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(80.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.HomeWork,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "No Properties Found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Try adjusting your price range, property type, or city filters.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = { viewModel.resetFilter() },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Clear All Filters")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(properties, key = { it.id }) { property ->
          val isFav = favoriteIds.contains(property.id)
          PropertyCard(
            property = property,
            isFavorite = isFav,
            onCardClick = { viewModel.openPropertyDetail(property) },
            onFavoriteClick = { viewModel.toggleFavorite(property.id) }
          )
        }
      }
    }
  }

  if (showFilterSheet) {
    FilterBottomSheet(
      initialFilter = filter,
      onDismiss = { showFilterSheet = false },
      onApplyFilter = { newFilter -> viewModel.setFilter(newFilter) }
    )
  }
}
