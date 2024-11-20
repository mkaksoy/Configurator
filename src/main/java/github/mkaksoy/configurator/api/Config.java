package github.mkaksoy.configurator.api;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Config {
    private final Plugin plugin;

    public Config(Plugin plugin) {
        this.plugin = plugin;
    }

    public <T> T get(String path, Class<T> type, T defaultValue, boolean strict) {
        Object value = plugin.getConfig().get(path);

        if (value == null) {
            if (strict) {
                throw new IllegalArgumentException("Path not found: " + path);
            } else {
                return defaultValue;
            }
        }

        if (!type.isInstance(value)) {
            throw new IllegalArgumentException("Type mismatch for path: " + path
                    + ". Expected: " + type.getSimpleName()
                    + ", but found: " + value.getClass().getSimpleName());
        }

        return type.cast(value);
    }

    public <T> T get(String path, Class<T> type, boolean strict) {
        return get(path, type, null, strict);
    }

    public <T> T get(String path, Class<T> type, T defaultValue) {
        return get(path, type, defaultValue, false);
    }

    public void set(String path, Object value) {
        plugin.getConfig().set(path, value);
        plugin.saveConfig();
    }

    public boolean contains(String path) {
        return plugin.getConfig().contains(path);
    }

    public <T> T getDefault(String path, Class<T> type) {
        if (!plugin.getConfig().contains(path)) {
            return get(path, type, null, false);
        }
        return get(path, type, false);
    }

    public <T> List<T> getList(String path, Class<T> type) {
        List<?> list = plugin.getConfig().getList(path);
        if (list == null) {
            return null;
        }

        List<T> typedList = new ArrayList<>();
        for (Object item : list) {
            if (type.isInstance(item)) {
                typedList.add(type.cast(item));
            } else {
                throw new IllegalArgumentException("List contains elements that are not of the expected type: " + type.getSimpleName());
            }
        }
        return typedList;
    }

    public void clear() {
        plugin.getConfig().getKeys(false).forEach(key -> plugin.getConfig().set(key, null));
        plugin.saveConfig();
    }

    public void reload() {
        plugin.reloadConfig();
    }

    public <T> T getOrDefault(String path, Class<T> type, T defaultValue) {
        return get(path, type, defaultValue, false);
    }

    public void validate(String path, String expectedValue) {
        String value = get(path, String.class, false);
        if (value == null || !value.equals(expectedValue)) {
            throw new IllegalArgumentException("Invalid value for path: " + path);
        }
    }

    public Set<String> getKeys() {
        return plugin.getConfig().getKeys(false);
    }

    public void save() {
        plugin.saveConfig();
    }

    public <T> T getNested(String path, Class<T> type, String... nestedPaths) {
        Object value = plugin.getConfig().get(path);
        for (String nestedPath : nestedPaths) {
            if (value instanceof org.bukkit.configuration.ConfigurationSection) {
                value = ((org.bukkit.configuration.ConfigurationSection) value).get(nestedPath);
            } else {
                throw new IllegalArgumentException("Path " + nestedPath + " not found in the section " + path);
            }
        }

        if (value == null) {
            return null;
        }

        if (!type.isInstance(value)) {
            throw new IllegalArgumentException("Type mismatch for path: " + path + ". Expected: " + type.getSimpleName());
        }

        return type.cast(value);
    }

    public void setDefault(String path, Object defaultValue) {
        if (!plugin.getConfig().contains(path)) {
            plugin.getConfig().set(path, defaultValue);
            plugin.saveConfig();
        }
    }

    public void remove(String path) {
        plugin.getConfig().set(path, null);
        plugin.saveConfig();
    }

    public org.bukkit.configuration.ConfigurationSection getConfigSection(String path) {
        return plugin.getConfig().getConfigurationSection(path);
    }

    public boolean isValidPath(String path) {
        return plugin.getConfig().isSet(path);
    }

    public boolean isList(String path) {
        return plugin.getConfig().get(path) instanceof List;
    }

    public boolean isConfigurationSection(String path) {
        return plugin.getConfig().get(path) instanceof org.bukkit.configuration.ConfigurationSection;
    }

}
