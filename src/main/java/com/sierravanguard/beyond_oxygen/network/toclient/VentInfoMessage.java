package com.sierravanguard.beyond_oxygen.network.toclient;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class VentInfoMessage extends ToClientPacket {
    private final boolean isSealed;
    private final boolean hasAir;
    private final float oxygenRate;

    public VentInfoMessage(boolean isSealed, boolean hasAir, float oxygenRate) {
        this.isSealed = isSealed;
        this.hasAir = hasAir;
        this.oxygenRate = oxygenRate;
    }

    public VentInfoMessage(FriendlyByteBuf buffer) {
        this.isSealed = buffer.readBoolean();
        this.hasAir = buffer.readBoolean();
        this.oxygenRate = buffer.readFloat();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(isSealed);
        buffer.writeBoolean(hasAir);
        buffer.writeFloat(oxygenRate);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleClient(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            Component message = isSealed
                    ? Component.translatable("message.vent.oxygen_flow", oxygenRate).withStyle(hasAir ? ChatFormatting.AQUA : ChatFormatting.RED)
                    : Component.translatable("message.vent.not_sealed").withStyle(ChatFormatting.RED);

            mc.player.displayClientMessage(message, true);
        }
    }
}
