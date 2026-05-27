package com.example.demo1.service

import com.example.demo1.model.User
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    private val storage = mutableMapOf<Long, User>()
    private var idCounter = 1L

    override fun create(user: User): User {
        user.id = idCounter++
        storage[user.id!!] = user
        return user
    }

    override fun getById(id: Long): User? {
        return storage[id]
    }

    override fun getAll(): List<User> {
        return storage.values.toList()
    }

    override fun update(id: Long, user: User): User? {
        if (!storage.containsKey(id)) return null
        user.id = id
        storage[id] = user
        return user
    }

    override fun delete(id: Long) {
        storage.remove(id)
    }
}