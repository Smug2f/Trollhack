package dev.luna5ama.trollhack.gui.hudgui

import dev.luna5ama.trollhack.gui.rgui.Component
import dev.luna5ama.trollhack.setting.GuiConfig
import dev.luna5ama.trollhack.setting.settings.SettingRegister

internal abstract class LabelHud(
    name: String,
    alias: Array<String> = emptyArray(),
    category: Category,
    description: String,
    alwaysListening: Boolean = false,
    enabledByDefault: Boolean = false
) : AbstractLabelHud(name, alias, category, description, alwaysListening, enabledByDefault, GuiConfig),
    SettingRegister<Component> by GuiConfig