package com.dhruv.angularapps.settings_app.groups

import android.content.pm.ApplicationInfo
import com.dhruv.angularapps.data.models.Group

/**
 * Builds suggested [Group]s from launcher apps and their [ApplicationInfo.category].
 * Uses int constants only (no Context) so it stays unit-testable.
 */
object AutoGroupBuilder {

    data class Suggestion(
        val group: Group,
        val totalCandidates: Int,
    )

    private data class Bucket(
        val categories: Set<Int>,
        val groupKey: String,
        val groupName: String,
    )

    /** Order preserved in output: games first, then media, etc. */
    private val buckets = listOf(
        Bucket(setOf(ApplicationInfo.CATEGORY_GAME), "games", "Games"),
        Bucket(setOf(ApplicationInfo.CATEGORY_AUDIO), "music", "Music"),
        Bucket(
            setOf(ApplicationInfo.CATEGORY_VIDEO, ApplicationInfo.CATEGORY_IMAGE),
            "video",
            "Photos & video",
        ),
        Bucket(setOf(ApplicationInfo.CATEGORY_SOCIAL), "groups", "Social"),
        Bucket(setOf(ApplicationInfo.CATEGORY_NEWS), "notes", "News"),
        Bucket(setOf(ApplicationInfo.CATEGORY_MAPS), "map", "Maps"),
        Bucket(setOf(ApplicationInfo.CATEGORY_PRODUCTIVITY), "work", "Work"),
        Bucket(setOf(ApplicationInfo.CATEGORY_ACCESSIBILITY), "settings", "Accessibility"),
    )

    fun suggest(
        appNames: Map<String, String>,
        appCategories: Map<String, Int>,
        maxAppsPerGroup: Int,
    ): List<Suggestion> {
        if (appNames.isEmpty() || appCategories.isEmpty()) return emptyList()

        val result = mutableListOf<Suggestion>()
        for (bucket in buckets) {
            val pkgs = appNames.keys
                .filter { pkg ->
                    val cat = appCategories[pkg] ?: ApplicationInfo.CATEGORY_UNDEFINED
                    cat in bucket.categories && cat != ApplicationInfo.CATEGORY_UNDEFINED
                }
                .sortedBy { appNames[it].orEmpty().lowercase() }

            if (pkgs.isEmpty()) continue

            val total = pkgs.size
            val capped = pkgs.take(maxAppsPerGroup)
            val group = Group(
                key = bucket.groupKey,
                name = bucket.groupName,
                apps = capped,
            )
            result.add(Suggestion(group = group, totalCandidates = total))
        }
        return result
    }
}
