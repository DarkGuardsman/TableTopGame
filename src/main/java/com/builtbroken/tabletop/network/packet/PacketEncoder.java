package com.builtbroken.tabletop.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class PacketEncoder extends MessageToMessageEncoder<Packet>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception
    {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(msg.id);
        msg.encodeInto(ctx, buffer);
        out.add(msg);
        out.add('\r');
    }
}
