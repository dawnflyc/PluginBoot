package com.dawnflyc.ps.handle.command;

import io.netty.channel.Channel;

public interface ICommand {
    boolean run(String[] command, Channel channel);
}
