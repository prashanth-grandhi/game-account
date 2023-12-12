package com.example.gameaccount.repository

// CustomerRepository.kt
import com.example.gameaccount.entity.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, Long>

