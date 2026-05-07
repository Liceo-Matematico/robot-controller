public class MotorState {

    private double velSX;
    private double velDX;

    private final double STEP = 0.1;

    public void incSX() {
        velSX += STEP;
        clamp();
    }

    public void decSX() {
        velSX -= STEP;
        clamp();
    }

    public void incDX() {
        velDX += STEP;
        clamp();
    }

    public void decDX() {
        velDX -= STEP;
        clamp();
    }

    private void clamp() {

        velSX = Math.max(-1, Math.min(1, velSX));
        velDX = Math.max(-1, Math.min(1, velDX));
    }

    public double getVelSX() {
        return velSX;
    }

    public double getVelDX() {
        return velDX;
    }

    @Override
    public String toString() {

        return """
                -------------------
                VEL SX = """ + velSX + """

                VEL DX = """ + velDX + """
                -------------------
                """;
    }
}