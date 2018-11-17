package com.phoboss.io.gps;

import com.pi4j.io.serial.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class GPS {
    /**
     * The position accuracy without any
     */
    public static final float UNAIDED_POSITION_ACCURACY = 3.0f;

    private final Serial serial;
    private final GPSDataRetriever dataRetriever = new GPSDataRetriever();
    private final Thread dataRetrieverThread;
    private final List<GPSListener> listeners = new CopyOnWriteArrayList<>();
    private final GpsConsumer gpsConsumer = new GpsConsumer();

    /**
     * Creates a new GPS instance.
     */
    public GPS() {
        serial = SerialFactory.createInstance();
        dataRetrieverThread = new Thread(dataRetriever, "GPS Data Retriever");
        initialize();
    }

    /**
     * Adds a new listener to listen for GPS data.
     *
     * @param gpsListener the new listener to add.
     * @see GPSListener
     */
    public void addListener(GPSListener gpsListener) {
        listeners.add(gpsListener);
    }

    /**
     * Removes a previously added listener.
     *
     * @param gpsListener the listener to remove.
     * @see GPSListener
     */
    public void removeListener(GPSListener gpsListener) {
        listeners.remove(gpsListener);
    }

    /**
     * Shuts down the GPS listener. After the shutdown is completed, no more
     * events will be sent to listeners.
     */
    public void shutdown() {
        dataRetriever.isRunning = false;
        try {
            dataRetrieverThread.join();
        } catch (InterruptedException e) {
        }
    }

    private void initialize() {
        try {
            SerialConfig config = new SerialConfig();
            config.device("/dev/ttyUSB0")
                    .baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);
            serial.open(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataRetrieverThread.start();
    }

    private void notifyListeners(GPSEvent event) {
        for (GPSListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    private final class GPSDataRetriever implements Runnable {
        private static final int DEFAULT_READ_INTERVAL = 550;
        volatile boolean isRunning = true;
        StringBuilder builder = new StringBuilder();

        @Override
        public void run() {
            try {
                while (isRunning) {
                    String str = readNext();
                    builder.setLength(0);
                    StringTokenizer st = new StringTokenizer(str, "\n", true);
                    while (st.hasMoreElements()) {
                        String dataLine = st.nextToken();
                        while ("\n".equals(dataLine) && st.hasMoreElements()) {
                            dataLine = st.nextToken();
                        }
                        if (st.hasMoreElements()) {
                            consume(dataLine);
                        } else if (!"\n".equals(dataLine)) {
                            builder.append(dataLine);
                        }
                    }
                    sleep(DEFAULT_READ_INTERVAL);
                }

                serial.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void consume(String dataLine) {
            gpsConsumer.consume(dataLine).ifPresent(GPS.this::notifyListeners);
        }

        private void sleep(int millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
            }
        }

        private String readNext() throws IOException {
            int available = serial.available();
            if (available > 0) {
                return new String(serial.read(available));
            } else {
                return "";
            }
        }
    }
}
