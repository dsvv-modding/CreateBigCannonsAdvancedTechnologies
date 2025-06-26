package com.dsvv.cbcat.debugUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class DebugUtils
{
    public static void displayCustomClientMessage(String text) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.displayClientMessage(Component.literal(text), true);
        }
    }
}
