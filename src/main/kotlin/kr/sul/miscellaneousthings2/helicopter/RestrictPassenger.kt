//package kr.sul.miscellaneousthings2.helicopter
//
//import kr.sul.servercore.util.SpawnLocation
//import org.bukkit.GameMode
//import org.bukkit.entity.Player
//import org.bukkit.event.EventHandler
//import org.bukkit.event.EventPriority
//import org.bukkit.event.Listener
//import org.bukkit.event.entity.EntityDamageByEntityEvent
//import org.bukkit.event.entity.EntityDamageEvent
//import org.bukkit.event.player.PlayerCommandPreprocessEvent
//import org.bukkit.event.player.PlayerQuitEvent
//
//object RestrictPassenger: Listener {
//    val helicopterPassengers = arrayListOf<Player>()
//    const val WAITING_FOR_PARACHUTE_TO_UNFOLD_KEY = "WAITING_FOR_PARACHUTE_TO_UNFOLD"  // 자동으로 낙하산을 펼치기를 대기하고 있는 상태
//
//
//    @EventHandler(priority = EventPriority.LOWEST)
//    fun onQuit(e: PlayerQuitEvent) {
//        if (helicopterPassengers.contains(e.player)) {
//            SpawnLocation.teleport(e.player)
//            e.player.gameMode = GameMode.SURVIVAL
//            helicopterPassengers.remove(e.player)
//        }
//    }
//
//    @EventHandler(priority = EventPriority.HIGH)
//    fun onCommand(e: PlayerCommandPreprocessEvent) {
//        if (helicopterPassengers.contains(e.player)) {
//            e.isCancelled = true
//            e.player.sendMessage("§6§lHELI: §c헬리콥터를 이용하는 중의 명령어 입력은 금지되어 있습니다.")
//        }
//    }
//
//
//    // 낙하산 또는 낙하 대기중의 상태에서의 데미지를 "입는것/입히는것" 을 절반으로 감소시키기
//    @EventHandler(priority = EventPriority.LOW)
//    fun onDamage(e: EntityDamageEvent) {
//        if (e.entity is Player && e.entity.hasMetadata(WAITING_FOR_PARACHUTE_TO_UNFOLD_KEY)) {
//            if (e.cause != EntityDamageEvent.DamageCause.FALL) {
//                e.damage = e.damage/2
//            }
//        }
//        else if (e is EntityDamageByEntityEvent) {
//            if (e.damager is Player && e.damager.hasMetadata(WAITING_FOR_PARACHUTE_TO_UNFOLD_KEY)) {
//                e.damage = e.damage/2
//            }
//        }
//    }
//}