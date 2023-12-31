module kolesov.maxim.client {
    requires kolesov.maxim.common;

    requires javafx.controls;
    requires javafx.fxml;

    requires lombok;
    requires org.slf4j;
    requires ch.qos.logback.core;

    requires com.fasterxml.jackson.databind;

    opens kolesov.maxim.client to javafx.fxml;
    exports kolesov.maxim.client;
    exports kolesov.maxim.client.controller;
    opens kolesov.maxim.client.controller to javafx.fxml;

    exports kolesov.maxim.client.context;
}