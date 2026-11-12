package com.example.data.model

enum class PropertyPurpose(val label: String) {
  BUY("Buy"),
  RENT("Rent")
}

enum class PropertyType(val displayName: String) {
  ALL("All Types"),
  HOUSE("House / Villa"),
  APARTMENT("Apartment"),
  PENTHOUSE("Penthouse"),
  PLOT("Plot / Land"),
  STUDIO("Studio Flat"),
  COMMERCIAL("Commercial")
}

enum class FurnishingStatus(val displayName: String) {
  FULLY_FURNISHED("Fully Furnished"),
  SEMI_FURNISHED("Semi-Furnished"),
  UNFURNISHED("Unfurnished")
}

data class Property(
  val id: String,
  val title: String,
  val description: String,
  val price: Long, // In standard currency units
  val purpose: PropertyPurpose,
  val type: PropertyType,
  val bedrooms: Int,
  val bathrooms: Int,
  val areaSqFt: Int,
  val address: String,
  val city: String,
  val isFeatured: Boolean = false,
  val isVerified: Boolean = true,
  val rating: Float = 4.8f,
  val reviewsCount: Int = 24,
  val furnishing: FurnishingStatus = FurnishingStatus.SEMI_FURNISHED,
  val parkingSpaces: Int = 1,
  val floorInfo: String = "2nd of 12 Floors",
  val facing: String = "East Facing",
  val amenities: List<String> = listOf("24/7 Security", "Covered Parking", "Power Backup", "Gym", "Lush Garden"),
  val imageRes: Int? = null,
  val imageUrl: String? = null,
  val ownerName: String = "Apna Ghar Premier Agent",
  val ownerPhone: String = "+92 300 1234567",
  val ownerType: String = "Verified Builder",
  val isUserCreated: Boolean = false,
  val datePosted: String = "Recently Added"
) {
  val formattedPrice: String
    get() {
      return when {
        purpose == PropertyPurpose.RENT -> {
          if (price >= 100_000) {
            "Rs. ${(price / 100_000.0).formatDec(1)} Lac / mo"
          } else {
            "Rs. ${String.format("%,d", price)} / mo"
          }
        }
        price >= 10_000_000 -> {
          "Rs. ${(price / 10_000_000.0).formatDec(2)} Crore"
        }
        price >= 100_000 -> {
          "Rs. ${(price / 100_000.0).formatDec(1)} Lac"
        }
        else -> "Rs. ${String.format("%,d", price)}"
      }
    }

  val shortAddress: String
    get() = "$address, $city"
}

private fun Double.formatDec(digits: Int): String {
  return String.format("%.${digits}f", this).trimEnd('0').trimEnd('.')
}

data class VisitBooking(
  val id: String,
  val propertyId: String,
  val propertyTitle: String,
  val propertyAddress: String,
  val visitDate: String,
  val timeSlot: String,
  val visitType: String = "In-Person Visit", // "In-Person Visit" or "Live Video Tour"
  val visitorName: String,
  val visitorPhone: String,
  val notes: String = "",
  val status: String = "Confirmed",
  val timestamp: Long = System.currentTimeMillis()
)

data class PropertyInquiry(
  val id: String,
  val propertyId: String,
  val propertyTitle: String,
  val senderName: String,
  val senderPhone: String,
  val senderEmail: String = "",
  val message: String,
  val timestamp: Long = System.currentTimeMillis()
)

data class PropertyFilter(
  val purpose: PropertyPurpose? = null,
  val type: PropertyType = PropertyType.ALL,
  val city: String = "All Cities",
  val maxPrice: Long = 100_000_000L,
  val minBeds: Int = 0,
  val verifiedOnly: Boolean = false,
  val searchQuery: String = ""
)

data class EmiCalculationResult(
  val monthlyEmi: Long,
  val totalInterest: Long,
  val totalPayment: Long,
  val principalAmount: Long,
  val downPayment: Long
)
