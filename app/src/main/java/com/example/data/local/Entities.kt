package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
  @PrimaryKey val propertyId: String,
  val savedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_properties")
data class UserPropertyEntity(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val price: Long,
  val purpose: String, // "BUY" or "RENT"
  val propertyType: String,
  val bedrooms: Int,
  val bathrooms: Int,
  val areaSqFt: Int,
  val address: String,
  val city: String,
  val ownerName: String,
  val ownerPhone: String,
  val furnishing: String,
  val amenitiesCsv: String,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "visit_bookings")
data class VisitBookingEntity(
  @PrimaryKey val id: String,
  val propertyId: String,
  val propertyTitle: String,
  val propertyAddress: String,
  val visitDate: String,
  val timeSlot: String,
  val visitType: String,
  val visitorName: String,
  val visitorPhone: String,
  val notes: String,
  val status: String,
  val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "inquiries")
data class InquiryEntity(
  @PrimaryKey val id: String,
  val propertyId: String,
  val propertyTitle: String,
  val senderName: String,
  val senderPhone: String,
  val message: String,
  val createdAt: Long = System.currentTimeMillis()
)
