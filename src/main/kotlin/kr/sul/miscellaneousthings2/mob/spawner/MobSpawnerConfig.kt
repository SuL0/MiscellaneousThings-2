package kr.sul.miscellaneousthings2.mob.spawner

import kr.sul.miscellaneousthings2.Main
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileOutputStream

object MobSpawnerConfig {
    private val configFile = File("${plugin.dataFolder}/mobspawner_config.yml")
    private val config = run {
        if (!configFile.exists()) {
            getDefaultConfig(configFile.name)!!.createNewFile()
        }
        val yamlConfig = YamlConfiguration()
        yamlConfig.load(configFile)
        yamlConfig
    }

    val worldNames: List<String> = config.getStringList("적용할월드_목록")
    val mobSpawningTerm = config.getInt("몹스폰_주기")
    val chanceOfSpawningMobDensely = config.getInt("밀집스폰_확률")

    val numRangeOfSpawningMobDensely = run {
        val split = config.getString("밀집스폰_몹_수_범위").split("~")
        NumRange(split[0].toInt(), split[1].toInt())
    }
    // 넉백 Section
    val AKnockbackWorldNames: List<String> = config.getStringList("넉백A_적용월드_목록")
    val AKnockbackStrength = config.getDouble("넉백A_강도")
    val BKnockbackWorldNames: List<String> = config.getStringList("넉백B_적용월드_목록")

    val BKnockbackStrength = config.getDouble("넉백B_강도")


    // 파일 없으면 자동으로 생성되겠지?
    // 근데 그럼 혹시 파일은 있는데, 내용이 비었으면 어떻게되지?

    data class NumRange(val min: Int, val max: Int)



    // Code from CrackShot
    /**
     * 플러그인 resources에 있는 파일 불러오기
     */
    private fun getDefaultConfig(fileName: String): File? {
        val file = File(plugin.dataFolder.toString() + "/" + fileName)
        val inputStream = Main::class.java.getResourceAsStream("/$fileName")
        return if (inputStream == null) {
            null
        } else {
            try {
                val output = FileOutputStream(file)
                val buffer = ByteArray(4096)
                val var6 = false
                var read: Int
                while (inputStream.read(buffer).also { read = it } > 0) {
                    output.write(buffer, 0, read)
                }
                inputStream.close()
                output.close()
                file
            } catch (var7: Exception) {
                null
            }
        }
    }
}