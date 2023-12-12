package com.example.gameaccount.controller

// GameAccountController.kt
import com.example.gameaccount.entity.Customer
import com.example.gameaccount.entity.GameEvent
import com.example.gameaccount.model.ChargeRequest
import com.example.gameaccount.model.ChargeResponse
import com.example.gameaccount.model.WinRequest
import com.example.gameaccount.model.WinResponse
import com.example.gameaccount.repository.CustomerRepository
import com.example.gameaccount.repository.GameEventRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/game-account")
class GameAccountController(
    private val customerRepository: CustomerRepository,
    private val gameEventRepository: GameEventRepository
) {
    @GetMapping("/customers")
    fun getAllCustomers(): List<Customer> =
        customerRepository.findAll()

    @GetMapping("/events")
    fun getAllEvents(): List<GameEvent> =
        gameEventRepository.findAll()

    @PostMapping("/customers")
    fun createNewCustomer(@RequestBody customer: Customer): Customer =
        customerRepository.save(customer)

    @PostMapping("/charge")
    fun charge(@RequestBody request: ChargeRequest): ResponseEntity<ChargeResponse> {
        return customerRepository.findById(request.customerId).map { customer ->
            if ((request.amount <= 0) || (customer.accountBalance < request.amount)) {
                ResponseEntity.badRequest().body(ChargeResponse(customer.accountBalance))
            } else {
                customer.accountBalance -= request.amount
                customerRepository.save(customer)

                val event = GameEvent(
                    generateUniqueEventId(),
                    LocalDateTime.now(),
                    customer.id,
                    EventType.PURCHASE,
                    request.amount
                )
                gameEventRepository.save(event)

                ResponseEntity.ok(ChargeResponse(customer.accountBalance))
            }
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("/win")
    fun win(@RequestBody request: WinRequest): ResponseEntity<WinResponse> {
        return customerRepository.findById(request.customerId).map { customer ->
            if (request.winningAmount <= 0) {
                ResponseEntity.badRequest().body(WinResponse(customer.accountBalance))
            } else {
                customer.accountBalance += request.winningAmount
                customerRepository.save(customer)

                val event = GameEvent(
                    generateUniqueEventId(),
                    LocalDateTime.now(),
                    customer.id,
                    EventType.WIN,
                    request.winningAmount
                )
                gameEventRepository.save(event)

                ResponseEntity.ok(WinResponse(customer.accountBalance))
            }
        }.orElse(ResponseEntity.notFound().build())
    }

    private fun generateUniqueEventId(): Long {
        // Simple unique ID generator (for demonstration purposes)
        return (System.nanoTime())
    }
}

enum class EventType {
    PURCHASE, WIN
}
