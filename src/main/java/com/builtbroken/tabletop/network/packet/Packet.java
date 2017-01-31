package com.builtbroken.tabletop.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public abstract class Packet
{
    public final byte id;

    public Packet(byte id)
    {
        this.id = id;
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

    /**
     * Handle a packet on the client side. Note this occurs after decoding has completed.
     */
    public void handleClientSide()
    {

    }

    /**
     * Handle a packet on the server side. Note this occurs after decoding has completed.
     */
    public void handleServerSide()
    {

    }
}
