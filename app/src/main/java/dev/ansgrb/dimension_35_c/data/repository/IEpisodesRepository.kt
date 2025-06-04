package dev.ansgrb.dimension_35_c.data.repository

import dev.ansgrb.dimension_35_c.data.model.ApiOps
import dev.ansgrb.dimension_35_c.data.model.Episode

interface IEpisodesRepository {
    suspend fun forAllEpisodesFetch(): ApiOps<List<Episode>>
}
