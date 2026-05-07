import com.fazecast.jSerialComm.SerialPort;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class ControlloRobotRealtime {

    public static void main(String[] args) throws Exception {

        SerialPort[] ports = SerialPort.getCommPorts();

        System.out.println("Porte seriali rilevate:");

        for (SerialPort p : ports) {
            System.out.println("- " + p.getSystemPortName());
        }

        SerialPort port = SerialPort.getCommPort("COM3");
        port.setBaudRate(9600);

        if (!port.openPort()) {
            System.out.println("Errore apertura porta");
            return;
        }

        OutputStream out = port.getOutputStream();
        InputStream in = port.getInputStream();

        @SuppressWarnings("resource")
        BufferedWriter file = new BufferedWriter(new FileWriter("dataset.csv"));

        JFrame frame = new JFrame("Controllo Robot");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Usa W A S D", SwingConstants.CENTER);
        frame.add(label);

        frame.addKeyListener(new KeyAdapter() {

            boolean[] keys = new boolean[256];

            @Override
            public void keyPressed(KeyEvent e) {
                keys[e.getKeyCode()] = true;
                aggiorna();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keys[e.getKeyCode()] = false;
                aggiorna();
            }

            void aggiorna() {
                try {
                    boolean w = keys[KeyEvent.VK_W];
                    boolean a = keys[KeyEvent.VK_A];
                    boolean s = keys[KeyEvent.VK_S];
                    boolean d = keys[KeyEvent.VK_D];

                    char comando = 'S';

                    if (w && a)
                        comando = 'Q';
                    else if (w && d)
                        comando = 'E';
                    else if (w)
                        comando = 'W';
                    else if (a)
                        comando = 'A';
                    else if (d)
                        comando = 'D';
                    else if (s)
                        comando = 'S';

                    out.write(comando);
                    out.flush();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.setVisible(true);

        new Thread(() -> {
            try {
                while (true) {
                    if (in.available() > 0) {
                        String linea = "";

                        while (in.available() > 0) {
                            linea += (char) in.read();
                        }

                        System.out.print(linea);
                        file.write(linea);
                        file.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
