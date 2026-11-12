package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {
  // Favorites
  @Query("SELECT propertyId FROM favorites")
  fun getAllFavoriteIds(): Flow<List<String>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun addFavorite(favorite: FavoriteEntity)

  @Query("DELETE FROM favorites WHERE propertyId = :propertyId")
  suspend fun removeFavorite(propertyId: String)

  @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE propertyId = :propertyId)")
  suspend fun isFavorite(propertyId: String): Boolean

  // User listed properties
  @Query("SELECT * FROM user_properties ORDER BY createdAt DESC")
  fun getUserProperties(): Flow<List<UserPropertyEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUserProperty(property: UserPropertyEntity)

  @Query("DELETE FROM user_properties WHERE id = :id")
  suspend fun deleteUserProperty(id: String)

  // Visit bookings
  @Query("SELECT * FROM visit_bookings ORDER BY createdAt DESC")
  fun getAllVisitBookings(): Flow<List<VisitBookingEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVisitBooking(booking: VisitBookingEntity)

  @Query("DELETE FROM visit_bookings WHERE id = :id")
  suspend fun deleteVisitBooking(id: String)

  // Inquiries
  @Query("SELECT * FROM inquiries ORDER BY createdAt DESC")
  fun getAllInquiries(): Flow<List<InquiryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertInquiry(inquiry: InquiryEntity)
}
