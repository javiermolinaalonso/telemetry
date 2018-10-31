package com.seven.segment;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;

public class SevenSegmentManager {

    private final GpioPinDigitalOutput[] positions;
    private final GpioPinDigitalOutput[] segments;

    private final Print print;
    private final Thread printThread;
    private static final int[][] matrix = new int[][]{
            {1, 1, 0, 1, 0, 1, 1, 1},
            {0, 0, 0, 1, 0, 1, 0, 0},
            {1, 1, 0, 0, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 1, 1, 0},
            {0, 0, 0, 1, 1, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 1},
            {1, 1, 0, 1, 1, 0, 1, 1},
            {0, 0, 0, 1, 0, 1, 1, 0},
            {1, 1, 0, 1, 1, 1, 1, 1},
            {0, 0, 0, 1, 1, 1, 1, 1}
    };

    public SevenSegmentManager(GpioController gpio) {
        positions = new GpioPinDigitalOutput[]{
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25),
        };

        segments = new GpioPinDigitalOutput[]{
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_07),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29),
        };
        print = new Print();
        printThread = new Thread(print);
        printThread.start();
    }

    public void print(int value) {
        print.update(value);
    }

    public void shutdown() throws InterruptedException {
        print.stop();
        printThread.join(1000);
    }

    private final class Print implements Runnable {

        int startingPoint = 4;
        String valueToPrint;
        private boolean printing = true;

        void update(int value) {
            valueToPrint = String.valueOf(value);
            startingPoint = 4 - valueToPrint.length();
        }

        void stop () {
            printing = false;
        }

        @Override
        public void run() {
            while(printing) {
                for (int position = startingPoint; position < 4; position++) {
                    final int strPosition = position - startingPoint;
                    int valueToPrint = new Integer(this.valueToPrint.substring(strPosition, strPosition + 1));
                    print(valueToPrint, position + 1);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {

                    }
                }
            }
            clearDisplay();
        }

        private void print(int number, int position) {
            printPosition(position);
            printNumber(number);
        }

        private void printNumber(int number) {
            for (int i = 0; i < 8; i++) {
                if (matrix[number][i] == 0) {
                    segments[i].low();
                } else {
                    segments[i].high();
                }
            }
        }

        private void printPosition(int position) {
            clearDisplay();
            positions[position-1].low();
        }

        private void clearDisplay() {
            for (GpioPinDigitalOutput position : positions) {
                position.high();
            }
        }
    }
}
