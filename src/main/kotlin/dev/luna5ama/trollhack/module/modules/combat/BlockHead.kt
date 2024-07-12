package dev.luna5ama.trollhack.module.modules.combat

import dev.luna5ama.trollhack.event.events.TickEvent
import dev.luna5ama.trollhack.event.safeParallelListener
import dev.luna5ama.trollhack.gui.hudgui.elements.client.Notification
import dev.luna5ama.trollhack.manager.managers.CombatManager
import dev.luna5ama.trollhack.manager.managers.HotbarSwitchManager.ghostSwitch
import dev.luna5ama.trollhack.module.Category
import dev.luna5ama.trollhack.module.Module
import dev.luna5ama.trollhack.util.inventory.slot.allSlotsPrioritized
import dev.luna5ama.trollhack.util.inventory.slot.firstBlock
import dev.luna5ama.trollhack.util.threads.runSafe
import dev.luna5ama.trollhack.util.world.PlacementSearchOption
import dev.luna5ama.trollhack.util.world.getPlacementSequence
import dev.luna5ama.trollhack.util.world.placeBlock
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos

internal object BlockHead : Module(
    name = "Block Head",
    category = Category.COMBAT,
    description = "Fast head trap"
) {
    private val autoDisable by setting("Auto Disable", true)
    init {
        safeParallelListener<TickEvent.Post> {
            runSafe {
                val target = CombatManager.target ?: run {
                    Notification.send(BlockHead, "No target found")
                    return@runSafe
                }

                val obsidianSlot = player.allSlotsPrioritized.firstBlock(Blocks.OBSIDIAN) ?: run {
                    Notification.send(BlockHead, "No obsidian found")
                    return@runSafe
                }

                val abovePos = BlockPos(target.posX, target.entityBoundingBox.maxY + 1.0, target.posZ)

                val sequence = getPlacementSequence(
                    abovePos,
                    5,
                    PlacementSearchOption.range(5.0),
                    PlacementSearchOption.ENTITY_COLLISION
                ) ?: run {
                    Notification.send(BlockHead, "No valid placement found")
                    return@runSafe
                }

                for (placeInfo in sequence) {
                    ghostSwitch(obsidianSlot) {
                        placeBlock(placeInfo)
                    }
                }
            }
            if (autoDisable) disable()
        }
    }
}