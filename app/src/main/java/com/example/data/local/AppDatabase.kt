package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.data.model.ApprovalStatus
import com.example.data.model.Job
import com.example.data.model.JobStatus
import com.example.data.model.ServiceChangeRequest
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "jobs")
data class JobEntity(
  @PrimaryKey val id: String,
  val customerName: String,
  val customerPhoneMasked: String,
  val serviceTitle: String,
  val packageType: String,
  val date: String,
  val timeSlot: String,
  val address: String,
  val latitude: Double,
  val longitude: Double,
  val instructions: String,
  val status: String,
  val estimatedEarnings: Double,
  val companyPrice: Double,
  val paymentMode: String,
  val beforePhotosJson: String,
  val afterPhotosJson: String,
  val serviceChangesJson: String,
  val cancellationReason: String?,
  val rescheduledDate: String?,
  val assignedTimestamp: Long
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
  @PrimaryKey val id: String,
  val timestamp: Long,
  val eventType: String,
  val details: String,
  val severity: String
)

@Entity(tableName = "partner_leaves")
data class LeaveEntity(
  @PrimaryKey val id: String,
  val date: String,
  val reason: String,
  val isFullDay: Boolean
)

@Dao
interface JobDao {
  @Query("SELECT * FROM jobs ORDER BY assignedTimestamp DESC")
  fun getAllJobs(): Flow<List<JobEntity>>

  @Query("SELECT * FROM jobs WHERE id = :jobId LIMIT 1")
  suspend fun getJobById(jobId: String): JobEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertJob(job: JobEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertJobs(jobs: List<JobEntity>)

  @Query("UPDATE jobs SET status = :status WHERE id = :jobId")
  suspend fun updateJobStatus(jobId: String, status: String)

  @Query("DELETE FROM jobs WHERE id = :jobId")
  suspend fun deleteJob(jobId: String)
}

@Dao
interface AuditDao {
  @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 100")
  fun getAuditLogs(): Flow<List<AuditLogEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLog(log: AuditLogEntity)
}

@Dao
interface LeaveDao {
  @Query("SELECT * FROM partner_leaves ORDER BY date ASC")
  fun getAllLeaves(): Flow<List<LeaveEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertLeave(leave: LeaveEntity)

  @Query("DELETE FROM partner_leaves WHERE id = :id")
  suspend fun deleteLeave(id: String)
}

class JsonConverters {
  @TypeConverter
  fun fromStringList(list: List<String>?): String {
    if (list == null) return "[]"
    val jsonArray = JSONArray()
    list.forEach { jsonArray.put(it) }
    return jsonArray.toString()
  }

  @TypeConverter
  fun toStringList(data: String?): List<String> {
    if (data.isNullOrEmpty()) return emptyList()
    val list = mutableListOf<String>()
    val jsonArray = JSONArray(data)
    for (i in 0 until jsonArray.length()) {
      list.add(jsonArray.getString(i))
    }
    return list
  }
}

@Database(
  entities = [JobEntity::class, AuditLogEntity::class, LeaveEntity::class],
  version = 1,
  exportSchema = false
)
@TypeConverters(JsonConverters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract fun jobDao(): JobDao
  abstract fun auditDao(): AuditDao
  abstract fun leaveDao(): LeaveDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "cleankr_partner_db"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
