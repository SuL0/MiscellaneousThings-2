package kr.sul.miscellaneousthings2.warpgui.data

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.servercore.util.UptimeBasedOnTick
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.File

object WarpPlayerDataMgr: Listener {
    private val dataFile = File("${plugin.dataFolder}/data_warpplayer.yml")
    private val dataYaml = run {
        if (!dataFile.exists()) {
            dataFile.createNewFile()
        }
        val yamlConfig = YamlConfiguration()
        yamlConfig.load(dataFile)
        yamlConfig
    }
    private var latestYamlSave = UptimeBasedOnTick.uptimeBasedOnTick
    private const val YAML_SAVE_TERM = (15*60*20).toLong()

    private val dataStorage = hashMapOf<Player, PlayerData>()



    private fun loadDataFromFile(p: Player): PlayerData? {
        val uuidStr = p.uniqueId.toString()
        if (dataYaml.contains(uuidStr)) {
            return PlayerData.fromJsonString(dataYaml.getString(uuidStr))
        }
        return null
    }

    private fun saveDataToFile(p: Player) {
        val uuidStr = p.uniqueId.toString()
        val data = getPlayerData(p)
        dataYaml[uuidStr] = data.toJsonString()

        if (UptimeBasedOnTick.uptimeBasedOnTick - latestYamlSave >= YAML_SAVE_TERM) {
            dataYaml.save(dataFile)
        }
    }



    fun getPlayerData(p: Player): PlayerData {
        if (dataStorage.contains(p)) {
            return dataStorage[p]!!
        }
        // TODO 만약 데이터 불러오는게 MySQL(Async) 로 바뀌게 되면 이 부분도 수정을 좀 해줘야 함
        throw Exception(p.name)
    }



    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val p = e.player
        val dataLoadedFromFile = loadDataFromFile(p)
        if (dataLoadedFromFile != null) {
            Bukkit.broadcastMessage("loaded")
            dataStorage[p] = dataLoadedFromFile
        } else {
            Bukkit.broadcastMessage("created new obj")
            dataStorage[p] = PlayerData(p.uniqueId.toString(), true)
        }
    }
    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        saveDataToFile(e.player)
        dataStorage.remove(e.player)
    }
    /**
     * dataStorage에 남은 데이터 모두 저장 후 File에도 저장
     */
    fun onPluginDisable() {
        latestYamlSave = UptimeBasedOnTick.uptimeBasedOnTick
        dataStorage.keys.forEach {
            saveDataToFile(it)
        }
        dataYaml.save(dataFile)
    }
}