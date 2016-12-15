/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp.comm;

/**
 *
 * @author fazeem
 */
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import testapp.hibernate.dao.DAO;
import testapp.hibernate.pojo.DeviceInput;

/**
 * This version of the TwoWaySerialComm example makes use of the
 * SerialPortEventListener to avoid polling.
 *
 */
public class SerialCommunicator {

    List<SerialPort> portList = new ArrayList<>();
    List<String> portNameList = new ArrayList<>();
    Logger log = Logger.getLogger(this.getClass().getName());
    public static BlockingQueue<JSONObject> reqBlockingQueue = new ArrayBlockingQueue<>(10);
    public static boolean disconnect = false;

    public SerialCommunicator() {
        super();
    }

    public void fetchandConnectSerialPorts() throws Exception {
        this.listPorts();
        this.connect();

    }

    List<String> listPorts() {
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                log.info("Adding Port" + portIdentifier.getName());
                portNameList.add(portIdentifier.getName());
            }
        }
        return portNameList;
    }

    public void disconnect() {
        try {
            SerialCommunicator.disconnect = true;
            SerialCommunicator.reqBlockingQueue.clear();
            for (SerialPort port : portList) {
                System.out.println("Disconnecting Port" + port.getName());
                port.removeEventListener();
                port.getInputStream().close();
                port.getOutputStream().close();
                port.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void connect() throws Exception {

        for (String portName : portNameList) {
            this.connect(portName);
        }

    }

    public void connect(String portName) throws Exception {
        log.info("Conncting Port" + portName);
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open("Conn" + portList.size(), 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                System.out.println("Boud Rate----" + serialPort.getBaudRate());

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                (new Thread(new SerialWriter(out))).start();

                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);
                portList.add(serialPort);
                log.info("Connected to port" + portName);

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /**
     * Handles the input coming from the serial port. A new line character is
     * treated as the end of a block in this example.
     */
    public static class SerialReader implements SerialPortEventListener {

        private InputStream in;
        private byte[] buffer = new byte[1024];

        public SerialReader(InputStream in) {
            this.in = in;
        }

        public void serialEvent(SerialPortEvent arg0) {
            int data;
            Logger log = Logger.getLogger(this.getClass().getName());

            try {
                int len = 0;
                while ((data = in.read()) > -1) {
                    if (data == '\n') {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                String response = new String(buffer, 0, len);
                log.info(response);
                try {
                    JSONObject outJSON = new JSONObject(response);
                    DeviceInput deviceInput = new DeviceInput();
                    deviceInput.setT1(outJSON.getDouble("T1"));
                    deviceInput.setT2(outJSON.getDouble("T2"));
                    deviceInput.setT3(outJSON.getDouble("T3"));
                    deviceInput.setSL(outJSON.getDouble("SL") / 10);
                    deviceInput.setAM(outJSON.getDouble("AM") / 10);
                    deviceInput.setPH(outJSON.getDouble("PH") / 10);
                    deviceInput.setNI(outJSON.getDouble("NI") / 10);
                    deviceInput.setCreated(new Date());
                    DAO.create(deviceInput);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    log.log(Level.INFO, "{0}is not a json", response);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

    }

    /**
     *
     */
    public static class SerialWriter implements Runnable {

        OutputStream out;

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        @Override
        public void run() {
            try {
                while (!disconnect) {
                    String request = reqBlockingQueue.take().toString();
                    System.out.println("Sending Request "+request);
                    this.out.write(request.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

//    public static void main(String[] args) {
//        try {
//            (new SerialCommunicator()).connect("COM3");
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
