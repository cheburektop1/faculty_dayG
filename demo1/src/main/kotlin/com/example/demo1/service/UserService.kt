package com.example.demo1.service

import com.example.demo1.model.User

interface UserService {

    fun create(user: User): User

    fun getById(id: Long): User?

    fun getAll(): List<User>

    fun update(id: Long, user: User): User?

    fun delete(id: Long)
}