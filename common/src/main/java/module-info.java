open module kolesov.maxim.common {
    requires lombok;
    requires org.slf4j;
    requires ch.qos.logback.core;

    exports kolesov.maxim.common.context;
    exports kolesov.maxim.common.config;
    exports kolesov.maxim.common.dto;
}