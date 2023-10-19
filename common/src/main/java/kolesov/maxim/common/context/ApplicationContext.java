package kolesov.maxim.common.context;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public abstract class ApplicationContext {

    private static final String PROPERTIES_FILE_NAME = "application.properties";

    private static ApplicationContext instance;

    private final Map<String, Object> components;

    private final Properties properties;

    protected ApplicationContext() {
        this.components = new HashMap<>();
        this.properties = new Properties();

        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME));
        } catch (IOException e) {
            log.error("Can't load properties", e);
            System.exit(-1);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(String name) throws IllegalArgumentException {
        if (!components.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Component %s not found", name));
        }

        return (T) components.get(name);
    }

    public <T> void registerComponent(String name, T component) throws IllegalArgumentException {
        if (components.containsKey(name)) {
            throw new IllegalArgumentException(String.format("Component %s is already register", name));
        }
        log.debug("Add component {}", component);
        components.put(name, component);
    }

    public String getProperty(String name) throws IllegalArgumentException {
        String property = properties.getProperty(name);
        if (property == null) {
            throw new IllegalArgumentException(String.format("Property %s not found", name));
        }

        return property;
    }

    protected abstract void initializeContext();

    public static void initialize(Class<? extends ApplicationContext> context) {
        log.debug("Start initialize context");
        try {
            instance = context.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to initialize context", e);
            System.exit(-1);
        }
        log.debug("Context initialization complete");
    }

    public static ApplicationContext get() {
        if (instance == null) {
            throw new NullPointerException("Application context is null");
        }

        return instance;
    }

}
