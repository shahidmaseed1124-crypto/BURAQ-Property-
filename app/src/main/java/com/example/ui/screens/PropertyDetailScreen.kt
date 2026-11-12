package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Property
import com.example.data.model.PropertyPurpose
import com.example.ui.components.EmiCalculatorCard
import com.example.ui.components.InquirySheet
import com.example.ui.components.ScheduleVisitSheet
import com.example.ui.theme.AccentSky
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.VerifiedGreen
import com.example.viewmodel.AppScreen
import com.example.viewmodel.PropertyViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PropertyDetailScreen(
  property: Property,
  viewModel: PropertyViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val favoriteIds by viewModel.favoriteIds.collectAsState()
  val isFavorite = favoriteIds.contains(property.id)

  var showScheduleSheet by remember { mutableStateOf(false) }
  var showInquirySheet by remember { mutableStateOf(false) }

  // Handle system and hardware back navigation
  BackHandler {
    viewModel.navigateBack()
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 100.dp)
    ) {
      // 1. Full-bleed Hero Image with Action Overlay
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        ) {
          val imageRes = property.imageRes ?: R.drawable.hero_luxury_home_1794507680318
          Image(
            painter = painterResource(id = imageRes),
            contentDescription = property.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )

          // Gradient overlay
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Black.copy(alpha = 0.65f))
                )
              )
          )

          // Top action bar: Back, Favorite, Share
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = CircleShape,
              color = Color.White.copy(alpha = 0.95f),
              modifier = Modifier.size(42.dp)
            ) {
              IconButton(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.testTag("detail_back_btn")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Back",
                  tint = Color(0xFF0F172A)
                )
              }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              // Share Button
              Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.95f),
                modifier = Modifier.size(42.dp)
              ) {
                IconButton(
                  onClick = {
                    val sendIntent = Intent().apply {
                      action = Intent.ACTION_SEND
                      putExtra(
                        Intent.EXTRA_TEXT,
                        "Check out this property on Apna Ghar: ${property.title} for ${property.formattedPrice}! Located in ${property.shortAddress}"
                      )
                      type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Property via")
                    context.startActivity(shareIntent)
                  }
                ) {
                  Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = Color(0xFF0F172A))
                }
              }

              // Favorite Button
              Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.95f),
                modifier = Modifier.size(42.dp)
              ) {
                IconButton(
                  onClick = { viewModel.toggleFavorite(property.id) },
                  modifier = Modifier.testTag("detail_fav_btn")
                ) {
                  Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color(0xFFE11D48) else Color(0xFF0F172A)
                  )
                }
              }
            }
          }

          // Bottom Price & Purpose Overlay
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .align(Alignment.BottomCenter)
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
          ) {
            Column {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (property.purpose == PropertyPurpose.BUY) EmeraldTeal else AmberGold
              ) {
                Text(
                  text = "FOR ${property.purpose.name}",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = property.formattedPrice,
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.Black.copy(alpha = 0.7f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                Text(
                  text = "${property.rating} (${property.reviewsCount} reviews)",
                  color = Color.White,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }

      // 2. Title & Location
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            if (property.isVerified) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = VerifiedGreen.copy(alpha = 0.12f)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(14.dp))
                  Text("Verified Registry & Title", color = VerifiedGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }
            }
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = MaterialTheme.colorScheme.surfaceVariant
            ) {
              Text(
                text = property.type.displayName,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = property.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = EmeraldTeal,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "${property.address}, ${property.city}",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // 3. Key Specifications Grid
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Property Overview",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))

            FlowRow(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(16.dp),
              verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              if (property.bedrooms > 0) {
                DetailSpecItem(icon = Icons.Default.Bed, label = "Bedrooms", value = "${property.bedrooms} Beds")
              }
              if (property.bathrooms > 0) {
                DetailSpecItem(icon = Icons.Default.Bathtub, label = "Bathrooms", value = "${property.bathrooms} Baths")
              }
              DetailSpecItem(icon = Icons.Default.SquareFoot, label = "Super Area", value = "${property.areaSqFt} sq. ft.")
              DetailSpecItem(icon = Icons.Default.DirectionsCar, label = "Parking", value = "${property.parkingSpaces} Reserved")
              DetailSpecItem(icon = Icons.Default.Layers, label = "Floor", value = property.floorInfo)
              DetailSpecItem(icon = Icons.Default.CompassCalibration, label = "Facing", value = property.facing)
            }
          }
        }
      }

      // 4. Description
      item {
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
          Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = property.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
          )
        }
      }

      // 5. Amenities
      item {
        Spacer(modifier = Modifier.height(20.dp))
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
          Text(
            text = "Key Amenities & Features",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(12.dp))

          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            property.amenities.forEach { amenity ->
              Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(16.dp))
                  Text(text = amenity, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
              }
            }
          }
        }
      }

      // 6. Interactive EMI Estimator for Buy properties
      if (property.purpose == PropertyPurpose.BUY) {
        item {
          Spacer(modifier = Modifier.height(24.dp))
          Box(modifier = Modifier.padding(horizontal = 20.dp)) {
            EmiCalculatorCard(initialPropertyValue = property.price)
          }
        }
      }

      // 7. Verified Owner / Agent Card
      item {
        Spacer(modifier = Modifier.height(24.dp))
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                Surface(
                  shape = CircleShape,
                  color = EmeraldTeal,
                  modifier = Modifier.size(48.dp)
                ) {
                  Box(contentAlignment = Alignment.Center) {
                    Text(
                      text = property.ownerName.take(1),
                      color = Color.White,
                      fontWeight = FontWeight.Bold,
                      fontSize = 20.sp
                    )
                  }
                }

                Column {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Text(
                      text = property.ownerName,
                      style = MaterialTheme.typography.titleSmall,
                      fontWeight = FontWeight.Bold
                    )
                    Icon(imageVector = Icons.Default.Verified, contentDescription = "Verified", tint = AccentSky, modifier = Modifier.size(16.dp))
                  }
                  Text(
                    text = property.ownerType,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Text(
                    text = property.ownerPhone,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = EmeraldTeal
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = {
                  val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${property.ownerPhone}")
                  }
                  context.startActivity(dialIntent)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
              ) {
                Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp).padding(end = 4.dp))
                Text("Call")
              }

              Button(
                onClick = { showInquirySheet = true },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
              ) {
                Icon(imageVector = Icons.Default.Mail, contentDescription = null, modifier = Modifier.size(18.dp).padding(end = 4.dp))
                Text("Message")
              }
            }
          }
        }
      }
    }

    // Bottom Sticky CTA Bar
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.BottomCenter)
        .navigationBarsPadding(),
      color = MaterialTheme.colorScheme.surface,
      shadowElevation = 12.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(
          onClick = { showInquirySheet = true },
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
            .testTag("detail_inquiry_btn"),
          shape = RoundedCornerShape(14.dp)
        ) {
          Icon(imageVector = Icons.Default.Mail, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
          Text("Inquire", fontWeight = FontWeight.Bold)
        }

        Button(
          onClick = { showScheduleSheet = true },
          modifier = Modifier
            .weight(1.5f)
            .height(52.dp)
            .testTag("schedule_visit_btn"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
        ) {
          Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.padding(end = 6.dp))
          Text("Schedule Tour", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
      }
    }
  }

  if (showScheduleSheet) {
    ScheduleVisitSheet(
      property = property,
      onDismiss = { showScheduleSheet = false },
      onConfirmBooking = { date, timeSlot, visitType, name, phone, notes ->
        viewModel.bookVisit(property, date, timeSlot, visitType, name, phone, notes)
      }
    )
  }

  if (showInquirySheet) {
    InquirySheet(
      property = property,
      onDismiss = { showInquirySheet = false },
      onSendInquiry = { name, phone, message ->
        viewModel.sendInquiry(property, name, phone, message)
      }
    )
  }
}

@Composable
fun DetailSpecItem(
  icon: ImageVector,
  label: String,
  value: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.width(150.dp)
  ) {
    Surface(
      shape = CircleShape,
      color = MaterialTheme.colorScheme.surface,
      modifier = Modifier.size(36.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = label,
          tint = EmeraldTeal,
          modifier = Modifier.size(18.dp)
        )
      }
    }
    Column {
      Text(
        text = label,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = value,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }
  }
}
