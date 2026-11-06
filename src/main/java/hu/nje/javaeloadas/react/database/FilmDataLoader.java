
package hu.nje.javaeloadas.react.database;

import hu.nje.javaeloadas.react.model.Film;
import hu.nje.javaeloadas.react.repository.FilmRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class FilmDataLoader implements CommandLineRunner {

    private final FilmRepository filmRepository;

    public FilmDataLoader(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (filmRepository.count() > 0) {
            return;
        }

        ClassPathResource resource =
                new ClassPathResource("database/film.txt");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        resource.getInputStream(),
                        StandardCharsets.UTF_8
                ))) {

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split("\t", -1);

                if (data.length != 4) {
                    throw new IllegalArgumentException(
                            "Hibás filmadat: " + line
                    );
                }

                Film film = new Film(
                        data[1].trim(),
                        Integer.parseInt(data[2].trim()),
                        Integer.parseInt(data[3].trim())
                );

                filmRepository.save(film);
            }
        }
    }
}
