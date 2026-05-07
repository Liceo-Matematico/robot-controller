import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;

public class RobotConnection {

    private SerialPort port;
    private OutputStream out;
    private InputStream in;

    public RobotConnection() throws Exception {

        SerialPort[] ports = SerialPort.getCommPorts();

        System.out.println("PORTE DISPONIBILI:");

        for (int i = 0; i < ports.length; i++) {

            System.out.println(
                    i + " -> "
                            + ports[i].getSystemPortName());
        }

        int scelta = Integer.parseInt(
                javax.swing.JOptionPane
                        .showInputDialog(
                                "Indice porta:"));

        port = ports[scelta];

        port.setBaudRate(9600);

        if (!port.openPort()) {

            throw new RuntimeException(
                    "Errore apertura porta");
        }

        out = port.getOutputStream();
        in = port.getInputStream();

        System.out.println("CONNESSO!");
    }

    public void sendMotorState(
            MotorState state) throws Exception {

        String comando = state.getVelSX()
                + ","
                + state.getVelDX()
                + "\n";

        out.write(comando.getBytes());

        out.flush();
    }

    public void stopRobot()
            throws Exception {

        out.write("0,0\n".getBytes());

        out.flush();
    }

    public String readScan()
            throws Exception {

        if (in.available() <= 0) {
            return null;
        }

        String line = "";

        while (in.available() > 0) {

            line += (char) in.read();
        }

        return line.trim();
    }
}