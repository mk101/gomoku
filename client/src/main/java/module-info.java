module kolesov.maxim.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires lombok;
    requires org.slf4j;
    requires ch.qos.logback.core;

    opens kolesov.maxim.client to javafx.fxml;
    exports kolesov.maxim.client;
}