package com.arrenda.porperty.service

import com.arrenda.common.exception.NotFoundException
import com.arrenda.porperty.dto.PropertyCreateDTO
import com.arrenda.porperty.dto.PropertyFilterDTO
import com.arrenda.porperty.dto.PropertyUpdateDTO
import com.arrenda.porperty.dto.PropertyViewDTO
import com.arrenda.porperty.mapper.PropertyMapper
import com.arrenda.porperty.model.Property
import com.arrenda.porperty.repository.PropertyRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest.of
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.stereotype.Service
import java.util.*

@Service
class PropertyService(
    private val repository: PropertyRepository,
    private val mapper: PropertyMapper
) {
    fun findAll(filter: PropertyFilterDTO): Page<PropertyViewDTO> =
        repository.findAll(
            specification = filter.description?.let { hasDescription(it) } ?: where(null),
            pageable = of(filter.page, filter.pageSize))
            .map(mapper::toDTO)

    fun findById(id: UUID): PropertyViewDTO =
        repository.findById(id)
            .map(mapper::toDTO)
            .orElseThrow { NotFoundException("Property with ID $id not found") }

    fun create(propertyCreateDTO: PropertyCreateDTO) = mapper.toDTO(repository.save(mapper.toModel(propertyCreateDTO)))

    fun update(propertyUpdateDTO: PropertyUpdateDTO): PropertyViewDTO {
        val property = repository.findById(propertyUpdateDTO.id)
            .orElseThrow { NotFoundException("Property with ID ${propertyUpdateDTO.id} not found") }

        property.description = propertyUpdateDTO.description

        return mapper.toDTO(repository.save(property))
    }

    private fun hasDescription(description: String): Specification<Property> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.like(root.get("description"), "%$description%")
        }
    }
}