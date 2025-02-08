package com.arrenda.porperty.model

import jakarta.persistence.*
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO
import java.time.LocalDateTime
import java.util.*

@Table
@Entity
class Property(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID?,
    @Version
    var version: Long = 0L,
    var description: String,
    @DateTimeFormat(iso = ISO.DATE_TIME)
    val createdDate: LocalDateTime = LocalDateTime.now()
)