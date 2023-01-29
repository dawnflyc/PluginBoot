package com.dawnflyc.pa;

/**
 * 插件生命周期
 */
public interface ILifeCycle {

    default void load(){}

    default void unload(){}
}
