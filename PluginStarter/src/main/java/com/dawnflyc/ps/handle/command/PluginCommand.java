package com.dawnflyc.ps.handle.command;

import com.dawnflyc.ps.PluginManager;
import com.dawnflyc.ps.handle.NetServer;
import io.netty.channel.Channel;

public class PluginCommand implements ICommand {
    @Override
    public boolean run(String[] command, Channel channel) {
        if (command.length > 0) {
            if ("list".equals(command[0])) {
                channel.writeAndFlush(NetServer.StrToData(PluginManager.plugins.keySet().toString()));
                return true;
            }
            if (command.length >= 2) {
                String name = command[1];
                switch (command[0]) {
                    case "load":
                        PluginManager.hardLoad(name);
                        PluginManager.softLoad(name);
                        channel.writeAndFlush(NetServer.StrToData("加载成功"));
                        break;
                    case "unload":
                        PluginManager.softUnload(name);
                        PluginManager.hardUnload(name);
                        channel.writeAndFlush(NetServer.StrToData("卸载成功"));
                        break;
//                    case "free":
////                        PluginManager.free(name);
//                        channel.writeAndFlush(NetServer.StrToData("释放成功"));
//                        break;
//                    case "reload":
////                        PluginManager.reload(name);
//                        channel.writeAndFlush(NetServer.StrToData("重载成功"));
//                        break;
                }
                return true;
            }
        }
        return false;
    }
}
