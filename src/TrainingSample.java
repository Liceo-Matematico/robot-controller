public class TrainingSample {

    private String scan;
    private double velSX;
    private double velDX;
    private long durata;

    public TrainingSample(
            String scan,
            double velSX,
            double velDX,
            long durata) {

        this.scan = scan;
        this.velSX = velSX;
        this.velDX = velDX;
        this.durata = durata;
    }

    @Override
    public String toString() {

        return scan + ","
                + velSX + ","
                + velDX + ","
                + durata;
    }
}