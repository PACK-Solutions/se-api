package com.ps.personne.health

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.SQLException

val logger = KotlinLogging.logger { }

/**
 * Health check service to verify connectivity to external dependencies
 */
class HealthCheckService {

    /**
     * Perform health check on all dependencies
     */
    fun performHealthCheck(): HealthCheckResult {
        val databaseHealth = checkDatabaseHealth()

        val overallStatus = if (databaseHealth.status == HealthStatus.UP) {
            HealthStatus.UP
        } else {
            HealthStatus.DOWN
        }

        return HealthCheckResult(
            status = overallStatus,
            checks = mapOf(
                "database" to databaseHealth,
            ),
        )
    }

    /**
     * Check database connectivity
     */
    private fun checkDatabaseHealth(): ComponentHealth {
        return try {
            val ok = transaction {
                val result = exec("SELECT 1") { rs -> if (rs.next()) rs.getInt(1) else null }
                result == 1
            }
            if (!ok) error("Unexpected result from health probe query")

            logger.debug { "Database health check: OK" }
            ComponentHealth(HealthStatus.UP, mapOf("connection" to "active"))
        } catch (e: ExposedSQLException) {
            logger.error(e) { "Database health check failed (Exposed)" }
            ComponentHealth(
                HealthStatus.DOWN,
                mapOf("connection" to "failed", "error" to (e.message ?: "Unknown error")),
            )
        } catch (e: SQLException) {
            logger.error(e) { "Database health check failed (SQL)" }
            ComponentHealth(
                HealthStatus.DOWN,
                mapOf("connection" to "failed", "error" to (e.message ?: "Unknown error")),
            )
        } catch (e: IllegalStateException) {
            // Covers internal check like unexpected result from health probe query
            logger.error(e) { "Database health check failed (IllegalState)" }
            ComponentHealth(
                HealthStatus.DOWN,
                mapOf("connection" to "failed", "error" to (e.message ?: "Unknown error")),
            )
        }
    }
}

/**
 * Health check result
 */
@Serializable
data class HealthCheckResult(
    val status: HealthStatus,
    val checks: Map<String, ComponentHealth>,
)

/**
 * Individual component health
 */
@Serializable
data class ComponentHealth(
    val status: HealthStatus,
    val details: Map<String, String>,
)

/**
 * Health status enum
 */
@Serializable
enum class HealthStatus {
    UP, DOWN
}
