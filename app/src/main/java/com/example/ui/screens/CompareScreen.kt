package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Property
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import com.example.viewmodel.PropertyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
  viewModel: PropertyViewModel,
  modifier: Modifier = Modifier
) {
  BackHandler {
    viewModel.navigateBack()
  }

  val compareList by viewModel.compareList.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    TopAppBar(
      title = {
        Text("Compare Properties", fontWeight = FontWeight.Bold)
      },
      navigationIcon = {
        IconButton(onClick = { viewModel.navigateBack() }) {
          Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
      },
      actions = {
        if (compareList.isNotEmpty()) {
          IconButton(onClick = { viewModel.clearCompare() }) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear All")
          }
        }
      },
      colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    )

    if (compareList.size < 2) {
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
          Text(
            text = "Select 2 Properties to Compare",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Go to your Saved Homes or Explore tab and tap 'Select to Compare' on 2 properties to see side-by-side differences.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = { viewModel.navigateBack() },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
          ) {
            Text("Back to Properties")
          }
        }
      }
    } else {
      val propA = compareList[0]
      val propB = compareList[1]

      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 12.dp)
          .navigationBarsPadding()
      ) {
        // Top Image Cards
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          CompareHeaderCard(property = propA, modifier = Modifier.weight(1f))
          CompareHeaderCard(property = propB, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Comparison Table Rows
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            CompareRow("Price", propA.formattedPrice, propB.formattedPrice, isHighlight = true)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow("Type", propA.type.displayName, propB.type.displayName)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow("Purpose", "For ${propA.purpose.name}", "For ${propB.purpose.name}")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow("Bedrooms", "${propA.bedrooms} Beds", "${propB.bedrooms} Beds")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow("Bathrooms", "${propA.bathrooms} Baths", "${propB.bathrooms} Baths")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow("Covered Area", "${propA.areaSqFt} sq ft", "${propB.areaSqFt} sq ft")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow(
              "Price / sq ft",
              "Rs. ${propA.price / propA.areaSqFt.coerceAtLeast(1)}",
              "Rs. ${propB.price / propB.areaSqFt.coerceAtLeast(1)}"
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow("City", propA.city, propB.city)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow("Furnishing", propA.furnishing.displayName, propB.furnishing.displayName)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            CompareRow("Parking", "${propA.parkingSpaces} slots", "${propB.parkingSpaces} slots")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Button(
            onClick = { viewModel.openPropertyDetail(propA) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("View ${propA.title.take(8)}...", fontSize = 12.sp)
          }

          Button(
            onClick = { viewModel.openPropertyDetail(propB) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("View ${propB.title.take(8)}...", fontSize = 12.sp)
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
fun CompareHeaderCard(
  property: Property,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column {
      val img = property.imageRes ?: R.drawable.hero_luxury_home_1794507680318
      Image(
        painter = painterResource(id = img),
        contentDescription = property.title,
        modifier = Modifier
          .fillMaxWidth()
          .height(110.dp)
          .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
        contentScale = ContentScale.Crop
      )
      Column(modifier = Modifier.padding(10.dp)) {
        Text(
          text = property.title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          maxLines = 1
        )
        Text(
          text = property.formattedPrice,
          color = EmeraldTeal,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 14.sp
        )
      }
    }
  }
}

@Composable
fun CompareRow(
  label: String,
  valA: String,
  valB: String,
  isHighlight: Boolean = false
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.align(Alignment.CenterHorizontally)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = valA,
        modifier = Modifier.weight(1f),
        textAlign = TextAlign.Center,
        fontSize = if (isHighlight) 14.sp else 12.sp,
        fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
        color = if (isHighlight) EmeraldTeal else MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = valB,
        modifier = Modifier.weight(1f),
        textAlign = TextAlign.Center,
        fontSize = if (isHighlight) 14.sp else 12.sp,
        fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
        color = if (isHighlight) AmberGold else MaterialTheme.colorScheme.onSurface
      )
    }
  }
}
