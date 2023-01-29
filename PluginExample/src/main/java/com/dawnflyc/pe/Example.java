package com.dawnflyc.pe;

import com.dawnflyc.pa.IPlugin;
import org.pf4j.Extension;

@Extension
public class Example extends IPlugin {
    @Override
    public void load() {
        System.out.println("加载");
    }

    @Override
    public void unload() {
        System.out.println("卸载");
    }
}
