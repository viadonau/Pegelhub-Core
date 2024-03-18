package org.example;

import com.stm.pegelhub.lib.PegelHubCommunicator;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

public class ConnectorOptions {
    private String core_ip;
    private String core_port;
    private Duration delay;
    private String propertiesFile;

    private Duration telemetryCycleTime;

    private String inputOnRevpi;


    public ConnectorOptions(String core_ip, String core_port, Duration delay,  String propertiesFile, Duration telemetryCycleTime, String inputOnRevpi)
    {
        this.core_ip = core_ip;
        this.core_port = core_port;
        this.delay = delay;
        this.propertiesFile = propertiesFile;
        this.telemetryCycleTime = telemetryCycleTime;
        this.inputOnRevpi = inputOnRevpi;
    }

    public String getCore_ip() {
        return core_ip;
    }

    public String getPropertiesFile() {
        return propertiesFile;
    }

    public void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    public void setCore_ip(String core_ip) {
        this.core_ip = core_ip;
    }

    public String getCore_port() {
        return core_port;
    }

    public void setCore_port(String core_port) {
        this.core_port = core_port;
    }

    public Duration getDelay() {
        return delay;
    }

    public void setDelay(Duration delay) {
        this.delay = delay;
    }

    public Duration getTelemetryCycleTime() {
        return telemetryCycleTime;
    }

    public void setTelemetryCycleTime(Duration telemetryCycleTime) {
        this.telemetryCycleTime = telemetryCycleTime;
    }

    public String getInputOnRevpi() {
        return inputOnRevpi;
    }

    public void setInputOnRevpi(String inputOnRevpi) {
        this.inputOnRevpi = inputOnRevpi;
    }
}

