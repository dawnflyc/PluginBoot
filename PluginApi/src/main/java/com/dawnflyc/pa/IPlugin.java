package com.dawnflyc.pa;

import org.pf4j.ExtensionPoint;

import java.util.LinkedList;
import java.util.List;

public abstract class IPlugin implements ExtensionPoint, ILifeCycle {

    /**
     * 依赖
     */
    public final List<String> dependents = new LinkedList<>();
    /**
     * 被依赖
     */
    public final List<String> beDependents = new LinkedList<>();

}
