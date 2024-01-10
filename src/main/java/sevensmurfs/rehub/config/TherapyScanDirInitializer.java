package sevensmurfs.rehub.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class TherapyScanDirInitializer implements ApplicationRunner {

    @Value("${therapy.scan.save.dir}")
    private String therapyScansDir;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path directoryPath = Paths.get(therapyScansDir);

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
            log.info("Therapy scan directory created.");
        } else {
            log.info("Therapy scan directory already exists {}", therapyScansDir);
        }
    }
}
