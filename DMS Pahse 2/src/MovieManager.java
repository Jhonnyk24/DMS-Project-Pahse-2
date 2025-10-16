import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the in-memory movie list and persistence to a CSV file.
 * CSV file must use this header/order:
 * title,year,director,rating,runtimeMinutes,votes,watched
 */
public class MovieManager {

    private final String filePath;  // must be initialized in constructor
    private List<Movie> movies = new ArrayList<>();

    // Simple report returned by uploadCSV so the CLI can display results
    public static class UploadReport {
        public final int inserted;
        public final List<String> errors;

        public UploadReport(int inserted, List<String> errors) {
            this.inserted = inserted;
            this.errors = errors;
        }
    }

    // Constructor requires file path
    public MovieManager(String filePath) {
        this.filePath = filePath;
        loadMovies();
    }

    // ---------------- CSV Persistence ----------------

    public void loadMovies() {
        movies.clear();
        File f = new File(filePath);
        if (!f.exists()) return; // no file yet

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                // skip header
                if (lineNum == 1 && line.toLowerCase().contains("title")) continue;

                try {
                    movies.add(Movie.fromCSV(line));
                } catch (IllegalArgumentException ex) {
                    System.out.println("Skipping invalid CSV line " + lineNum + ": " + ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file '" + filePath + "': " + e.getMessage());
        }
    }

    public void saveMovies() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("title,year,director,rating,runtimeMinutes,votes,watched");
            bw.newLine();
            for (Movie m : movies) {
                bw.write(m.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to file '" + filePath + "': " + e.getMessage());
        }
    }

    // ---------------- Movie Operations ----------------

    public List<Movie> getAll() {
        return new ArrayList<>(movies);
    }

    public void addMovie(Movie m) {
        movies.add(m);
        saveMovies();
    }

    public boolean removeMovie(int index) {
        if (index >= 0 && index < movies.size()) {
            movies.remove(index);
            saveMovies();
            return true;
        } else return false;
    }

    public UploadReport uploadCSV(String csvPath) {
        int inserted = 0;
        List<String> errors = new ArrayList<>();
        File f = new File(csvPath);
        if (!f.exists()) {
            errors.add("File not found: " + csvPath);
            return new UploadReport(0, errors);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;
                if (lineNum == 1 && line.toLowerCase().contains("title")) continue;

                try {
                    Movie m = Movie.fromCSV(line);
                    movies.add(m);
                    inserted++;
                } catch (IllegalArgumentException ex) {
                    errors.add("Line " + lineNum + ": " + ex.getMessage());
                }
            }
            if (inserted > 0) saveMovies();
        } catch (IOException e) {
            errors.add("I/O error while reading the file: " + e.getMessage());
        }

        return new UploadReport(inserted, errors);
    }
}
