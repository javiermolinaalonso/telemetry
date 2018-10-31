package com.gps.publisher;

import com.gps.GPSEvent;
import com.pi4j.io.serial.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@Component
class GpsSerialReader {

    private final Serial serial;
    private final SerialConfig serialConfig;

    GpsSerialReader() {
        this.serial = SerialFactory.createInstance();
        this.serialConfig = new SerialConfig()
                .device(Serial.FIRST_USB_COM_PORT)
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
    }

    @PostConstruct
    public void initialize() throws IOException {
        serial.open(serialConfig);
    }

    @PreDestroy
    public void tearDown() throws IOException {
        serial.close();
    }

    GPSEvent read() {

        return
                null;
    }

}
