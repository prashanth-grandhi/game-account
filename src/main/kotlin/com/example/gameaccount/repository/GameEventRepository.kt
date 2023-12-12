package com.example.gameaccount.repository

// GameEventRepository.kt
import com.example.gameaccount.entity.GameEvent
import org.springframework.data.jpa.repository.JpaRepository

interface GameEventRepository : JpaRepository<GameEvent, Long>
