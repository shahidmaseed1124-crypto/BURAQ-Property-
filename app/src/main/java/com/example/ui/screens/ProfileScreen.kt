package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import com.example.viewmodel.AppScreen
import com.example.viewmodel.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
  viewModel: PropertyViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val savedProperties by viewModel.savedProperties.collectAsState()
  val visits by viewModel.visitBookings.collectAsState()
  val myListings by viewModel.myProperties.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    TopAppBar(
      title = { Text("My Account & Hub", fontWeight = FontWeight.Bold) },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    )

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. User Header
      item {
        Card(
          shape = RoundedCornerShape(20.dp),
          colors = CardDefaults.cardColors(containerColor = EmeraldTeal),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = AmberGold,
              modifier = Modifier.size(54.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(32.dp)
                )
              }
            }

            Column {
              Text(
                text = "Welcome to Apna Ghar",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Verified Property Seeker & Host",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
              )
            }
          }
        }
      }

      // 2. Activity Counters
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          StatCard(
            count = savedProperties.size.toString(),
            label = "Saved Homes",
            icon = Icons.Default.Favorite,
            color = Color(0xFFE11D48),
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(AppScreen.SAVED) }
          )
          StatCard(
            count = visits.size.toString(),
            label = "Tours",
            icon = Icons.Default.CalendarMonth,
            color = EmeraldTeal,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(AppScreen.VISITS) }
          )
          StatCard(
            count = myListings.size.toString(),
            label = "My Listings",
            icon = Icons.Default.Home,
            color = AmberGold,
            modifier = Modifier.weight(1f),
            onClick = { viewModel.navigateTo(AppScreen.POST_PROPERTY) }
          )
        }
      }

      // 3. User Listed Properties Section
      if (myListings.isNotEmpty()) {
        item {
          Text(
            text = "My Listed Properties (${myListings.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        items(myListings, key = { it.id }) { userProp ->
          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = userProp.title,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Text(
                  text = "${userProp.formattedPrice} • ${userProp.shortAddress}",
                  fontSize = 12.sp,
                  color = EmeraldTeal,
                  fontWeight = FontWeight.SemiBold
                )
              }

              IconButton(onClick = { viewModel.deleteUserProperty(userProp.id) }) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete Listing",
                  tint = MaterialTheme.colorScheme.error
                )
              }
            }
          }
        }
      }

      // 4. Quick Tools Navigation
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(vertical = 6.dp)) {
            ProfileMenuRow(
              icon = Icons.Default.AddHome,
              title = "Post a New Property",
              subtitle = "List your home for Sale or Rent with 0% brokerage",
              onClick = { viewModel.navigateTo(AppScreen.POST_PROPERTY) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            ProfileMenuRow(
              icon = Icons.Default.Calculate,
              title = "Home Loan & EMI Calculator",
              subtitle = "Estimate monthly mortgage and loan options",
              onClick = { viewModel.navigateTo(AppScreen.CALCULATOR) }
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            ProfileMenuRow(
              icon = Icons.Default.CalendarMonth,
              title = "My Scheduled Tours",
              subtitle = "View in-person and video property tours",
              onClick = { viewModel.navigateTo(AppScreen.VISITS) }
            )
          }
        }
      }

      // 5. Support & Helpline
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = EmeraldTeal,
                modifier = Modifier.size(40.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
              }

              Column {
                Text(
                  text = "Apna Ghar Support Helpline",
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Text(
                  text = "9:00 AM - 9:00 PM • Free Advisory",
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
              onClick = {
                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                  data = Uri.parse("tel:+923001234567")
                }
                context.startActivity(dialIntent)
              },
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp)
            ) {
              Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp).padding(end = 6.dp))
              Text("Call +92 300 1234567")
            }
          }
        }
      }

      // 6. About App
      item {
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = EmeraldTeal)
              Text("About Apna Ghar", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Apna Ghar is Pakistan's premier property marketplace for finding, buying, renting, and listing verified homes, apartments, villas, and commercial real estate. All listings feature verified titles and direct contact without middleman barriers.",
              fontSize = 12.sp,
              lineHeight = 18.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Version 1.0.0 • Apna Ghar Technologies",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
          }
        }
      }
    }
  }
}

@Composable
fun StatCard(
  count: String,
  label: String,
  icon: ImageVector,
  color: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Card(
    modifier = modifier.clickable(onClick = onClick),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = count, fontWeight = FontWeight.Bold, fontSize = 18.sp)
      Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
  }
}

@Composable
fun ProfileMenuRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(horizontal = 16.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp),
      modifier = Modifier.weight(1f)
    ) {
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.size(38.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(imageVector = icon, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(20.dp))
        }
      }
      Column {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(14.dp)
    )
  }
}
