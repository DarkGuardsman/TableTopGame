package com.builtbroken.tabletop.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf>
{
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception
    {
        byte id = msg.readByte();
        Packet packet = null;
        switch (id)
        {

        }
        packet.decodeInto(ctx, msg);
    }
}
