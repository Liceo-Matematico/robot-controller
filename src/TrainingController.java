import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TrainingController {

    private MotorState motors;
    private JTextArea area;
    private RobotConnection robot;
    private DatasetRecorder recorder;

    private boolean running = false;
    private boolean active = true;

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

    private void aggiornaGUI() {

        String stato = running
                ? "ROBOT IN MOVIMENTO"
                : "ROBOT FERMO";

        area.setText(
                """
                        CONTROLLI

                        W = + velocità SX
                        A = - velocità SX

                        I = + velocità DX
                        L = - velocità DX

                        SPACE = START / STOP

                        ENTER = termina

                        -------------------

                        VEL SX = """ + motors.getVelSX() + """

                        VEL DX = """ + motors.getVelDX() + """

                        """ + stato);
    }

    private void createGUI() {

        JFrame frame = new JFrame("Training Robot AI");

        frame.setSize(500, 300);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        area = new JTextArea();

        area.setEditable(false);

        frame.add(area);

        aggiornaGUI();

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

        frame.setFocusable(true);

        frame.requestFocus();

        frame.requestFocusInWindow();
    }

    private void manageKey(KeyEvent e)
            throws Exception {

        int key = e.getKeyCode();

        // -------------------------
        // ENTER = TERMINA
        // -------------------------
        if (key == KeyEvent.VK_ENTER) {

            active = false;

            robot.stopRobot();

            robot.close();

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
            aggiornaGUI();
        }

        if (key == KeyEvent.VK_A) {
            motors.decSX();
            aggiornaGUI();
        }

        if (key == KeyEvent.VK_I) {
            motors.incDX();
            aggiornaGUI();
        }

        if (key == KeyEvent.VK_L) {
            motors.decDX();
            aggiornaGUI();
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

                while (active) {

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

                if (active) {

                    System.out.println(
                            "Errore seriale / robot disconnesso");
                }
            }

        }).start();
    }

    public static void main(String[] args)
            throws Exception {

        new TrainingController();
    }
}