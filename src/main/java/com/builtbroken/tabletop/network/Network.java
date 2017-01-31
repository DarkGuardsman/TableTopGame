package com.builtbroken.tabletop.network;

import com.builtbroken.tabletop.network.client.ClientInitializer;
import com.builtbroken.tabletop.network.packet.Packet;
import com.builtbroken.tabletop.network.server.ServerHandler;
import com.builtbroken.tabletop.network.server.ServerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.List;

/**
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by Dark(DarkGuardsman, Robert) on 1/31/2017.
 */
public class Network
{
    public final boolean server;
    public boolean run = true;

    Channel clientChannel;

    public Network(boolean server)
    {
        this.server = server;
    }

    public void sendPacket(Packet packet, List<ChannelHandlerContext> exclude)
    {
        if(server)
        {
            for (Channel c: ServerHandler.channels)
            {
                if(!exclude.contains(c))
                {
                    c.writeAndFlush(packet);
                }
            }
        }
        else
        {
            clientChannel.writeAndFlush(packet);
        }
    }

    public void start(int port)
    {

    }

    public void start(String host, int port)
    {

    }

    protected void createConnectionToServer(String host, int port)
    {
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientInitializer());

            // Start the connection attempt.
            clientChannel = b.connect(host, port).sync().channel();

        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }

    protected void createServer(int port)
    {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer());

            b.bind(port).sync().channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
