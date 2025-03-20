/*
 * Product Made by Anas Ghareib
 * Copyright (C) 2025 Anas Ghareib
 *
 * All rights reserved. This software and associated documentation files
 * (the "Software") are proprietary and confidential. Unauthorized copying,
 * distribution, modification, or use of this Software, via any medium,
 * is strictly prohibited without prior written permission from Anas Ghareib.
 *
 * This Software is provided "as is", without warranty of any kind, express
 * or implied, including but not limited to the warranties of merchantability,
 * fitness for a particular purpose, and non-infringement. In no event shall
 * the author be liable for any claim, damages, or other liability,
 * whether in an action of contract, tort, or otherwise, arising from,
 * out of, or in connection with the Software or the use of it.
 */
package dev.ansgrb.network

import dev.ansgrb.network.models.domain.Character
import dev.ansgrb.network.models.domain.Episode
import dev.ansgrb.network.models.remote.RemoteCharacter
import dev.ansgrb.network.models.remote.RemoteEpisode
import dev.ansgrb.network.models.remote.toDomainCharacter
import dev.ansgrb.network.models.remote.toDomainEpisode
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.text.get

class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest { url("https://rickandmortyapi.com/api/") }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var characterCash = mutableMapOf<Int, Character>() // I'm using a map cuz i want to look up things really fucking quickly

    // a suspendable function to get a character by id that returns a Character object
    suspend fun getCharacter(id: Int): ApiOps<Character> {
        characterCash[id]?.let { return ApiOps.Made(it) } // if we have a cash it will make a hit instantly
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
                .also { characterCash[id] = it } // first use of also
        }
    }

    suspend fun getEpisodes(episodeIds: List<Int>): ApiOps<List<Episode>> {
        val idsCommaSeparated = episodeIds.joinToString(separator = ",")
        return safeApiCall {
            //  to handle both single episode and multiple episodes cases
            val response = if (episodeIds.size == 1) {
                listOf(client.get("episode/$idsCommaSeparated").body<RemoteEpisode>()) // if we have a single episode we will wrap it in a list
            } else {
                client.get("episode/$idsCommaSeparated").body<List<RemoteEpisode>>() // if we have multiple episodes we will get a list
            }
            response.map { it.toDomainEpisode() }

//            client.get("episode/$idsCommaSeparated")
//                .body<List<RemoteEpisode>>()
//                .map { it.toDomainEpisode() }
        }
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOps<T> {
        return try {
            ApiOps.Made(data = apiCall())
        } catch (e: Exception) {
            ApiOps.Failed(exception = e)
        }
    }
}

// why the interface? to be able to return either a success or an error classes :)
sealed interface ApiOps<T> {
    data class Made<T>(val data: T) : ApiOps<T>
    data class Failed<T>(val exception: Exception) : ApiOps<T>

    fun onMade(block: (T) -> Unit): ApiOps<T> {
        if (this is Made) block(data)
        return this
    }
    fun onFailed(block: (Exception) -> Unit): ApiOps<T> {
        if (this is Failed) block(exception)
        return this
    }
}