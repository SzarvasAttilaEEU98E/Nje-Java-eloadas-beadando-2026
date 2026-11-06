
package hu.nje.javaeloadas.react.service;

import hu.nje.javaeloadas.react.model.Film;
import hu.nje.javaeloadas.react.repository.FilmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Transactional(readOnly = true)
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Film getFilmById(Long id) {
        return filmRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Nem található film ezzel az azonosítóval: " + id
                        )
                );
    }

    @Transactional
    public Film createFilm(Film film) {
        film.setId(null);
        return filmRepository.save(film);
    }

    @Transactional
    public Film updateFilm(Long id, Film updatedFilm) {
        Film film = getFilmById(id);

        film.setCim(updatedFilm.getCim());
        film.setEv(updatedFilm.getEv());
        film.setHossz(updatedFilm.getHossz());

        return filmRepository.save(film);
    }

    @Transactional
    public void deleteFilm(Long id) {
        Film film = getFilmById(id);
        filmRepository.delete(film);
    }
}
