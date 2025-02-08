package com.arrenda.porperty.mapper

import com.arrenda.porperty.dto.PropertyCreateDTO
import com.arrenda.porperty.dto.PropertyViewDTO
import com.arrenda.porperty.model.Property
import org.springframework.stereotype.Component

@Component
class PropertyMapper {
    fun toDTO(property: Property) = PropertyViewDTO(property.id!!, property.description, property.createdDate)
    fun toModel(propertyDTO: PropertyCreateDTO) = Property(null, description = propertyDTO.description)
}