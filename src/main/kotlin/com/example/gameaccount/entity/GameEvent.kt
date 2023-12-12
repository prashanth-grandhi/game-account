package com.example.gameaccount.entity

import com.example.gameaccount.controller.EventType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "game_events")
data class GameEvent(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "timestamp", nullable = false)
    val timestamp: LocalDateTime,

    @Column(name = "player_id", nullable = false)
    val customerId: Long,

    @Column(name = "event_type", nullable = false)
    val eventType: EventType,

    @Column(name = "amount")
    val amount: Double
)