package kr.sul.miscellaneousthings2.endervaultsaddon

import com.github.dig.endervaults.api.EnderVaultsPlugin
import com.github.dig.endervaults.api.lang.Lang
import com.github.dig.endervaults.bukkit.ui.selector.SelectorConstants
import com.github.dig.endervaults.bukkit.ui.selector.SelectorInventory
import de.tr7zw.nbtapi.NBTItem
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import net.luckperms.api.LuckPerms
import net.luckperms.api.node.Node
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.inventory.ItemStack
import java.util.*


object SelectorListener: Listener {
    private lateinit var luckPerms: LuckPerms
    init {
        if (Bukkit.getPluginManager().isPluginEnabled("EnderVaults")) {
            Bukkit.getPluginManager().registerEvents(SelectorListener, plugin)

            val provider = Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)
            if (provider != null) {
                luckPerms = provider.provider
            } else {
                throw Exception("Bukkit.getServicesManager().getRegistration(LuckPerms::class.java) 에 실패")
            }
        }
    }

    // EnderVaults가 필수 플러그인이 아니기에 없을 수도 있고, dependsOn에 들어가지 않아 EnderVaults가 이 플러그인보다 나중에 로드될 수도 있기 때문.
    private val enderVaultsPlugin = lazy { (Bukkit.getPluginManager().getPlugin("EnderVaults") as EnderVaultsPlugin) }
    private val enderVaultsConfig = lazy { enderVaultsPlugin.value.configFile.configuration as FileConfiguration }


    // 열쇠를 드래그 한 채로 잠긴 Vault를 클릭하면 Vault 잠금을 해제함
    @EventHandler
    fun onClickWithKey(e: InventoryClickEvent) {
        if (e.cursor != null && VaultKey.isKeyItem(e.cursor)
            && e.currentItem != null &&e.currentItem.type != Material.AIR) {
            val nbtItem = NBTItem(e.currentItem)

            // GUI 이름이 Lang.GUI_SELECTOR_TITLE 인지 확인하고, Vault 아이템인지도 확인
            if (e.inventory.name == enderVaultsPlugin.value.language.get(Lang.VAULT_SELECTOR_TITLE)
                && nbtItem.hasKey(SelectorConstants.NBT_VAULT_ITEM)
                && e.currentItem.itemMeta.displayName == ChatColor.translateAlternateColorCodes('&', enderVaultsConfig.value.getString("selector.template.locked.title"))) {   // itemMeta 없어도 NPE ㄴ?

                val clickedVaultPermission = "endervaults.vault.${e.slot +1}"
                // 펄미션 체크하고, 잠긴 Vault 구매
                if (!hasPermission(e.whoClicked as Player, clickedVaultPermission)) {
                    // 펄미션 추가
                    addPermission(e.whoClicked.uniqueId, clickedVaultPermission)  // TODO 아마 클릭한 창고 번호는 슬롯을 통해서 유추하는 방법밖에 없을 듯?  아니면 차라리 endervault 열 때 잠긴 창고는 새로운 아이템으로 다 덮어씌우던가
                    if (e.cursor.amount == 1) {
                        e.whoClicked.itemOnCursor = ItemStack(Material.AIR)
                    }
                    // EnderVaults GUI 새로고침 (닫기 -> 열기)
                    e.whoClicked.closeInventory()
                    SelectorInventory(e.whoClicked.uniqueId, 1).launchFor(e.whoClicked as Player)
                }
            }
        }
    }


    fun addPermission(userUuid: UUID, permission: String) {
        // < 방법 1 >
        // Load, modify, then save
        luckPerms.userManager.modifyUser(userUuid) { user ->
            user.data().add(Node.builder(permission).build())
        }

        // < 방법 2 >
        // val user = luckPerms.userManager.getUser(e.whoClicked.uniqueId)!!
        // val result = user.data().add(Node.builder(permissionToAdd).build())
        // luckPerms.userManager.saveUser(user)
    }

    fun hasPermission(p: Player, permission: String): Boolean {
        val user = luckPerms.getPlayerAdapter(Player::class.java).getUser(p)
        return user.cachedData.permissionData.checkPermission(permission).asBoolean()
    }


    @EventHandler
    fun onCommand(e: PlayerCommandPreprocessEvent) {
        if (e.message == "/g창고열쇠" && e.player.isOp) {
            e.player.inventory.addItem(VaultKey.WOODEN_KEY.getItem())
            e.player.sendMessage("§c§lKEY: §7OP의 권한으로 창고 열쇠를 가져왔습니다.")
            e.isCancelled = true
        }
    }
}