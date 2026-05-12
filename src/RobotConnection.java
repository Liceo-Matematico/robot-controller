import com.fazecast.jSerialComm.SerialPort;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class RobotConnection {

    private SerialPort port;
    private OutputStream out;
    private InputStream in;

    private BufferedReader reader;

    public RobotConnection() throws Exception {

        // --------------------------------
        // PORTE DISPONIBILI
        // --------------------------------

        SerialPort[] ports = SerialPort.getCommPorts();

        System.out.println(
                "PORTE DISPONIBILI:");

        for (int i = 0; i < ports.length; i++) {

            System.out.println(
                    i + " -> "
                            + ports[i].getSystemPortName());
        }

        // --------------------------------
        // SCELTA PORTA
        // --------------------------------

        int scelta = Integer.parseInt(
                javax.swing.JOptionPane
                        .showInputDialog(
                                "Indice porta:"));

        port = ports[scelta];

        // --------------------------------
        // CONFIG SERIAL
        // --------------------------------

        port.setBaudRate(9600);

        port.setComPortTimeouts(
                SerialPort.TIMEOUT_READ_SEMI_BLOCKING,
                0,
                0);

        // --------------------------------
        // APERTURA PORTA
        // --------------------------------

        if (!port.openPort()) {

            throw new RuntimeException(
                    "Errore apertura porta");
        }

        // --------------------------------
        // STREAM
        // --------------------------------

        out = port.getOutputStream();

        in = port.getInputStream();

        // --------------------------------
        // READER LINEE
        // --------------------------------

        reader = new BufferedReader(
                new InputStreamReader(in));

        System.out.println(
                "CONNESSO!");
    }

    // ==================================
    // INVIO VELOCITÀ MOTORI
    // ==================================

    public void sendMotorState(
            MotorState state) throws Exception {

        String comando = state.getVelSX()
                + ","
                + state.getVelDX()
                + "\n";

        out.write(comando.getBytes());

        out.flush();
    }

    // ==================================
    // STOP ROBOT
    // ==================================

    public void stopRobot()
            throws Exception {

        out.write("0,0\n".getBytes());

        out.flush();
    }

    // ==================================
    // LETTURA SCAN COMPLETO
    // ==================================

    public String readScan()
            throws Exception {

        return reader.readLine();
    }

    public void close() {

        port.closePort();
    }
}