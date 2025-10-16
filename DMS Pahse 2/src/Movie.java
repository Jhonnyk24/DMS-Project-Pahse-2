import java.time.Year;

/**
 * Represents a single movie and contains CSV-parsing + validation logic.
 */
public class Movie {
    // Movie attributes
    private String title;
    private int year;
    private String director;
    private double rating;         // 0.0 - 10.0
    private int runtimeMinutes;    // > 0
    private int votes;             // >= 0
    private boolean watched;

    // Constructor with values
    public Movie(String title, int year, String director, double rating, int runtimeMinutes, int votes, boolean watched) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.runtimeMinutes = runtimeMinutes;
        this.votes = votes;
        this.watched = watched;
    }

    // Empty constructor (not strictly necessary but convenient)
    public Movie() {}

    // ----- Getters -----
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getDirector() { return director; }
    public double getRating() { return rating; }
    public int getRuntimeMinutes() { return runtimeMinutes; }
    public int getVotes() { return votes; }
    public boolean isWatched() { return watched; }

    // ----- Calculation -----

    /**
     * Calculates a scariness score for the movie based on rating, votes, runtime, and watched status.
     * Returns a double between 0.0 and 10.0
     */
    public double getScariness() {
        double score = rating; // base
        score += Math.min(votes / 500000.0, 2); // max +2 from votes
        if (runtimeMinutes > 120) score += 1;
        if (watched) score -= 1; // already seen, less scary
        if (score < 0) score = 0;
        if (score > 10) score = 10;
        return score;
    }

    // ----- Helpers -----

    /**
     * Convert the movie to a CSV line (same order used by parser).
     * Escaping/quoting is not implemented — fields must not contain commas.
     */
    @Override
    public String toString() {
        return String.format("%s,%d,%s,%.1f,%d,%d,%s",
                title, year, director, rating, runtimeMinutes, votes, watched);
    }

    /**
     * Nice human-friendly representation for CLI printing.
     */
    public String prettyPrint() {
        return String.format("🎬 %s (%d) - Dir: %s | Rating: %.1f | %d min | Votes: %d | Watched: %s",
                title, year, director, rating, runtimeMinutes, votes, watched ? "Yes" : "No");
    }

    /**
     * Parse a CSV line into a Movie object and validate fields.
     * Throws IllegalArgumentException with a clear message on error.
     *
     * Expected CSV columns (7):
     * title,year,director,rating,runtimeMinutes,votes,watched
     *
     * NOTE: This parser expects simple CSV without quoted commas.
     */
    public static Movie fromCSV(String line) throws IllegalArgumentException {
        if (line == null) throw new IllegalArgumentException("Line is null");

        // Split into fields (keep empty fields if any)
        String[] parts = line.split(",", -1);

        if (parts.length != 7) {
            throw new IllegalArgumentException("Expected 7 fields but found " + parts.length);
        }

        // Trim all fields
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        // Field 1: title (non-empty)
        String title = parts[0];
        if (title.isEmpty()) {
            throw new IllegalArgumentException("Title is empty");
        }

        // Field 2: year (integer, reasonable range)
        int year;
        try {
            year = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Year is not a valid integer: '" + parts[1] + "'");
        }
        int currentYear = Year.now().getValue();
        if (year < 1888 || year > currentYear) { // 1888 is traditionally one of first film years
            throw new IllegalArgumentException("Year must be between 1888 and " + currentYear);
        }

        // Field 3: director (non-empty)
        String director = parts[2];
        if (director.isEmpty()) {
            throw new IllegalArgumentException("Director is empty");
        }

        // Field 4: rating (double 0.0 - 10.0)
        double rating;
        try {
            rating = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Rating is not a valid number: '" + parts[3] + "'");
        }
        if (rating < 0.0 || rating > 10.0) {
            throw new IllegalArgumentException("Rating must be between 0.0 and 10.0");
        }

        // Field 5: runtimeMinutes (positive integer)
        int runtime;
        try {
            runtime = Integer.parseInt(parts[4]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Runtime is not a valid integer: '" + parts[4] + "'");
        }
        if (runtime <= 0) {
            throw new IllegalArgumentException("Runtime must be a positive integer");
        }

        // Field 6: votes (non-negative integer)
        int votes;
        try {
            votes = Integer.parseInt(parts[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Votes is not a valid integer: '" + parts[5] + "'");
        }
        if (votes < 0) {
            throw new IllegalArgumentException("Votes must be 0 or greater");
        }

        // Field 7: watched (boolean) — accept many forms
        boolean watched;
        String w = parts[6].toLowerCase();
        if (w.equals("true") || w.equals("yes") || w.equals("y") || w.equals("1")) {
            watched = true;
        } else if (w.equals("false") || w.equals("no") || w.equals("n") || w.equals("0")) {
            watched = false;
        } else {
            throw new IllegalArgumentException("Watched must be true/false or yes/no or 1/0: '" + parts[6] + "'");
        }

        // If all validations pass, return a new Movie
        return new Movie(title, year, director, rating, runtime, votes, watched);
    }
}