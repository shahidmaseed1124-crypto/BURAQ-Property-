package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Property
import com.example.data.model.PropertyPurpose
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.VerifiedGreen

@Composable
fun PropertyCard(
  property: Property,
  isFavorite: Boolean,
  onCardClick: () -> Unit,
  onFavoriteClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onCardClick)
      .testTag("property_card_${property.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column {
      // Image Container with Badges & Favorite button
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
      ) {
        val imageRes = property.imageRes ?: R.drawable.hero_luxury_home_1794507680318
        Image(
          painter = painterResource(id = imageRes),
          contentDescription = property.title,
          modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay for readability
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Black.copy(alpha = 0.45f), Color.Transparent, Color.Black.copy(alpha = 0.55f)),
                startY = 0f,
                endY = Float.POSITIVE_INFINITY
              )
            )
        )

        // Top Row: Purpose Tag & Favorite Button
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            // Purpose Tag
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (property.purpose == PropertyPurpose.BUY) EmeraldTeal else AmberGold,
              shadowElevation = 2.dp
            ) {
              Text(
                text = "FOR ${property.purpose.name}",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
              )
            }

            if (property.isVerified) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.92f)
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = VerifiedGreen,
                    modifier = Modifier.size(13.dp)
                  )
                  Text(
                    text = "Verified",
                    color = Color(0xFF0F172A),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                }
              }
            }
          }

          // Favorite Icon Button
          Surface(
            modifier = Modifier.size(38.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 3.dp
          ) {
            IconButton(
              onClick = onFavoriteClick,
              modifier = Modifier
                .fillMaxSize()
                .testTag("fav_btn_${property.id}")
            ) {
              Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "Saved" else "Save Property",
                tint = if (isFavorite) Color(0xFFE11D48) else Color(0xFF475569),
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }

        // Bottom Image Overlay: Price & Property Type
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Column {
            Text(
              text = property.formattedPrice,
              color = Color.White,
              fontSize = 20.sp,
              fontWeight = FontWeight.ExtraBold
            )
            Text(
              text = property.type.displayName,
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          }

          // Rating badge
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.65f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                tint = AmberGold,
                modifier = Modifier.size(14.dp)
              )
              Text(
                text = "${property.rating} (${property.reviewsCount})",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }

      // Card Content Details
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = property.title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(15.dp)
          )
          Text(
            text = property.shortAddress,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Property Specs Row (Beds, Baths, SqFt)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (property.bedrooms > 0) {
            SpecBadge(icon = Icons.Default.Bed, label = "${property.bedrooms} Beds")
          }
          if (property.bathrooms > 0) {
            SpecBadge(icon = Icons.Default.Bathtub, label = "${property.bathrooms} Baths")
          }
          SpecBadge(icon = Icons.Default.SquareFoot, label = "${property.areaSqFt} sq ft")
        }
      }
    }
  }
}

@Composable
fun HorizontalFeaturedCard(
  property: Property,
  isFavorite: Boolean,
  onCardClick: () -> Unit,
  onFavoriteClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .width(280.dp)
      .clickable(onClick = onCardClick)
      .testTag("featured_card_${property.id}"),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
  ) {
    Column {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(150.dp)
      ) {
        val imageRes = property.imageRes ?: R.drawable.hero_luxury_home_1794507680318
        Image(
          painter = painterResource(id = imageRes),
          contentDescription = property.title,
          modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(Color.Black.copy(alpha = 0.4f), Color.Transparent, Color.Black.copy(alpha = 0.5f))
              )
            )
        )

        // Badges
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = AmberGold
          ) {
            Text(
              text = "FEATURED",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold
            )
          }

          Surface(
            modifier = Modifier.size(34.dp),
            shape = CircleShape,
            color = Color.White.copy(alpha = 0.95f),
            shadowElevation = 2.dp
          ) {
            IconButton(
              onClick = onFavoriteClick,
              modifier = Modifier.fillMaxSize()
            ) {
              Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Save",
                tint = if (isFavorite) Color(0xFFE11D48) else Color(0xFF475569),
                modifier = Modifier.size(17.dp)
              )
            }
          }
        }

        Text(
          text = property.formattedPrice,
          color = Color.White,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(12.dp)
        )
      }

      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = property.title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = "${property.address}, ${property.city}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          if (property.bedrooms > 0) {
            SpecBadge(icon = Icons.Default.Bed, label = "${property.bedrooms} Beds")
          }
          SpecBadge(icon = Icons.Default.SquareFoot, label = "${property.areaSqFt} sq ft")
        }
      }
    }
  }
}

@Composable
fun SpecBadge(icon: ImageVector, label: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(15.dp)
    )
    Text(
      text = label,
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
