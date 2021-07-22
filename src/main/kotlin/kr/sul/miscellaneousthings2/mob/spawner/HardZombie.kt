package kr.sul.miscellaneousthings2.mob.spawner

import com.comphenix.protocol.wrappers.WrappedGameProfile
import com.comphenix.protocol.wrappers.WrappedSignedProperty
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.ClassifyWorlds
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise
import org.bukkit.Bukkit
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import java.util.*


object HardZombie: Listener {
    val playerProfile = run {
        val profile = WrappedGameProfile(UUID.randomUUID(), "")
//        val profile = Bukkit.createProfile(UUID.randomUUID(), null)
//        profile.properties.add(ProfileProperty("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYyNjg4MTUxODU1MCwKICAicHJvZmlsZUlkIiA6ICI3MjQ3ZWMyYTZlZGQ0NDU4ODI2ZmZjN2Y3ZGZlN2U5NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJQZXJwZXR1YWxEZW1vbnMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY5NzBiNTZlYjE5ZjBhMDJjZmZhMjEwNWE0ODljMGE2ODgyYjZjMWQyZGZmZjY4MWZiODdlNTZiZWVhOTNkZiIKICAgIH0KICB9Cn0="))
        profile.properties.put("textures", WrappedSignedProperty("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYyNjg4MTUxODU1MCwKICAicHJvZmlsZUlkIiA6ICI3MjQ3ZWMyYTZlZGQ0NDU4ODI2ZmZjN2Y3ZGZlN2U5NiIsCiAgInByb2ZpbGVOYW1lIiA6ICJQZXJwZXR1YWxEZW1vbnMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY5NzBiNTZlYjE5ZjBhMDJjZmZhMjEwNWE0ODljMGE2ODgyYjZjMWQyZGZmZjY4MWZiODdlNTZiZWVhOTNkZiIKICAgIH0KICB9Cn0=", "m5ml1shSypWwAHFunNP0KMIDotDo6Bh14ED3aZ4AjTFqptpm3QI9neBouRup+xKAaJpTwbWwPX4d1jiWuExZ6bdVk/RFRNoLB55X0If0ljd6IAqtUFIMVggE4ptboxDmppKx/xwvjLCJn7bkB+YQlwVNjrQC5O9mKuYB96aNqx/aRUHEp0I1dZbsUG0IDj9Cm6vq1+dP7qCAgexELjP3bK6yDDBSkjC3xhuqvCb+ahh1y5UO9REcyms7gNjB4fSqUrpE5oMiDIvJHs86fKUVb72zO0xoluhz62nb6E7Ia+DcFZD4shQtL1cETpARNMN2uVxN3UL3DA4yok4KrhFzG/pC/UuhaeLbyahXrbkFTEXLMzGdKTvHxA462bWHKu84jOc/D/pRcffU3SZxhJrEiD4NVEMY7RJCwZ5jZUKf24DEGGrFy8/aSC9fHrAIwrG2hqamunK5nMLP1aMkURfBM4Yqr6labTcqh0nWG3b0xT9Q2bJwOWZ7ynbwth6nPSeUoFc/uw7N5tT9JM8hD8a0Ysjy+kwTvlrMmQTx/FI5vv0yC/kGk0yEsEn7k+wcezSZBDRgRiXqUtD1gi0F0+OW98LLkAfe34wIV2kUanvpqmBWyiOLQNPeq93q1O3IgGhY1IHeOOXq/TZL9Q6afrpso0PlJNN7ZtwjR3w2JvKPIhM="))
        profile
    }

    init {
        Bukkit.getScheduler().runTask(plugin) {
            for (hardWorld in ClassifyWorlds.hardWorlds) {
                hardWorld.entities.filterIsInstance<Zombie>().forEach { zombie ->

                }
            }
        }
    }

    @EventHandler
    fun onZombieSpawn(e: EntitySpawnEvent) {
        if (e.entity is Zombie && ClassifyWorlds.hardWorlds.contains(e.location.world)) {
            val mobDisguise = PlayerDisguise("", "Zombie")
            mobDisguise.entity = e.entity
            mobDisguise.startDisguise()
        }
    }
}