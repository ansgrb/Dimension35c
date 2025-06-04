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
package dev.ansgrb.dimension_35_c.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.Binds
import dagger.hilt.components.SingletonComponent
import dev.ansgrb.dimension_35_c.data.repository.CharacterRepository
import dev.ansgrb.dimension_35_c.data.repository.EpisodesRepository
import dev.ansgrb.dimension_35_c.data.repository.ICharacterRepository
import dev.ansgrb.dimension_35_c.data.repository.IEpisodesRepository
import dev.ansgrb.network.KtorClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideKtorClient(): KtorClient {
        return KtorClient()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        characterRepository: CharacterRepository
    ): ICharacterRepository

    @Binds
    @Singleton
    abstract fun bindEpisodesRepository(
        episodesRepository: EpisodesRepository
    ): IEpisodesRepository
}