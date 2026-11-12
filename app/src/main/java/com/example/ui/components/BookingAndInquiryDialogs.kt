package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.example.data.model.Property
import com.example.ui.theme.EmeraldTeal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ScheduleVisitSheet(
  property: Property,
  onDismiss: () -> Unit,
  onConfirmBooking: (date: String, timeSlot: String, visitType: String, name: String, phone: String, notes: String) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  // Generate next 5 available days
  val dates = remember {
    val list = mutableListOf<String>()
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
    for (i in 1..5) {
      cal.add(Calendar.DAY_OF_YEAR, 1)
      list.add(sdf.format(cal.time))
    }
    list
  }

  val timeSlots = listOf("10:00 AM", "12:30 PM", "03:00 PM", "05:00 PM", "06:30 PM")

  var selectedDate by remember { mutableStateOf(dates.firstOrNull() ?: "Tomorrow") }
  var selectedTime by remember { mutableStateOf("03:00 PM") }
  var visitType by remember { mutableStateOf("In-Person Visit") }
  var visitorName by remember { mutableStateOf("") }
  var visitorPhone by remember { mutableStateOf("") }
  var notes by remember { mutableStateOf("") }
  var hasError by remember { mutableStateOf(false) }

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
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Schedule a Tour",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = property.title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Tour Type
      Text(
        text = "Visit Type",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        FilterChip(
          selected = visitType == "In-Person Visit",
          onClick = { visitType = "In-Person Visit" },
          label = { Text("In-Person Visit") },
          leadingIcon = { Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null) },
          colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
        )
        FilterChip(
          selected = visitType == "Live Video Tour",
          onClick = { visitType = "Live Video Tour" },
          label = { Text("Live Video Tour") },
          leadingIcon = { Icon(imageVector = Icons.Default.Videocam, contentDescription = null) },
          colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Select Date
      Text(
        text = "Select Date",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        dates.forEach { date ->
          FilterChip(
            selected = selectedDate == date,
            onClick = { selectedDate = date },
            label = { Text(date) },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Select Time Slot
      Text(
        text = "Select Preferred Time Slot",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        timeSlots.forEach { slot ->
          FilterChip(
            selected = selectedTime == slot,
            onClick = { selectedTime = slot },
            label = { Text(slot) },
            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = EmeraldTeal, selectedLabelColor = Color.White)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Contact Details
      Text(
        text = "Your Contact Information",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(8.dp))
      OutlinedTextField(
        value = visitorName,
        onValueChange = { visitorName = it; hasError = false },
        label = { Text("Your Full Name") },
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
        isError = hasError && visitorName.isBlank(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("visitor_name_input"),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = visitorPhone,
        onValueChange = { visitorPhone = it; hasError = false },
        label = { Text("Phone Number") },
        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
        isError = hasError && visitorPhone.isBlank(),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("visitor_phone_input"),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = notes,
        onValueChange = { notes = it },
        label = { Text("Optional message / questions") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 3
      )

      if (hasError) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Please enter your name and phone number to schedule the visit.",
          color = MaterialTheme.colorScheme.error,
          fontSize = 12.sp
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = {
          if (visitorName.isBlank() || visitorPhone.isBlank()) {
            hasError = true
          } else {
            onConfirmBooking(
              selectedDate,
              selectedTime,
              visitType,
              visitorName.trim(),
              visitorPhone.trim(),
              notes.trim()
            )
            onDismiss()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("confirm_booking_btn"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
      ) {
        Text("Confirm Tour Booking", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquirySheet(
  property: Property,
  onDismiss: () -> Unit,
  onSendInquiry: (name: String, phone: String, message: String) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var name by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("") }
  var message by remember { mutableStateOf("Hi, I am interested in ${property.title}. Please provide more details and final price.") }
  var hasError by remember { mutableStateOf(false) }

  val quickReplies = listOf(
    "Is this property still available?",
    "Can you share more photos & floor plan?",
    "Is the price negotiable?"
  )

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
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Contact ${property.ownerName}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = property.ownerType,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
          )
        }
        IconButton(onClick = onDismiss) {
          Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      OutlinedTextField(
        value = name,
        onValueChange = { name = it; hasError = false },
        label = { Text("Your Name") },
        leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
        isError = hasError && name.isBlank(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(8.dp))

      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it; hasError = false },
        label = { Text("Phone / WhatsApp Number") },
        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
        isError = hasError && phone.isBlank(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "Quick Inquiries:",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold
      )
      Spacer(modifier = Modifier.height(4.dp))
      quickReplies.forEach { suggestion ->
        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable { message = suggestion },
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = suggestion,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      OutlinedTextField(
        value = message,
        onValueChange = { message = it },
        label = { Text("Message") },
        modifier = Modifier.fillMaxWidth(),
        maxLines = 4
      )

      if (hasError) {
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Please enter your name and phone number.",
          color = MaterialTheme.colorScheme.error,
          fontSize = 12.sp
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = {
          if (name.isBlank() || phone.isBlank()) {
            hasError = true
          } else {
            onSendInquiry(name.trim(), phone.trim(), message.trim())
            onDismiss()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp)
          .testTag("send_inquiry_btn"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal)
      ) {
        Icon(imageVector = Icons.Default.Mail, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
        Text("Send Message", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
