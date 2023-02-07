//package kr.sul.miscellaneousthings2.helicopter
//
//import kr.sul.miscellaneousthings2.Main.Companion.plugin
//import kr.sul.servercore.util.SpawnLocation
//import org.bukkit.Bukkit
//import org.bukkit.GameMode
//import org.bukkit.entity.ArmorStand
//import org.bukkit.entity.Player
//import org.bukkit.event.EventHandler
//import org.bukkit.event.EventPriority
//import org.bukkit.event.Listener
//import org.bukkit.event.entity.EntityDamageEvent
//import org.bukkit.event.player.PlayerJoinEvent
//
//object HelicopterPreventBug: Listener {
//    const val HELICOPTER_TAG = "HELI: isHelicopter"
//
//    // 서버 시작됐을 때 실행
//    init {
//        // 서버가 강제종료 됐거나, 오류로 인해서 남은 헬기 아머스탠드를 모두 제거
//        Bukkit.getScheduler().runTask(plugin) {
//            Bukkit.getWorlds().forEach { world ->
//                world.entities.filterIsInstance<ArmorStand>().forEach { armorStand ->
//                    if (armorStand.customName == HELICOPTER_TAG) {
//                        armorStand.remove()
//                    }
//                }
//            }
//        }
//    }
//
//
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    fun onJoin(e: PlayerJoinEvent) {
//        val p = e.player
//        if (!p.isOp && p.gameMode == GameMode.SPECTATOR) {
//            p.gameMode = GameMode.SURVIVAL
//            SpawnLocation.teleport(p)
//        }
//    }
//
//    @EventHandler(priority = EventPriority.LOW)
//    fun onDamage(e: EntityDamageEvent) {
//        if (e.entity is Player && e.entity.hasMetadata(RestrictPassenger.WAITING_FOR_PARACHUTE_TO_UNFOLD_KEY)) {
//            if (e.cause == EntityDamageEvent.DamageCause.FALL) {
//                e.isCancelled = true
//                e.entity.removeMetadata(RestrictPassenger.WAITING_FOR_PARACHUTE_TO_UNFOLD_KEY, plugin)
//            }
//        }
//    }
//
//
//
//
////    // 왜 우클하면 사라지지?
////    @EventHandler
////    fun onInteract(e: PlayerInteractAtEntityEvent) {
////        if (e.rightClicked is ArmorStand) {
////            val ent = e.rightClicked as ArmorStand
////            Bukkit.broadcastMessage("${ent.rightArmPose.x}, ${ent.rightArmPose.y}, ${ent.rightArmPose.z}")
////        }
////    }
//}