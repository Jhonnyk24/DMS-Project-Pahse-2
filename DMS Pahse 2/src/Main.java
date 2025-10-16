/**
 * Program entry point.
 * Uses "movies.csv" in the working directory for persistence.
 */
public class Main {
    public static void main(String[] args) {
        // Use CSV only (as requested)
        MovieManager manager = new MovieManager("movies.csv");
        MovieCLI cli = new MovieCLI(manager);
        cli.run();
    }
}