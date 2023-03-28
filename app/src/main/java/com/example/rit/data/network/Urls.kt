package com.example.rit.data.network

sealed class Urls {
    class RELEASE : Urls() {
        val DOG_URL = "https://dog.ceo/dog-api/"
        val NATIONALIZE_URL = "https://nationalize.io/"
    }
}