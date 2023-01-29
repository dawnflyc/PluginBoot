package com.dawnflyc.ps.handle;


import com.dawnflyc.ps.handle.command.ICommand;
import io.netty.channel.Channel;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class CommandManage {

    public static final List<ICommand> commands = new ArrayList<>();

    public static void load() {
        for (Class<? extends ICommand> superType : new Reflections(ICommand.class.getPackage().getName()).getSubTypesOf(ICommand.class)) {
            try {
                commands.add(superType.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void handle(String msg, Channel channel) {
        String[] temp = msg.split(" ");
        boolean run = false;
        for (ICommand command : commands) {
            if (command.run(temp, channel)) {
                run = true;
                break;
            }
        }
        if (!run) {
            channel.writeAndFlush(NetServer.StrToData("找不到命令:" + msg));
        }
    }

}
