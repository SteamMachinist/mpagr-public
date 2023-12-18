package steammachinist.mpagradmin;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class EngineControlService {
    public void startEngine() {
        //executeShellCommand("docker start mpagr-engine-container");
        executeShellCommand("docker-compose up");
    }

    public void stopEngine() {
        executeShellCommand("docker stop mpagr-engine-container");
    }

    private void executeShellCommand(String command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("sh", "-c", command);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Docker Compose command exited with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
