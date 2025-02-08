package com.arrenda.porperty.repository

import com.arrenda.porperty.model.Property
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PropertyRepository : JpaRepository<Property, UUID> {
    fun findAll(specification: Specification<Property>, pageable: Pageable): Page<Property>
}