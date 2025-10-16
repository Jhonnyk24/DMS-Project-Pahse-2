import org.junit.jupiter.api.*;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieManagerTest {

    private MovieManager manager;
    private static final String TEST_FILE = "test_movies.csv";

    @BeforeEach
    void setUp() {
        // Delete test file if exists
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();

        manager = new MovieManager(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        // Clean up test file after each test
        File f = new File(TEST_FILE);
        if (f.exists()) f.delete();
    }

    // ------------------- 1️⃣ File open test -------------------
    @Test
    void testLoadMovies_FileOpensSuccessfully() throws IOException {
        // Write one valid CSV line
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TEST_FILE))) {
            bw.write("title,year,director,rating,runtimeMinutes,votes,watched\n");
            bw.write("Inception,2010,Christopher Nolan,8.8,148,2000000,true\n");
        }

        manager.loadMovies();
        List<Movie> all = manager.getAll();
        assertEquals(1, all.size());
        assertEquals("Inception", all.get(0).getTitle());
    }

    // ------------------- 2️⃣ Add movie test -------------------
    @Test
    void testAddMovie() {
        Movie m = new Movie("The Shining", 1980, "Stanley Kubrick", 8.4, 146, 500000, true);
        manager.addMovie(m);

        List<Movie> all = manager.getAll();
        assertEquals(1, all.size());
        assertEquals("The Shining", all.get(0).getTitle());
    }

    // ------------------- 3️⃣ Remove movie test -------------------
    @Test
    void testRemoveMovie() {
        Movie m = new Movie("It", 2017, "Andy Muschietti", 7.3, 135, 350000, true);
        manager.addMovie(m);

        boolean removed = manager.removeMovie(0);
        assertTrue(removed);
        assertEquals(0, manager.getAll().size());
    }

    // ------------------- 4️⃣ Upload CSV test -------------------
    @Test
    void testUploadCSV() throws IOException {
        String csvContent = "title,year,director,rating,runtimeMinutes,votes,watched\n"
                + "Halloween,1978,John Carpenter,7.8,91,200000,true\n";

        File tempCsv = new File("upload_test.csv");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(tempCsv))) {
            bw.write(csvContent);
        }

        MovieManager.UploadReport report = manager.uploadCSV("upload_test.csv");
        assertEquals(1, report.inserted);
        assertTrue(report.errors.isEmpty());

        tempCsv.delete();
    }

    // ------------------- 5️⃣ Edit movie test (simulated) -------------------
    @Test
    void testEditMovie() {
        Movie original = new Movie("It", 2017, "Andy Muschietti", 7.3, 135, 350000, true);
        manager.addMovie(original);

        Movie updated = new Movie("It Chapter Two", 2019, "Andy Muschietti", 6.8, 165, 400000, true);

        // Simulate edit: remove old, add updated
        manager.removeMovie(0);
        manager.addMovie(updated);

        List<Movie> all = manager.getAll();
        assertEquals(1, all.size());
        Movie m = all.get(0);
        assertEquals("It Chapter Two", m.getTitle());
        assertEquals(2019, m.getYear());
        assertEquals(6.8, m.getRating());
    }

    // ------------------- 6️⃣ Custom action test: scariness -------------------
    @Test
    void testMovieScariness() {
        Movie m = new Movie("Scary Movie", 2000, "Director", 8.0, 130, 500000, false);
        double score = m.getScariness();
        assertTrue(score > 0 && score <= 10, "Scariness score should be between 0 and 10");
    }
}
