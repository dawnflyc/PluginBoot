package com.dawnflyc.ps;

import com.dawnflyc.ps.handle.CommandManage;
import com.dawnflyc.ps.handle.NetServer;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class SpringBootsrap implements ApplicationRunner, ApplicationListener<ContextClosedEvent>, ApplicationContextAware {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        PluginManager.hardLoads();
        PluginManager.softLoads();
        CommandManage.load();
        NetServer.load();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {

    }
}
