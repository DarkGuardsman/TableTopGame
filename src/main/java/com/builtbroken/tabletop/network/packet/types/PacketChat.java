package com.builtbroken.tabletop.network.packet.types;

import com.builtbroken.tabletop.network.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class PacketChat extends Packet
{
    public String chat;

    public PacketChat()
    {
        super((byte)0);
    }

    public PacketChat(String chat)
    {
        this();
        this.chat = chat;
    }

    /**
     * Encode the packet data into the ByteBuf stream. Complex data sets may need specific data handlers
     *
     * @param ctx    channel context
     * @param buffer the buffer to encode into
     */
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {

    }

    /**
     * Decode the packet data from the ByteBuf stream. Complex data sets may need specific data handlers
     *
     * @param ctx    channel context
     * @param buffer the buffer to decode from
     */
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {

    }
}
