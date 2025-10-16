import java.util.List;
import java.util.Scanner;

public class MovieCLI {

    private final MovieManager manager;
    private final Scanner sc = new Scanner(System.in);

    public MovieCLI(MovieManager manager) {
        this.manager = manager;
    }

    // ----------------- Menu -----------------

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== HORROR MOVIES MANAGER (CLI) ===");
            System.out.println("1. Show all movies");
            System.out.println("2. Add a new movie");
            System.out.println("3. Delete a movie");
            System.out.println("4. Upload movies from CSV file");
            System.out.println("5. Edit a movie");
            System.out.println("6. Calculate Scariness of a movie");
            System.out.println("7. Exit");
            System.out.print("Choose an option (1-7): ");

            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> listMovies();
                case "2" -> addMovie();
                case "3" -> deleteMovie();
                case "4" -> uploadCSV();
                case "5" -> editMovie();
                case "6" -> showScariness();
                case "7" -> running = false;
                default -> System.out.println("❌ Invalid option. Please enter a number between 1 and 7.");
            }
        }
        System.out.println("👋 Goodbye!");
    }

    // ----------------- Menu Actions -----------------

    public void listMovies() {
        List<Movie> movies = manager.getAll();
        if (movies.isEmpty()) {
            System.out.println("No movies found.");
            return;
        }

        int idxWidth = 4, titleWidth = 25, directorWidth = 20, yearWidth = 6, ratingWidth = 6,
                runtimeWidth = 8, votesWidth = 8, watchedWidth = 8;

        System.out.println(
                padRight("No.", idxWidth) + " | " +
                        padRight("Title", titleWidth) + " | " +
                        padRight("Director", directorWidth) + " | " +
                        padRight("Year", yearWidth) + " | " +
                        padRight("Rating", ratingWidth) + " | " +
                        padRight("Runtime", runtimeWidth) + " | " +
                        padRight("Votes", votesWidth) + " | " +
                        padRight("Watched", watchedWidth)
        );
        System.out.println("-".repeat(idxWidth + titleWidth + directorWidth + yearWidth + ratingWidth + runtimeWidth + votesWidth + watchedWidth + 21));

        for (int i = 0; i < movies.size(); i++) {
            Movie m = movies.get(i);
            System.out.println(
                    padRight(String.valueOf(i + 1), idxWidth) + " | " +
                            padRight(m.getTitle(), titleWidth) + " | " +
                            padRight(m.getDirector(), directorWidth) + " | " +
                            padRight(String.valueOf(m.getYear()), yearWidth) + " | " +
                            padRight(String.format("%.1f", m.getRating()), ratingWidth) + " | " +
                            padRight(String.valueOf(m.getRuntimeMinutes()), runtimeWidth) + " | " +
                            padRight(String.valueOf(m.getVotes()), votesWidth) + " | " +
                            padRight(m.isWatched() ? "Yes" : "No", watchedWidth)
            );
        }
    }

    public void addMovie() {
        System.out.println("\n--- Add a new movie ---");
        String title = promptNonEmptyString("Title: ");
        int year = promptInt("Year (e.g. 2017): ", 1888, java.time.Year.now().getValue());
        String director = promptNonEmptyString("Director: ");
        double rating = promptDouble("Rating (0.0 - 10.0): ", 0.0, 10.0);
        int runtime = promptInt("Runtime minutes (>0): ", 1, Integer.MAX_VALUE);
        int votes = promptInt("Votes (0 or greater): ", 0, Integer.MAX_VALUE);
        boolean watched = promptBoolean("Watched? (true/false, yes/no, y/n): ");

        Movie m = new Movie(title, year, director, rating, runtime, votes, watched);
        manager.addMovie(m);
        System.out.println("✅ Movie added successfully!");
    }

    public void deleteMovie() {
        List<Movie> movies = manager.getAll();
        if (movies.isEmpty()) {
            System.out.println("No movies to delete.");
            return;
        }
        listMovies();
        int choice = promptInt("Enter the number of the movie to delete: ", 1, movies.size());
        boolean removed = manager.removeMovie(choice - 1);
        if (removed) System.out.println("🗑️ Movie deleted successfully.");
        else System.out.println("⚠️ Could not delete the movie (invalid index).");
    }

    public void editMovie() {
        List<Movie> movies = manager.getAll();
        if (movies.isEmpty()) {
            System.out.println("No movies to edit.");
            return;
        }
        listMovies();
        int choice = promptInt("Enter the number of the movie to edit: ", 1, movies.size());
        Movie selected = movies.get(choice - 1);

        System.out.println("\n--- Editing: " + selected.getTitle() + " ---");

        String title = promptOptionalString("Title [" + selected.getTitle() + "]: ", selected.getTitle());
        int year = promptOptionalInt("Year [" + selected.getYear() + "]: ", 1888, java.time.Year.now().getValue(), selected.getYear());
        String director = promptOptionalString("Director [" + selected.getDirector() + "]: ", selected.getDirector());
        double rating = promptOptionalDouble("Rating [" + selected.getRating() + "]: ", 0.0, 10.0, selected.getRating());
        int runtime = promptOptionalInt("Runtime minutes [" + selected.getRuntimeMinutes() + "]: ", 1, Integer.MAX_VALUE, selected.getRuntimeMinutes());
        int votes = promptOptionalInt("Votes [" + selected.getVotes() + "]: ", 0, Integer.MAX_VALUE, selected.getVotes());
        boolean watched = promptOptionalBoolean("Watched [" + (selected.isWatched() ? "Yes" : "No") + "]: ", selected.isWatched());

        Movie updated = new Movie(title, year, director, rating, runtime, votes, watched);
        manager.removeMovie(choice - 1);
        manager.addMovie(updated);
        System.out.println("✅ Movie updated successfully!");
    }

    public void uploadCSV() {
        System.out.println("\n--- Upload movies from CSV file ---");
        System.out.print("Enter the path to the CSV file: ");
        String path = sc.nextLine().trim();
        if (path.isEmpty()) {
            System.out.println("No path entered. Aborting upload.");
            return;
        }

        MovieManager.UploadReport report = manager.uploadCSV(path);
        System.out.println("Upload finished. Inserted: " + report.inserted + ", Errors: " + report.errors.size());
        if (!report.errors.isEmpty()) {
            System.out.println("Errors (first 20 shown):");
            int count = 0;
            for (String err : report.errors) {
                System.out.println(" - " + err);
                if (++count >= 20) {
                    System.out.println(" (more errors omitted...)");
                    break;
                }
            }
        }
    }

    public void showScariness() {
        List<Movie> movies = manager.getAll();
        if (movies.isEmpty()) {
            System.out.println("No movies available.");
            return;
        }
        listMovies();
        int choice = promptInt("Enter the number of the movie to calculate scariness: ", 1, movies.size());
        Movie m = movies.get(choice - 1);
        System.out.println("\n🎬 Movie Selected:");
        System.out.println(m.prettyPrint());
        System.out.printf("😱 Scariness Score: %.1f / 10.0%n", m.getScariness());
    }

    // ----------------- Input Helpers -----------------

    public String promptNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("⚠️ This field cannot be empty. Please enter a value.");
        }
    }

    public int promptInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val < min) System.out.println("⚠️ Value must be at least " + min);
                else if (val > max) System.out.println("⚠️ Value must be at most " + max);
                else return val;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid integer. Please enter a valid number.");
            }
        }
    }

    public double promptDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val < min) System.out.println("⚠️ Value must be at least " + min);
                else if (val > max) System.out.println("⚠️ Value must be at most " + max);
                else return val;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid decimal number. Please enter a valid number.");
            }
        }
    }

    public boolean promptBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim().toLowerCase();
            if (line.matches("true|t|yes|y|1")) return true;
            if (line.matches("false|f|no|n|0")) return false;
            System.out.println("⚠️ Please answer true/false, yes/no, y/n, or 1/0.");
        }
    }

    public String promptOptionalString(String prompt, String oldValue) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        return line.isEmpty() ? oldValue : line;
    }

    public int promptOptionalInt(String prompt, int min, int max, int oldValue) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) return oldValue;
            try {
                int val = Integer.parseInt(line);
                if (val < min) System.out.println("⚠️ Value must be at least " + min);
                else if (val > max) System.out.println("⚠️ Value must be at most " + max);
                else return val;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid integer. Please enter a valid number.");
            }
        }
    }

    public double promptOptionalDouble(String prompt, double min, double max, double oldValue) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) return oldValue;
            try {
                double val = Double.parseDouble(line);
                if (val < min) System.out.println("⚠️ Value must be at least " + min);
                else if (val > max) System.out.println("⚠️ Value must be at most " + max);
                else return val;
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid decimal number.");
            }
        }
    }

    public boolean promptOptionalBoolean(String prompt, boolean oldValue) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim().toLowerCase();
            if (line.isEmpty()) return oldValue;
            if (line.matches("true|t|yes|y|1")) return true;
            if (line.matches("false|f|no|n|0")) return false;
            System.out.println("⚠️ Please answer true/false, yes/no, y/n, or 1/0.");
        }
    }

    // ----------------- Helpers -----------------

    private String padRight(String text, int length) {
        if (text.length() > length) return text.substring(0, length);
        return String.format("%-" + length + "s", text);
    }
}
