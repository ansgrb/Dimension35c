package dev.ansgrb.dimension_35_c.data.repository

import dev.ansgrb.dimension_35_c.data.model.ApiOps
import dev.ansgrb.dimension_35_c.data.model.CharacterFilter
import dev.ansgrb.dimension_35_c.data.model.CharacterPage
import dev.ansgrb.dimension_35_c.data.model.Dimension34cCharacter
import dev.ansgrb.dimension_35_c.data.model.Episode

interface ICharacterRepository {
    suspend fun fetchCharacters(page: Int): ApiOps<CharacterPage>
    suspend fun fetchCharacter(characterId: Int): ApiOps<Dimension34cCharacter>
    suspend fun fetchEpisodes(episodeIds: List<Int>): ApiOps<List<Episode>>
    suspend fun searchCharacters(filter: CharacterFilter): ApiOps<CharacterPage>
}
