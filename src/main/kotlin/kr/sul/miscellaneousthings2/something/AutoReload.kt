package kr.sul.miscellaneousthings2.something

import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.something.AutoReload.pluginsFolder
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.logging.Level


object AutoReload {
    private val timeSinceLastChanged: MutableMap<String, Long> = hashMapOf()
    private val fileToPluginName: HashMap<String, String> = hashMapOf()
    var pluginsFolder: File = File("${Bukkit.getServer().worldContainer.path}/plugins")

    init {
        // Generate list of times
        logTimes()
        // Get plugin jar to plugin name
        getPlugins()
        // Set it to check every so often
        object : BukkitRunnable() {
            override fun run() {
                checkIfModified()
            }
        }.runTaskTimerAsynchronously(plugin, 1, 20) //auto complete this statement

    }

    private fun getPlugins() {
        val plugins = Bukkit.getServer().pluginManager.plugins
        for (i in plugins.indices) {
            val plugin = plugins[i]
            var location = plugin.javaClass.protectionDomain.codeSource.location.toString() + ""
            val temp = location.split("/".toRegex()).toTypedArray()
            location = temp[temp.size - 1]
            location = try {
                URLDecoder.decode(location, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                Bukkit.getLogger().log(Level.SEVERE, "Your java doesn't support converting to UTF-8! Please update it before using Auto Reload")
                return
            }
            fileToPluginName[location] = plugin.name
        }
    }

    private fun checkIfModified() {
        val listOfFiles = pluginsFolder.listFiles()!!
        for (i in listOfFiles.indices) {
            val fileName = listOfFiles[i].name
            if (listOfFiles[i].isFile && fileName.endsWith(".jar")) {
                // Its a plugin, check if its on our list
                if (timeSinceLastChanged.containsKey(fileName)) {
                    //Should be checking this one
                    val time = timeSinceLastChanged[fileName]!!
                    if (time < listOfFiles[i].lastModified()) {
                        //Has been modified!
                        val pluginName = fileToPluginName[fileName] ?: continue

                        //Send command synchronously
                        Bukkit.getScheduler().callSyncMethod(plugin) {
                                Bukkit.getServer()
                                    .dispatchCommand(Bukkit.getConsoleSender(), "plugman reload $pluginName")
                            }
                        sendMessageToOpPlayers("Successfully reloaded $pluginName!")
                        timeSinceLastChanged.remove(fileName)
                        timeSinceLastChanged[fileName] = listOfFiles[i].lastModified()
                    }
                }
            }
        }
    }

    private fun logTimes() {
        val listOfFiles: Array<File> = pluginsFolder.listFiles()!!
        for (i in listOfFiles.indices) {
            if (listOfFiles[i].isFile && listOfFiles[i].name.endsWith(".jar")) {
                sendMessageToOpPlayers("Found plugin " + listOfFiles[i].name)
                timeSinceLastChanged[listOfFiles[i].name] = listOfFiles[i].lastModified()
            }
        }
    }

    private fun sendMessageToOpPlayers(message: String) {
        Bukkit.getOnlinePlayers().filter { it.isOp }.forEach {
            it.sendMessage(message)
        }
    }
}
