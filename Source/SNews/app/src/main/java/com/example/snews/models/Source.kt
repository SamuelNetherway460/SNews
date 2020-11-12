package com.example.snews.models

class Source (id: String, name: String) {

    private var id: String? = null
    private var name: String? = null

    init {
        this.id = id
        this.name = name
    }

    fun getID() : String? {
        return this.id
    }

    fun getName() : String? {
        return this.name
    }
}