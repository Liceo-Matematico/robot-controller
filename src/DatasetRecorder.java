import java.io.BufferedWriter;
import java.io.FileWriter;

public class DatasetRecorder {

    private BufferedWriter file;

    public DatasetRecorder(String nomeFile)
            throws Exception {

        file = new BufferedWriter(
                new FileWriter(nomeFile, true));
    }

    public void save(TrainingSample sample)
            throws Exception {

        file.write(sample.toString());

        file.newLine();

        file.flush();
    }

    public void close()
            throws Exception {

        file.close();
    }
}