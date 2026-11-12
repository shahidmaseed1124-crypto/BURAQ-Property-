package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Property
import com.example.data.model.PropertyPurpose
import com.example.data.model.PropertyType
import com.example.ui.components.EmiCalculatorCard
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.HorizontalFeaturedCard
import com.example.ui.components.PropertyCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.EmeraldTealDark
import com.example.viewmodel.AppScreen
import com.example.viewmodel.PropertyViewModel

@Composable
fun HomeScreen(
  viewModel: PropertyViewModel,
  modifier: Modifier = Modifier
) {
  val properties by viewModel.allProperties.collectAsState()
  val favoriteIds by viewModel.favoriteIds.collectAsState()
  val filter by viewModel.filter.collectAsState()

  var showFilterSheet by remember { mutableStateOf(false) }

  val featuredProperties = remember(properties) {
    properties.filter { it.isFeatured }
  }

  val recentProperties = remember(properties) {
    properties.take(5)
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // 1. Top Brand Header
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(EmeraldTealDark)
          .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = AmberGold,
              modifier = Modifier.size(40.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(8.dp)
              )
            }
            Column {
              Text(
                text = "Apna Ghar",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
              )
              Text(
                text = "Your Dream Home Awaits",
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 11.sp
              )
            }
          }

          // City selector button
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White.copy(alpha = 0.15f),
            modifier = Modifier.clickable { showFilterSheet = true }
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = AmberGold,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = filter.city,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Search Bar with Filter Button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = filter.searchQuery,
            onValueChange = { viewModel.updateSearchQuery(it) },
            placeholder = { Text("Search location, society, or villa...", fontSize = 13.sp) },
            leadingIcon = {
              Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = EmeraldTeal)
            },
            modifier = Modifier
              .weight(1f)
              .testTag("home_search_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.White,
              unfocusedContainerColor = Color.White,
              focusedBorderColor = AmberGold,
              unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true
          )

          Surface(
            shape = RoundedCornerShape(14.dp),
            color = AmberGold,
            modifier = Modifier
              .size(50.dp)
              .clickable { showFilterSheet = true }
              .testTag("home_filter_btn")
          ) {
            Icon(
              imageVector = Icons.Default.FilterList,
              contentDescription = "Filters",
              tint = Color.White,
              modifier = Modifier.padding(13.dp)
            )
          }
        }
      }
    }

    // 2. Hero Luxury Banner
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 16.dp)
          .height(170.dp)
          .clip(RoundedCornerShape(22.dp))
          .clickable {
            viewModel.setPurposeFilter(PropertyPurpose.BUY)
            viewModel.navigateTo(AppScreen.EXPLORE)
          }
      ) {
        Image(
          painter = painterResource(id = R.drawable.hero_luxury_home_1794507680318),
          contentDescription = "Hero Luxury Home",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.horizontalGradient(
                colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)
              )
            )
        )

        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
          verticalArrangement = Arrangement.Center
        ) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = AmberGold
          ) {
            Text(
              text = "VERIFIED PROPERTIES",
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Find Your Dream\nApna Ghar",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 24.sp
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "0% Commission on Select Listings →",
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    // 3. Category Quick Actions
    item {
      Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
          text = "Explore Categories",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          CategoryItem(
            icon = Icons.Default.Home,
            label = "Buy",
            color = EmeraldTeal,
            onClick = {
              viewModel.setPurposeFilter(PropertyPurpose.BUY)
              viewModel.setTypeFilter(PropertyType.ALL)
              viewModel.navigateTo(AppScreen.EXPLORE)
            }
          )
          CategoryItem(
            icon = Icons.Default.Apartment,
            label = "Rent",
            color = Color(0xFF0284C7),
            onClick = {
              viewModel.setPurposeFilter(PropertyPurpose.RENT)
              viewModel.setTypeFilter(PropertyType.ALL)
              viewModel.navigateTo(AppScreen.EXPLORE)
            }
          )
          CategoryItem(
            icon = Icons.Default.AddHome,
            label = "Post Property",
            color = AmberGold,
            onClick = { viewModel.navigateTo(AppScreen.POST_PROPERTY) }
          )
          CategoryItem(
            icon = Icons.Default.Landscape,
            label = "Plots",
            color = Color(0xFF059669),
            onClick = {
              viewModel.setPurposeFilter(null)
              viewModel.setTypeFilter(PropertyType.PLOT)
              viewModel.navigateTo(AppScreen.EXPLORE)
            }
          )
          CategoryItem(
            icon = Icons.Default.Calculate,
            label = "EMI Calc",
            color = Color(0xFF7C3AED),
            onClick = { viewModel.navigateTo(AppScreen.CALCULATOR) }
          )
        }
      }
    }

    // 4. Featured Properties Carousel
    if (featuredProperties.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Featured Listings",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Hand-picked premium homes & villas",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Text(
            text = "See All",
            color = EmeraldTeal,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable {
              viewModel.resetFilter()
              viewModel.navigateTo(AppScreen.EXPLORE)
            }
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(14.dp),
          contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
          items(featuredProperties, key = { it.id }) { property ->
            val isFav = favoriteIds.contains(property.id)
            HorizontalFeaturedCard(
              property = property,
              isFavorite = isFav,
              onCardClick = { viewModel.openPropertyDetail(property) },
              onFavoriteClick = { viewModel.toggleFavorite(property.id) }
            )
          }
        }
      }
    }

    // 5. Why Choose Apna Ghar Card
    item {
      Spacer(modifier = Modifier.height(24.dp))
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldTeal)
            Text(
              text = "Why Apna Ghar?",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          TrustBullet("100% Verified Titles & Registry Clearance")
          TrustBullet("Direct Owner & Verified Builder Contacts")
          TrustBullet("Schedule Free Physical Visits or Live Video Tours")
          TrustBullet("Instant Accurate Home Loan EMI Calculations")
        }
      }
    }

    // 6. Recent Listings Section
    item {
      Spacer(modifier = Modifier.height(24.dp))
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Trending Properties",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Latest homes, apartments, & plots",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Text(
          text = "View All",
          color = EmeraldTeal,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.clickable {
            viewModel.resetFilter()
            viewModel.navigateTo(AppScreen.EXPLORE)
          }
        )
      }
      Spacer(modifier = Modifier.height(12.dp))
    }

    items(recentProperties, key = { it.id }) { property ->
      val isFav = favoriteIds.contains(property.id)
      PropertyCard(
        property = property,
        isFavorite = isFav,
        onCardClick = { viewModel.openPropertyDetail(property) },
        onFavoriteClick = { viewModel.toggleFavorite(property.id) },
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
      )
    }
  }

  if (showFilterSheet) {
    FilterBottomSheet(
      initialFilter = filter,
      onDismiss = { showFilterSheet = false },
      onApplyFilter = { newFilter ->
        viewModel.setFilter(newFilter)
        viewModel.navigateTo(AppScreen.EXPLORE)
      }
    )
  }
}

@Composable
fun CategoryItem(
  icon: ImageVector,
  label: String,
  color: Color,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.clickable(onClick = onClick)
  ) {
    Surface(
      shape = CircleShape,
      color = color.copy(alpha = 0.12f),
      modifier = Modifier.size(54.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = color,
          modifier = Modifier.size(26.dp)
        )
      }
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = label,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@Composable
fun TrustBullet(text: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.padding(vertical = 3.dp)
  ) {
    Icon(
      imageVector = Icons.Default.Check,
      contentDescription = null,
      tint = EmeraldTeal,
      modifier = Modifier.size(16.dp)
    )
    Text(
      text = text,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
