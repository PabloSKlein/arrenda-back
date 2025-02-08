package com.arrenda.porperty.controller

import com.arrenda.porperty.dto.PropertyCreateDTO
import com.arrenda.porperty.dto.PropertyFilterDTO
import com.arrenda.porperty.dto.PropertyUpdateDTO
import com.arrenda.porperty.service.PropertyService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/properties")
class PropertyController(private val service: PropertyService) {

    @GetMapping
    fun getAll(@ModelAttribute filter: PropertyFilterDTO) = service.findAll(filter)

    @PostMapping
    fun create(@RequestBody property: PropertyCreateDTO) = service.create(property)

    @PatchMapping
    fun update(@RequestBody property: PropertyUpdateDTO) = service.update(property)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID) = service.findById(id)
}