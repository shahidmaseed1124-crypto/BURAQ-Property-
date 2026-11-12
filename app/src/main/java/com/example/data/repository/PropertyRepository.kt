package com.example.data.repository

import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.FavoriteEntity
import com.example.data.local.InquiryEntity
import com.example.data.local.UserPropertyEntity
import com.example.data.local.VisitBookingEntity
import com.example.data.model.FurnishingStatus
import com.example.data.model.Property
import com.example.data.model.PropertyInquiry
import com.example.data.model.PropertyPurpose
import com.example.data.model.PropertyType
import com.example.data.model.VisitBooking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID

class PropertyRepository(private val database: AppDatabase) {
  private val dao = database.propertyDao()

  // Curated initial seed properties
  val defaultProperties = listOf(
    Property(
      id = "prop_1",
      title = "Lumière Sunset Villa with Pool",
      description = "An ultra-luxurious architectural masterpiece offering 5 expansive en-suite bedrooms, imported Italian marble flooring, double-height grand living hall, private infinity swimming pool, landscaped lawn, state-of-the-art smart home automation, and separate maid quarters.",
      price = 57_500_000L, // 5.75 Crore
      purpose = PropertyPurpose.BUY,
      type = PropertyType.HOUSE,
      bedrooms = 5,
      bathrooms = 6,
      areaSqFt = 4500,
      address = "Sector J, Phase 6, DHA",
      city = "Lahore",
      isFeatured = true,
      isVerified = true,
      rating = 4.9f,
      reviewsCount = 38,
      furnishing = FurnishingStatus.FULLY_FURNISHED,
      parkingSpaces = 3,
      floorInfo = "Ground + 1st Floor",
      facing = "North-East Facing",
      amenities = listOf("Private Pool", "Smart Automation", "Solar Backup", "24/7 Gated Security", "Lush Lawn", "Italian Kitchen"),
      imageRes = R.drawable.hero_luxury_home_1794507680318,
      ownerName = "Taimur Malik",
      ownerPhone = "+92 300 8472910",
      ownerType = "Verified Owner",
      datePosted = "2 days ago"
    ),
    Property(
      id = "prop_2",
      title = "The Skyline Residences Penthouse",
      description = "Soar above the city in this magnificent high-rise penthouse featuring unobstructed panoramic skyline views, bespoke designer finishes, wraparound glass balcony, temperature-controlled indoor pool access, gym, high-speed private elevators, and concierge service.",
      price = 28_500_000L, // 2.85 Crore
      purpose = PropertyPurpose.BUY,
      type = PropertyType.PENTHOUSE,
      bedrooms = 3,
      bathrooms = 4,
      areaSqFt = 2800,
      address = "Main Boulevard, Gulberg III",
      city = "Lahore",
      isFeatured = true,
      isVerified = true,
      rating = 4.8f,
      reviewsCount = 29,
      furnishing = FurnishingStatus.FULLY_FURNISHED,
      parkingSpaces = 2,
      floorInfo = "18th Floor (Top Floor)",
      facing = "West Facing (Sunset View)",
      amenities = listOf("Panoramic Balcony", "Rooftop Pool", "Concierge", "24/7 Power Backup", "Fitness Center", "Valet Parking"),
      imageRes = R.drawable.apartment_modern_1794507695893,
      ownerName = "Apex Real Estate Builders",
      ownerPhone = "+92 321 4455667",
      ownerType = "Developer Agent",
      datePosted = "Yesterday"
    ),
    Property(
      id = "prop_3",
      title = "Margalla View Executive Flat",
      description = "Scenic and tranquil luxury apartment situated at the foothills of Margalla hills. Features contemporary open-concept layout, floor-to-ceiling double-glazed windows, premium woodwork, centralized air conditioning, and peaceful neighborhood.",
      price = 34_000_000L, // 3.40 Crore
      purpose = PropertyPurpose.BUY,
      type = PropertyType.APARTMENT,
      bedrooms = 4,
      bathrooms = 4,
      areaSqFt = 3200,
      address = "Sector F-7/2",
      city = "Islamabad",
      isFeatured = true,
      isVerified = true,
      rating = 4.9f,
      reviewsCount = 42,
      furnishing = FurnishingStatus.SEMI_FURNISHED,
      parkingSpaces = 2,
      floorInfo = "5th of 10 Floors",
      facing = "Margalla Hills Facing",
      amenities = listOf("Hill Views", "24/7 Security", "Basement Parking", "Gym & Sauna", "Backup Generator", "High-speed Lifts"),
      imageRes = R.drawable.apartment_modern_1794507695893,
      ownerName = "Suleman Shah",
      ownerPhone = "+92 333 9876543",
      ownerType = "Verified Owner",
      datePosted = "3 days ago"
    ),
    Property(
      id = "prop_4",
      title = "Creek Vista Modern Apartment for Rent",
      description = "Chic sea-breeze modern apartment in the prestigious Creek Vista towers. Walkable to seaside promenade, high security with biometric access, well-maintained community club with tennis courts and Olympic pool.",
      price = 85_000L, // 85,000 / mo
      purpose = PropertyPurpose.RENT,
      type = PropertyType.APARTMENT,
      bedrooms = 2,
      bathrooms = 2,
      areaSqFt = 1650,
      address = "Clifton Block 4",
      city = "Karachi",
      isFeatured = false,
      isVerified = true,
      rating = 4.7f,
      reviewsCount = 18,
      furnishing = FurnishingStatus.FULLY_FURNISHED,
      parkingSpaces = 1,
      floorInfo = "7th of 15 Floors",
      facing = "Sea Facing",
      amenities = listOf("Sea Breeze", "Clubhouse", "Tennis Court", "24/7 Water & Power", "Visitor Parking", "Security Cameras"),
      imageRes = R.drawable.apartment_modern_1794507695893,
      ownerName = "Kashif Riaz",
      ownerPhone = "+92 345 5566778",
      ownerType = "Property Manager",
      datePosted = "5 days ago"
    ),
    Property(
      id = "prop_5",
      title = "Grand Designer Villa on Golf Course",
      description = "Exclusive 1-Kanal golf-front designer residence with imported Spanish tile work, heated jacuzzi, modern chef's kitchen, rooftop terrace lounge with barbecue setup, and mature manicured palm trees.",
      price = 68_000_000L, // 6.80 Crore
      purpose = PropertyPurpose.BUY,
      type = PropertyType.HOUSE,
      bedrooms = 5,
      bathrooms = 6,
      areaSqFt = 5200,
      address = "Sector A, Phase 8, Bahria Town",
      city = "Rawalpindi",
      isFeatured = true,
      isVerified = true,
      rating = 5.0f,
      reviewsCount = 51,
      furnishing = FurnishingStatus.FULLY_FURNISHED,
      parkingSpaces = 4,
      floorInfo = "Ground + 1st Floor + Roof Deck",
      facing = "Golf Course Facing",
      amenities = listOf("Golf View", "Heated Jacuzzi", "BBQ Terrace", "Solar 15kW", "Servant Room", "Lush Garden"),
      imageRes = R.drawable.hero_luxury_home_1794507680318,
      ownerName = "Brig. (R) Haroon",
      ownerPhone = "+92 301 2233445",
      ownerType = "Verified Owner",
      datePosted = "Just now"
    ),
    Property(
      id = "prop_6",
      title = "Emerald Court Furnished Residency for Rent",
      description = "Spacious 3-BHK luxury apartment in prime E-11, completely furnished with plush sofas, 65-inch 4K smart TVs, inverter ACs in all rooms, modern refrigerator, oven, and washing machine. Move-in ready!",
      price = 120_000L, // 1.20 Lac / mo
      purpose = PropertyPurpose.RENT,
      type = PropertyType.APARTMENT,
      bedrooms = 3,
      bathrooms = 3,
      areaSqFt = 2100,
      address = "Sector E-11/3",
      city = "Islamabad",
      isFeatured = false,
      isVerified = true,
      rating = 4.6f,
      reviewsCount = 14,
      furnishing = FurnishingStatus.FULLY_FURNISHED,
      parkingSpaces = 1,
      floorInfo = "3rd of 8 Floors",
      facing = "Park Facing",
      amenities = listOf("Fully Furnished", "High-speed Internet", "Inverter ACs", "Elevator", "Security Guard", "Covered Car Park"),
      imageRes = R.drawable.apartment_modern_1794507695893,
      ownerName = "Zubair Ahmed",
      ownerPhone = "+92 312 8899001",
      ownerType = "Verified Owner",
      datePosted = "4 days ago"
    ),
    Property(
      id = "prop_7",
      title = "Prime Commercial Showroom & Office",
      description = "Strategic corner showroom located on the commercial strip of Blue Area. Double height frontage, massive footfall, reserved front parking, dedicated transformer, and high rental yield potential.",
      price = 79_000_000L, // 7.90 Crore
      purpose = PropertyPurpose.BUY,
      type = PropertyType.COMMERCIAL,
      bedrooms = 0,
      bathrooms = 2,
      areaSqFt = 3500,
      address = "Jinnah Avenue, Blue Area",
      city = "Islamabad",
      isFeatured = false,
      isVerified = true,
      rating = 4.9f,
      reviewsCount = 22,
      furnishing = FurnishingStatus.UNFURNISHED,
      parkingSpaces = 6,
      floorInfo = "Ground + Mezzanine",
      facing = "Main Road Corner",
      amenities = listOf("Corner Location", "Dedicated Transformer", "High Footfall", "Glass Facade", "Fire Safety System"),
      imageRes = R.drawable.apartment_modern_1794507695893,
      ownerName = "Capital Commercial Estates",
      ownerPhone = "+92 300 7711223",
      ownerType = "Commercial Agency",
      datePosted = "1 week ago"
    ),
    Property(
      id = "prop_8",
      title = "Park View City 1-Kanal Prime Residential Plot",
      description = "Ideal corner plot ready for immediate construction. Direct possession available, all dues cleared, adjacent to botanical garden and community center. Outstanding investment opportunity.",
      price = 19_500_000L, // 1.95 Crore
      purpose = PropertyPurpose.BUY,
      type = PropertyType.PLOT,
      bedrooms = 0,
      bathrooms = 0,
      areaSqFt = 4500,
      address = "Block H, Park View City",
      city = "Islamabad",
      isFeatured = false,
      isVerified = true,
      rating = 4.8f,
      reviewsCount = 19,
      furnishing = FurnishingStatus.UNFURNISHED,
      parkingSpaces = 0,
      floorInfo = "Residential Land",
      facing = "Corner East",
      amenities = listOf("Possession Ready", "Underground Electricity", "Water Connection", "Boundary Wall", "Gated Society"),
      imageRes = R.drawable.hero_luxury_home_1794507680318,
      ownerName = "Shahid Khan",
      ownerPhone = "+92 334 1122334",
      ownerType = "Direct Landowner",
      datePosted = "3 days ago"
    )
  )

  // Combines default properties with user-created listings from Room
  fun getAllProperties(): Flow<List<Property>> {
    return dao.getUserProperties().combine(dao.getAllFavoriteIds()) { userEntities, favIds ->
      val userProps = userEntities.map { entity ->
        Property(
          id = entity.id,
          title = entity.title,
          description = entity.description,
          price = entity.price,
          purpose = if (entity.purpose == "RENT") PropertyPurpose.RENT else PropertyPurpose.BUY,
          type = PropertyType.values().firstOrNull { it.name == entity.propertyType } ?: PropertyType.HOUSE,
          bedrooms = entity.bedrooms,
          bathrooms = entity.bathrooms,
          areaSqFt = entity.areaSqFt,
          address = entity.address,
          city = entity.city,
          isFeatured = false,
          isVerified = true,
          rating = 5.0f,
          reviewsCount = 1,
          furnishing = FurnishingStatus.values().firstOrNull { it.name == entity.furnishing } ?: FurnishingStatus.SEMI_FURNISHED,
          amenities = if (entity.amenitiesCsv.isNotBlank()) entity.amenitiesCsv.split(",") else listOf("24/7 Security", "Parking"),
          imageRes = if (entity.propertyType == PropertyType.APARTMENT.name || entity.propertyType == PropertyType.COMMERCIAL.name) {
            R.drawable.apartment_modern_1794507695893
          } else {
            R.drawable.hero_luxury_home_1794507680318
          },
          ownerName = entity.ownerName,
          ownerPhone = entity.ownerPhone,
          ownerType = "Direct Owner (You)",
          isUserCreated = true,
          datePosted = "Recently Listed"
        )
      }
      userProps + defaultProperties
    }
  }

  fun getFavoritePropertyIds(): Flow<List<String>> {
    return dao.getAllFavoriteIds()
  }

  suspend fun toggleFavorite(propertyId: String) {
    if (dao.isFavorite(propertyId)) {
      dao.removeFavorite(propertyId)
    } else {
      dao.addFavorite(FavoriteEntity(propertyId))
    }
  }

  suspend fun insertUserProperty(
    title: String,
    description: String,
    price: Long,
    purpose: PropertyPurpose,
    type: PropertyType,
    bedrooms: Int,
    bathrooms: Int,
    areaSqFt: Int,
    address: String,
    city: String,
    ownerName: String,
    ownerPhone: String,
    furnishing: FurnishingStatus,
    amenities: List<String>
  ): String {
    val id = "user_prop_" + UUID.randomUUID().toString().take(8)
    val entity = UserPropertyEntity(
      id = id,
      title = title,
      description = description,
      price = price,
      purpose = purpose.name,
      propertyType = type.name,
      bedrooms = bedrooms,
      bathrooms = bathrooms,
      areaSqFt = areaSqFt,
      address = address,
      city = city,
      ownerName = ownerName,
      ownerPhone = ownerPhone,
      furnishing = furnishing.name,
      amenitiesCsv = amenities.joinToString(",")
    )
    dao.insertUserProperty(entity)
    return id
  }

  suspend fun deleteUserProperty(id: String) {
    dao.deleteUserProperty(id)
  }

  fun getVisitBookings(): Flow<List<VisitBooking>> {
    return dao.getAllVisitBookings().combine(dao.getAllFavoriteIds()) { entities, _ ->
      entities.map { entity ->
        VisitBooking(
          id = entity.id,
          propertyId = entity.propertyId,
          propertyTitle = entity.propertyTitle,
          propertyAddress = entity.propertyAddress,
          visitDate = entity.visitDate,
          timeSlot = entity.timeSlot,
          visitType = entity.visitType,
          visitorName = entity.visitorName,
          visitorPhone = entity.visitorPhone,
          notes = entity.notes,
          status = entity.status,
          timestamp = entity.createdAt
        )
      }
    }
  }

  suspend fun bookVisit(
    property: Property,
    date: String,
    timeSlot: String,
    visitType: String,
    visitorName: String,
    visitorPhone: String,
    notes: String
  ): String {
    val id = "visit_" + UUID.randomUUID().toString().take(8)
    val entity = VisitBookingEntity(
      id = id,
      propertyId = property.id,
      propertyTitle = property.title,
      propertyAddress = "${property.address}, ${property.city}",
      visitDate = date,
      timeSlot = timeSlot,
      visitType = visitType,
      visitorName = visitorName,
      visitorPhone = visitorPhone,
      notes = notes,
      status = "Confirmed"
    )
    dao.insertVisitBooking(entity)
    return id
  }

  suspend fun cancelVisit(id: String) {
    dao.deleteVisitBooking(id)
  }

  suspend fun sendInquiry(
    property: Property,
    name: String,
    phone: String,
    message: String
  ): String {
    val id = "inq_" + UUID.randomUUID().toString().take(8)
    val entity = InquiryEntity(
      id = id,
      propertyId = property.id,
      propertyTitle = property.title,
      senderName = name,
      senderPhone = phone,
      message = message
    )
    dao.insertInquiry(entity)
    return id
  }

  fun getInquiries(): Flow<List<PropertyInquiry>> {
    return dao.getAllInquiries().combine(dao.getAllFavoriteIds()) { list, _ ->
      list.map {
        PropertyInquiry(
          id = it.id,
          propertyId = it.propertyId,
          propertyTitle = it.propertyTitle,
          senderName = it.senderName,
          senderPhone = it.senderPhone,
          message = it.message,
          timestamp = it.createdAt
        )
      }
    }
  }
}
