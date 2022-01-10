package kr.sul.miscellaneousthings2.something.world

import com.github.shynixn.mccoroutine.launch
import kotlinx.coroutines.delay
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.ClassifyWorlds
import org.bukkit.Bukkit

object FixTimeInSomeWorlds {

    private const val TERM = 1*20.toLong()
    init {
        Bukkit.getScheduler().runTask(plugin) {
            Bukkit.getScheduler().runTaskTimer(plugin, {
                // 낮, 비X 고정
                ClassifyWorlds.beachTownWorlds.forEach {
                    it.time = 12500
                    it.setStorm(false)  // 비 X
                }
            }, 0, TERM)
        }
    }

    init {
        SpawnTimeCycle.dummy() // 초기화 용도
    }

    // 10분에 한 사이클 (12000 -> 13500 or 13500 -> 12000)
    object SpawnTimeCycle {
        private const val MIN = 12000
        private const val MAX = 13500
        private const val SCHEDULER_TERM = 20L
        private const val ONE_CYCLE_TERM = 10*60*20L
//        private const val TERM = 1*10*20L
        private const val smoothen = (MAX-MIN)*(SCHEDULER_TERM/ONE_CYCLE_TERM.toDouble())
//        private const val smoothen = 500*((1*20)/TERM.toDouble())


        private var currentTime = MIN.toDouble()
        private var direction = 1
        init {
            plugin.launch {
                delay(50L)
                val spawnWorlds = Bukkit.getWorlds().filter { it.name.startsWith("Spawn", true) }

                Bukkit.getScheduler().runTaskTimer(plugin, {
                    currentTime += (smoothen* direction)
                    spawnWorlds.forEach {
                        it.time = currentTime.toLong()
                        it.setStorm(false)
                    }
                    if (currentTime <= MIN || MAX <= currentTime) {
                        reverseDirection()
                    }
                }, 0L, SCHEDULER_TERM)
            }
        }

        private fun reverseDirection() {
            direction = if (direction == 1) {
                currentTime = MAX.toDouble() // 이건 굳이 할 필요는 없는데, 명령어 또는 다른 요인에 의해 강제로 시간이 변경됐을 때 시간을 다시 정상으로 되돌려 놓기 위한 장치
                -1
            } else {
                currentTime = MIN.toDouble()
                1
            }
        }
        fun dummy() { }
    }
}