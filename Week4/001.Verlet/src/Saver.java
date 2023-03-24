import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Saver {
    private String path;
    public void setPath(String path){
        this.path = path;
    }

    public ArrayList load() {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    Files.newInputStream(Paths.get(path)));
            ArrayList<Particle[]> output = (ArrayList<Particle[]>) ois.readObject();
            ois.close();
            return output;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(ArrayList<Particle[]> object) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    Files.newOutputStream(Paths.get(path))
            );
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
