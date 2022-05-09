package kr.sul.miscellaneousthings2.something.world.spawn

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.ClassifyWorlds
import kr.sul.servercore.util.MsgPrefix
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.EquipmentSlot


// 플레이어 무적
object SpawnWorldFeatures: Listener {
    private val targetWorlds = ClassifyWorlds.spawnWorlds
    private val PREFIX = MsgPrefix.get("URL")
    val HITBOX_ENT_TYPE = EntityType.SLIME
    const val HITBOX_ENT_NAME = "[SPAWN-HITBOX]"
    private val hitboxEntMap = hashMapOf<World, ArrayList<HitboxEntity>>()

    init {
        Bukkit.getScheduler().runTask(plugin) {
            targetWorlds.forEach { world ->
                world.entities.forEach { ent ->
                    if (ent.customName != null && ent.customName.startsWith(HITBOX_ENT_NAME)) {
                        ent.remove()
                    }
                }
            }
            targetWorlds.forEach { world ->
                hitboxEntMap[world] = arrayListOf()
                for (i in 0..2) {
                    for (j in 0..2) {
                        // 네이버 카페
                        hitboxEntMap[world]!!.add(
                            HitboxEntity.spawnHitBox(Location(world, 824.5+i, 53.3+j, 769.0, -180F, 0F))
                        )
                        // 위성지도
                        hitboxEntMap[world]!!.add(
                            HitboxEntity.spawnHitBox(Location(world, 830.5+i, 53.3+j, 769.0, -180F, 0F))
                        )
                        // 디스코드
                        hitboxEntMap[world]!!.add(
                            HitboxEntity.spawnHitBox(Location(world, 836.5+i, 53.3+j, 769.0, -180F, 0F))
                        )
                    }
                }
            }
            maintainHitBox()
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onDamage(e: EntityDamageByEntityEvent) {
        if (!e.isCancelled
                && e.entityType == HITBOX_ENT_TYPE
                && targetWorlds.contains(e.entity.world)) {
            e.isCancelled = true

            if (e.damager is Player) {
                maybePlayerClickedTheBoard(
                    e.damager as Player,
                    e.entity.location.x,
                    e.entity.location.y,
                    e.entity.location.z
                )
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onClickBoardInSpawn(e: PlayerInteractEntityEvent) {
        if (e.hand == EquipmentSlot.OFF_HAND) return // Interact가 한 번 클릭에 HAND, OFF_HAND 두 번 실행되기 때문
        if (!e.isCancelled
                && e.rightClicked.type == HITBOX_ENT_TYPE
                && targetWorlds.contains(e.rightClicked.world)) {
            maybePlayerClickedTheBoard(
                e.player,
                e.rightClicked.location.x,
                e.rightClicked.location.y,
                e.rightClicked.location.z
            )
        }
    }

    private fun maybePlayerClickedTheBoard(p: Player, x: Double, y: Double, z: Double) {
        // 네이버 카페
        if (x in 824.0..827.0 && y in 53.3..56.0 && z in 768.9..770.0) {
            val jsonMsg = arrayListOf<TextComponent>().run {
                add(TextComponent("${PREFIX}§ahttps://cafe.naver.com/mineunioncommunity §7§o< Click").run {
                    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("§e클릭하여 해당 URL으로 이동")))
                    clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://cafe.naver.com/mineunioncommunity")
                    this
                })
                this
            }
            p.sendMessage("")
            p.sendMessage("")
            p.sendMessage(*jsonMsg.toTypedArray())
            p.sendMessage("")
        }
        // 위성지도
        else if (x in 830.0..833.0 && y in 53.3..56.0 && z in 768.9..770.0) {
            val jsonMsg = arrayListOf<TextComponent>().run {
                add(TextComponent("${PREFIX}§bhttps://cafe.naver.com/mineunioncommunity §7§o< Click").run {
                    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("§e클릭하여 해당 URL으로 이동")))
                    clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://cafe.naver.com/mineunioncommunity")
                    this
                })
                this
            }
            p.sendMessage("")
            p.sendMessage("")
            p.sendMessage(*jsonMsg.toTypedArray())
            p.sendMessage("")
        }
        // 디스코드
        else if (x in 836.0..839.0 && y in 53.3..56.0 && z in 768.9..770.0) {
            val jsonMsg = arrayListOf<TextComponent>().run {
                add(TextComponent("${PREFIX}§9https://discord.gg/qYsttuEUfv §7§o< Click").run {
                    hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("§e클릭하여 해당 URL으로 이동")))
                    clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/qYsttuEUfv")
                    this
                })
                this
            }
            p.sendMessage("")
            p.sendMessage("")
            p.sendMessage(*jsonMsg.toTypedArray())
            p.sendMessage("")
        }
    }

    @EventHandler
    fun onKilled(e: EntityDeathEvent) {
        if (e.entity.customName != null && e.entity.customName == HITBOX_ENT_NAME) {
            e.isCancelled = true
        }
    }

    // 명령어로 제거되면 onKilled에서 e.isCancelled가 무시되기 때문
    private fun maintainHitBox() {
        var cnt = 1
        Bukkit.getScheduler().runTaskTimer(plugin, {
            if (cnt++ % 10 == 0) {
                for (hitboxList in hitboxEntMap.values) {
                    hitboxList.forEach(HitboxEntity::spawnNewOneIfItIsRemoved)
                }
            } else {
                for (world in hitboxEntMap.keys) {
                    if (hitboxEntMap[world]!!.first().spawnNewOneIfItIsRemoved()) {
                        cnt = 10
                    }
                }
            }
        }, 20*1L, 20*1L)
    }
}
