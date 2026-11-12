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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PropertyCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import com.example.viewmodel.AppScreen
import com.example.viewmodel.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
  viewModel: PropertyViewModel,
  modifier: Modifier = Modifier
) {
  val savedProperties by viewModel.savedProperties.collectAsState()
  val favoriteIds by viewModel.favoriteIds.collectAsState()
  val compareList by viewModel.compareList.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    TopAppBar(
      title = {
        Column {
          Text(
            text = "Saved Homes",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${savedProperties.size} homes saved",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      },
      actions = {
        if (savedProperties.size >= 2) {
          OutlinedButton(
            onClick = { viewModel.navigateTo(AppScreen.COMPARE) },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .padding(end = 12.dp)
              .testTag("compare_nav_btn")
          ) {
            Icon(imageVector = Icons.Default.Compare, contentDescription = null, modifier = Modifier.size(16.dp).padding(end = 4.dp))
            Text("Compare (${compareList.size}/2)", fontSize = 12.sp)
          }
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    )

    if (savedProperties.isEmpty()) {
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
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "No Saved Homes Yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Tap the heart icon on any property to bookmark and review your favorite homes here.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = { viewModel.navigateTo(AppScreen.EXPLORE) },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Explore Properties")
          }
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        items(savedProperties, key = { it.id }) { property ->
          val isFav = favoriteIds.contains(property.id)
          val isComparing = compareList.any { it.id == property.id }

          Column {
            PropertyCard(
              property = property,
              isFavorite = isFav,
              onCardClick = { viewModel.openPropertyDetail(property) },
              onFavoriteClick = { viewModel.toggleFavorite(property.id) }
            )

            // Quick Compare Toggle Button
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
              horizontalArrangement = Arrangement.End
            ) {
              OutlinedButton(
                onClick = { viewModel.toggleCompare(property) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = if (isComparing) EmeraldTeal.copy(alpha = 0.1f) else Color.Transparent
                )
              ) {
                Icon(
                  imageVector = Icons.Default.Compare,
                  contentDescription = null,
                  modifier = Modifier.size(14.dp).padding(end = 4.dp),
                  tint = if (isComparing) EmeraldTeal else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = if (isComparing) "Selected for Compare ✓" else "Select to Compare",
                  fontSize = 11.sp,
                  color = if (isComparing) EmeraldTeal else MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }
  }
}
