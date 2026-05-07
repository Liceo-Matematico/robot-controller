import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TrainingController {

    private MotorState motors;

    private RobotConnection robot;

    private DatasetRecorder recorder;

    private boolean running = false;

    private long startTime;

    private String currentScan = "";

    public TrainingController()
            throws Exception {

        motors = new MotorState();

        robot = new RobotConnection();

        recorder = new DatasetRecorder(
                "dataset.csv");

        createGUI();

        startScanThread();
    }

    private void createGUI() {

        JFrame frame = new JFrame("Training Robot AI");

        frame.setSize(500, 300);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        JTextArea area = new JTextArea();

        area.setEditable(false);

        area.setText("""
                CONTROLLI

                W = + velocità SX
                A = - velocità SX

                I = + velocità DX
                L = - velocità DX

                SPACE = START / STOP

                ENTER = termina
                """);

        frame.add(area);

        printMotors();

        frame.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyPressed(
                            KeyEvent e) {

                        try {

                            manageKey(e);

                        } catch (Exception ex) {

                            ex.printStackTrace();
                        }
                    }
                });

        frame.setVisible(true);
    }

    private void manageKey(KeyEvent e)
            throws Exception {

        int key = e.getKeyCode();

        // -------------------------
        // ENTER = TERMINA
        // -------------------------
        if (key == KeyEvent.VK_ENTER) {

            robot.stopRobot();

            recorder.close();

            System.out.println(
                    "\nSESSIONE TERMINATA");

            System.exit(0);
        }

        // -------------------------
        // ROBOT IN MOVIMENTO
        // -------------------------
        if (running) {

            if (key == KeyEvent.VK_SPACE) {

                stopExecution();
            }

            return;
        }

        // -------------------------
        // MODIFICA MOTORI
        // -------------------------
        if (key == KeyEvent.VK_W) {
            motors.incSX();
        }

        if (key == KeyEvent.VK_A) {
            motors.decSX();
        }

        if (key == KeyEvent.VK_I) {
            motors.incDX();
        }

        if (key == KeyEvent.VK_L) {
            motors.decDX();
        }

        printMotors();

        // -------------------------
        // START
        // -------------------------
        if (key == KeyEvent.VK_SPACE) {

            startExecution();
        }
    }

    private void startExecution()
            throws Exception {

        robot.sendMotorState(motors);

        running = true;

        startTime = System.currentTimeMillis();

        System.out.println(
                "\nROBOT AVVIATO");
    }

    private void stopExecution()
            throws Exception {

        robot.stopRobot();

        running = false;

        long durata = System.currentTimeMillis()
                - startTime;

        TrainingSample sample = new TrainingSample(
                currentScan,
                motors.getVelSX(),
                motors.getVelDX(),
                durata);

        recorder.save(sample);

        System.out.println(
                "\nESEMPIO SALVATO:");

        System.out.println(sample);
    }

    private void printMotors() {

        System.out.println(motors);
    }

    private void startScanThread() {

        new Thread(() -> {

            try {

                while (true) {

                    String scan = robot.readScan();

                    if (scan != null
                            && !scan.isEmpty()) {

                        currentScan = scan;

                        System.out.println(
                                "\nSCAN:");

                        System.out.println(
                                currentScan);
                    }

                    Thread.sleep(10);
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }).start();
    }

    public static void main(String[] args)
            throws Exception {

        new TrainingController();
    }
}