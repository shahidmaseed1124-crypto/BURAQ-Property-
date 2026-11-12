package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentSky
import com.example.ui.theme.AmberGold
import com.example.ui.theme.EmeraldTeal
import kotlin.math.pow

@Composable
fun EmiCalculatorCard(
  initialPropertyValue: Long = 30_000_000L,
  modifier: Modifier = Modifier
) {
  var propertyPrice by remember { mutableFloatStateOf(initialPropertyValue.toFloat()) }
  var downPaymentPercent by remember { mutableFloatStateOf(20f) }
  var interestRate by remember { mutableFloatStateOf(11.5f) }
  var tenureYears by remember { mutableIntStateOf(20) }

  val downPaymentAmount by remember {
    derivedStateOf { (propertyPrice * (downPaymentPercent / 100f)).toLong() }
  }

  val principalLoanAmount by remember {
    derivedStateOf { (propertyPrice - downPaymentAmount).toLong().coerceAtLeast(0) }
  }

  val emiResult by remember {
    derivedStateOf {
      val p = principalLoanAmount
      val r = (interestRate / 12f) / 100f
      val n = tenureYears * 12
      if (p <= 0 || n <= 0) {
        Triple(0L, 0L, 0L)
      } else {
        val factor = (1 + r).pow(n)
        val emi = (p * r * factor / (factor - 1)).toLong()
        val totalPayment = emi * n
        val totalInterest = (totalPayment - p).coerceAtLeast(0)
        Triple(emi, totalInterest, totalPayment)
      }
    }
  }

  val monthlyEmi = emiResult.first
  val totalInterest = emiResult.second
  val totalPayment = emiResult.third

  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
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
          Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Calculate,
              contentDescription = null,
              tint = EmeraldTeal,
              modifier = Modifier.padding(8.dp)
            )
          }
          Column {
            Text(
              text = "Home Loan / EMI Estimator",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Estimate monthly mortgage payments",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Monthly EMI Highlight Box
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = EmeraldTeal
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "Estimated Monthly EMI",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Rs. ${String.format("%,d", monthlyEmi)} / mo",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Sliders
      // 1. Property Price
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Property Price", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Text(
          formatCurrencyAmount(propertyPrice.toLong()),
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Bold,
          color = EmeraldTeal
        )
      }
      Slider(
        value = propertyPrice,
        onValueChange = { propertyPrice = it },
        valueRange = 1_000_000f..100_000_000f,
        colors = SliderDefaults.colors(thumbColor = EmeraldTeal, activeTrackColor = EmeraldTeal)
      )

      // 2. Down Payment %
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Down Payment (${downPaymentPercent.toInt()}%)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Text(
          formatCurrencyAmount(downPaymentAmount),
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Bold,
          color = AccentSky
        )
      }
      Slider(
        value = downPaymentPercent,
        onValueChange = { downPaymentPercent = it },
        valueRange = 10f..50f,
        steps = 7,
        colors = SliderDefaults.colors(thumbColor = AccentSky, activeTrackColor = AccentSky)
      )

      // 3. Interest Rate %
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Interest Rate", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Text(
          "${String.format("%.1f", interestRate)}% p.a.",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Bold,
          color = AmberGold
        )
      }
      Slider(
        value = interestRate,
        onValueChange = { interestRate = it },
        valueRange = 7.0f..18.0f,
        colors = SliderDefaults.colors(thumbColor = AmberGold, activeTrackColor = AmberGold)
      )

      // 4. Tenure Years
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text("Loan Tenure", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        Text(
          "$tenureYears Years",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
      Slider(
        value = tenureYears.toFloat(),
        onValueChange = { tenureYears = it.toInt() },
        valueRange = 5f..30f,
        steps = 24,
        colors = SliderDefaults.colors(thumbColor = EmeraldTeal, activeTrackColor = EmeraldTeal)
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Ratio visual bar
      val totalCost = (principalLoanAmount + totalInterest).toFloat().coerceAtLeast(1f)
      val principalFraction = (principalLoanAmount.toFloat() / totalCost).coerceIn(0f, 1f)
      val interestFraction = (totalInterest.toFloat() / totalCost).coerceIn(0f, 1f)

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp))
      ) {
        Box(
          modifier = Modifier
            .weight(principalFraction.coerceAtLeast(0.01f))
            .fillMaxHeight()
            .background(EmeraldTeal)
        )
        Box(
          modifier = Modifier
            .weight(interestFraction.coerceAtLeast(0.01f))
            .fillMaxHeight()
            .background(AmberGold)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Legend & Totals
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Box(modifier = Modifier.size(10.dp).background(EmeraldTeal, CircleShape))
          Text("Loan: ${formatCurrencyAmount(principalLoanAmount)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
          Box(modifier = Modifier.size(10.dp).background(AmberGold, CircleShape))
          Text("Total Interest: ${formatCurrencyAmount(totalInterest)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }
  }
}

fun formatCurrencyAmount(amount: Long): String {
  return when {
    amount >= 10_000_000 -> "Rs. ${String.format("%.2f", amount / 10_000_000.0)} Cr"
    amount >= 100_000 -> "Rs. ${String.format("%.1f", amount / 100_000.0)} Lac"
    else -> "Rs. ${String.format("%,d", amount)}"
  }
}
