package kr.sul.miscellaneousthings2.customitem.armor

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import kr.sul.miscellaneousthings2.Main.Companion.plugin
import kr.sul.miscellaneousthings2.customitem.armor.ArmorVendingMachine.getDefenseNBT
import kr.sul.miscellaneousthings2.customitem.armor.ArmorVendingMachine.getSpeedNBT
import net.minecraft.server.v1_12_R1.NBTTagCompound
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.min


// ArmorWeight class 참조
class ArmorPlayer(val p: Player): Listener {
    private var nbtHelmet: NbtDurabilityArmor? = null
    private var nbtChestPlate: NbtDurabilityArmor? = null
    private var nbtLeggings: NbtDurabilityArmor? = null
    private var nbtBoots: NbtDurabilityArmor? = null

    private var defense = 0F
    private var extraSpeed = 0F

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
        armorUpdate(PlayerArmorChangeEvent.SlotType.HEAD, p.inventory.helmet)
        armorUpdate(PlayerArmorChangeEvent.SlotType.CHEST, p.inventory.chestplate)
        armorUpdate(PlayerArmorChangeEvent.SlotType.LEGS, p.inventory.leggings)
        armorUpdate(PlayerArmorChangeEvent.SlotType.FEET, p.inventory.boots)
    }


    private fun armorUpdate(slotType: PlayerArmorChangeEvent.SlotType, originalNewItem: ItemStack?) {
        if (originalNewItem == nbtChestPlate?.item) return

        // 이전 아이템 정리
        val oldNmsItemTag: NBTTagCompound?
        when (slotType) {
            PlayerArmorChangeEvent.SlotType.HEAD -> {
                oldNmsItemTag = nbtHelmet?.nmsItemTag
                nbtHelmet = null
            }
            PlayerArmorChangeEvent.SlotType.CHEST -> {
                oldNmsItemTag = nbtChestPlate?.nmsItemTag
                nbtChestPlate = null
            }
            PlayerArmorChangeEvent.SlotType.LEGS -> {
                oldNmsItemTag = nbtLeggings?.nmsItemTag
                nbtLeggings = null
            }
            PlayerArmorChangeEvent.SlotType.FEET -> {
                oldNmsItemTag = nbtBoots?.nmsItemTag
                nbtBoots = null
            }
            else -> throw Exception("HEAD, CHEST, LEGS, FEET 중에 포함되지 않습니다-1.")
        }
        oldNmsItemTag?.run {
            defense -= getDefenseNBT(this)
            extraSpeed -= getSpeedNBT(this)
        }


        // 새로운 아이템 적용
        if (originalNewItem != null && originalNewItem.type != Material.AIR) {
            (originalNewItem as CraftItemStack).handle.tagOrDefault.run {
                defense += getDefenseNBT(this)
                extraSpeed += getSpeedNBT(this)
            }

            // NBT 태그가 없는 것(커스텀된 방어구가 아님)이 확인되면 constructor에서 알아서 null 보내 줌
            when (slotType) {
                PlayerArmorChangeEvent.SlotType.HEAD -> {
                    nbtHelmet = NbtDurabilityArmor.constructor(originalNewItem)
                }
                PlayerArmorChangeEvent.SlotType.CHEST -> {
                    nbtChestPlate = NbtDurabilityArmor.constructor(originalNewItem)
                }
                PlayerArmorChangeEvent.SlotType.LEGS -> {
                    nbtLeggings = NbtDurabilityArmor.constructor(originalNewItem)
                }
                PlayerArmorChangeEvent.SlotType.FEET -> {
                    nbtBoots = NbtDurabilityArmor.constructor(originalNewItem)
                }
                else -> throw Exception("HEAD, CHEST, LEGS, FEET 중에 포함되지 않습니다-2.")
            }
        }
        if (p.walkSpeed != 0.2F + extraSpeed) {
            p.walkSpeed = 0.2F + extraSpeed
        }
    }


    // e.newItem은 CraftItemStack이 아님.  e.player.inventory.chestplate는 CraftItemStack임
    // e.newItem은 clone된 ItemStack
    @EventHandler(priority = EventPriority.HIGH)
    fun onArmorChanged(e: PlayerArmorChangeEvent) {
        Bukkit.broadcastMessage("§conArmorChanged 기존: $defense $extraSpeed")
        if (e.originalNewItem != null && e.originalNewItem!!.type != Material.AIR) {
            Bukkit.broadcastMessage("§conArmorChanged new[${e.originalNewItem?.type}]: ${getDefenseNBT((e.originalNewItem as CraftItemStack).handle.tagOrDefault)} ${getSpeedNBT((e.originalNewItem as CraftItemStack).handle.tagOrDefault)}")
        }
        armorUpdate(e.slotType, e.originalNewItem)
    }



    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (e.entity != p) return
        // TODO fake로 플레이어에게 생성된 ArmorValue인지 정말 그냥 일반 갑옷이라서 그런건지 구분할 필요가 있음
        Bukkit.broadcastMessage("§eonDamage ${e.damage} / ${e.finalDamage}")
        if (canBeProtected.contains(e.cause)) {
            e.damage = e.finalDamage - (e.finalDamage * min(99/100.0, defense/100.0))
            nbtHelmet?.decreaseCurrentDurability(1)
            nbtChestPlate?.decreaseCurrentDurability(1)
            nbtLeggings?.decreaseCurrentDurability(1)
            nbtBoots?.decreaseCurrentDurability(1)
        }
    }

    fun destroy() {
        HandlerList.unregisterAll(this)
    }





    companion object {
        val canBeProtected = arrayListOf(
            EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
            EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
            EntityDamageEvent.DamageCause.ENTITY_ATTACK,
            EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK,
            EntityDamageEvent.DamageCause.CUSTOM,
            EntityDamageEvent.DamageCause.PROJECTILE,
            EntityDamageEvent.DamageCause.DRAGON_BREATH,
            EntityDamageEvent.DamageCause.THORNS,
            EntityDamageEvent.DamageCause.FALLING_BLOCK,
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.LAVA,
            EntityDamageEvent.DamageCause.HOT_FLOOR,
        )
    }
}