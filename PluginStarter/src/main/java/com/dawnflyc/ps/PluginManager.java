package com.dawnflyc.ps;

import com.dawnflyc.pa.IPlugin;
import org.pf4j.*;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 插件控制器
 */
public class PluginManager extends JarPluginManager {
    /**
     * 已加载插件
     */
    public static final Map<String, IPlugin> plugins = new HashMap<>();
    /**
     * 插件管理器
     */
    private static final PluginManager pluginManager = new PluginManager();
    /**
     * 信息查找对象
     */
    private static final PluginDescriptorFinder pluginDescriptorFinder = pluginManager.getPluginDescriptorFinder();

    /**
     * 硬加载所有
     */
    public static void hardLoads() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
    }

    /**
     * 软加载所有
     */
    public static void softLoads() {
        for (PluginWrapper pluginWrapper : pluginManager.getPlugins()) {
            load(pluginWrapper);
        }
    }

    /**
     * 硬卸载所有
     */
    public static void hardUnloads() {
        pluginManager.stopPlugins();
        pluginManager.unloadPlugins();
    }

    /**
     * 软卸载所有
     */
    public static void softUnloads() {
        for (String key : plugins.keySet()) {
            unload(key);
        }
        plugins.clear();
    }

    /**
     * 硬加载
     *
     * @param name 插件名
     */
    public static void hardLoad(String name) {
        PluginWrapper plugin = pluginManager.getPlugin(name);
        if (plugin != null) {
            return;
        }
        Path path = findPlugin(name);
        PluginDescriptor pluginDescriptor = pluginDescriptorFinder.find(path);
        List<PluginDependency> dependencies = pluginDescriptor.getDependencies();
        if (!dependencies.isEmpty()) {
            for (PluginDependency dependency : dependencies) {
                hardLoad(dependency.getPluginId());
            }
        }
        pluginManager.loadPlugin(path);
        pluginManager.startPlugin(name);

    }

    /**
     * 软加载
     *
     * @param name 插件名
     */
    public static void softLoad(String name) {
        load(pluginManager.getPlugin(name));
    }

    /**
     * 硬卸载
     *
     * @param name 插件名
     */
    public static void hardUnload(String name) {
        pluginManager.stopPlugin(name, true);
        pluginManager.unloadPlugin(name, true);
    }

    /**
     * 软卸载
     *
     * @param name 插件名
     */
    public static void softUnload(String name) {
        unload(name);
    }

    /**
     * 查找插件地址
     *
     * @param name 插件名
     * @return 地址
     */
    private static Path findPlugin(String name) {
        List<Path> pluginPathList = pluginManager.pluginRepository.getPluginPaths();
        for (Path path : pluginPathList) {
            PluginDescriptor pluginDescriptor = pluginDescriptorFinder.find(path);
            if (pluginDescriptor.getPluginId().equals(name)) {
                return path;
            }
        }
        return null;
    }

    /**
     * 加载
     *
     * @param pluginWrapper 插件
     */
    private static void load(PluginWrapper pluginWrapper) {
        if (plugins.containsKey(pluginWrapper.getPluginId())) {
            return;
        }
        PluginDescriptor descriptor = pluginWrapper.getDescriptor();
        List<PluginDependency> dependencies = descriptor.getDependencies();
        List<IPlugin> extensions = pluginManager.getExtensions(IPlugin.class, pluginWrapper.getPluginId());
        if (!extensions.isEmpty()) {
            plugins.put(pluginWrapper.getPluginId(), extensions.get(0));
        }
        if (!dependencies.isEmpty()) {
            for (PluginDependency dependency : dependencies) {
                String dependencyPluginId = dependency.getPluginId();
                PluginWrapper plugin = pluginManager.getPlugin(dependencyPluginId);
                load(plugin);
                //添加关系
                IPlugin self = plugins.get(pluginWrapper.getPluginId());
                IPlugin dep = plugins.get(dependencyPluginId);
                self.dependents.add(dependencyPluginId);
                dep.beDependents.add(pluginWrapper.getPluginId());
            }
        }
        plugins.get(pluginWrapper.getPluginId()).load();
    }

    /**
     * 卸载
     *
     * @param name 插件
     */
    private static void unload(String name) {
        PluginWrapper plugin = pluginManager.getPlugin(name);
        if (plugin == null) {
            return;
        }
        IPlugin iPlugin = plugins.get(name);
        if (!iPlugin.beDependents.isEmpty()) {
            for (String bedependency : iPlugin.beDependents) {
                unload(bedependency);
            }
        }
        plugins.get(name).unload();
        plugins.remove(name);
    }

}
