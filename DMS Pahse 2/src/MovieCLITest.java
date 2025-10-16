import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovieCLITest {

    private MovieManager manager;
    private MovieCLI cli;

    @BeforeEach
    void setUp() {
        // Dummy MovieManager: in-memory only, no file I/O
        manager = new MovieManager("dummy.csv") {
            @Override
            public void saveMovies() { }
            @Override
            public void loadMovies() { }
        };

        // CLI with overrides for automated testing
        cli = new MovieCLI(manager) {
            @Override
            public String promptNonEmptyString(String prompt) {
                return switch (prompt) {
                    case "Title: " -> "TestMovie";
                    case "Director: " -> "TestDirector";
                    default -> "Unknown";
                };
            }

            @Override
            public int promptInt(String prompt, int min, int max) {
                return switch (prompt) {
                    case "Year (e.g. 2017): " -> 2021;
                    case "Runtime minutes (>0): " -> 120;
                    case "Votes (0 or greater): " -> 500;
                    default -> min;
                };
            }

            @Override
            public double promptDouble(String prompt, double min, double max) {
                return 8.0;
            }

            @Override
            public boolean promptBoolean(String prompt) {
                return true;
            }

            @Override
            public String promptOptionalString(String prompt, String oldValue) {
                return "EditedMovie";
            }

            @Override
            public int promptOptionalInt(String prompt, int min, int max, int oldValue) {
                return oldValue + 1;
            }

            @Override
            public double promptOptionalDouble(String prompt, double min, double max, double oldValue) {
                return oldValue + 1.0;
            }

            @Override
            public boolean promptOptionalBoolean(String prompt, boolean oldValue) {
                return !oldValue;
            }
        };
    }

    @Test
    void testAddMovieCLI() {
        cli.addMovie();
        List<Movie> movies = manager.getAll();
        assertEquals(1, movies.size());

        Movie m = movies.get(0);
        assertEquals("TestMovie", m.getTitle());
        assertEquals(2021, m.getYear());
        assertEquals("TestDirector", m.getDirector());
        assertEquals(8.0, m.getRating());
        assertEquals(120, m.getRuntimeMinutes());
        assertEquals(500, m.getVotes());
        assertTrue(m.isWatched());
    }

    @Test
    void testEditMovieCLI() {
        manager.addMovie(new Movie("OldMovie", 2000, "OldDirector", 5.0, 100, 50, false));
        cli.editMovie();

        List<Movie> movies = manager.getAll();
        assertEquals(1, movies.size());
        Movie m = movies.get(0);
        assertEquals("EditedMovie", m.getTitle());
        assertEquals(2001, m.getYear());
        assertEquals("EditedMovie", m.getDirector());
        assertEquals(6.0, m.getRating());
        assertEquals(101, m.getRuntimeMinutes());
        assertEquals(51, m.getVotes());
        assertTrue(m.isWatched()); // toggled from false
    }

    @Test
    void testDeleteMovieCLI() {
        manager.addMovie(new Movie("DeleteMe", 2010, "Director", 7.0, 90, 200, true));

        // Override promptInt to select the first movie
        cli = new MovieCLI(manager) {
            @Override
            public int promptInt(String prompt, int min, int max) {
                return 1;
            }
        };

        cli.deleteMovie();
        assertEquals(0, manager.getAll().size());
    }
}
