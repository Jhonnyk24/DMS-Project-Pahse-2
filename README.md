# Horror Movies Database Management System (DMS) - Phase 2

## Project Overview
This project is a Command-Line Interface (CLI) database manager for horror movies, with persistence using CSV files.  
**Phase 2** focuses on **unit testing** and ensuring all CRUD operations and other features work correctly.

---

## Features Implemented (Phase 2)

### 1️ File Handling
- Movies are loaded from and stored in a CSV file (`movies.csv`).  
- JUnit tests ensure that files open correctly and handle valid/invalid lines properly.

### 2️ Add Movies
- New movies can be added through the CLI or programmatically.  
- JUnit tests confirm that movies are correctly inserted into the database.

### 3️ Remove Movies
- Movies can be deleted using their indexed position in the database.  
- JUnit tests verify successful removal.

### 4️ Update Movies
- Any attribute of a movie can be edited through the CLI.  
- JUnit tests confirm that updates overwrite the old movie correctly.

### 5️ Custom Action: Scariness Rating
- Movies have a **scariness score** based on rating, votes, runtime, and watched status.  
- JUnit tests ensure scores are always between `0.0` and `10.0`.

### 6️ CSV Upload
- Users can upload movies from an external CSV file.  
- Invalid lines are skipped, and errors are reported.  
- JUnit tests assert the number of successfully inserted movies and reported errors.

---

## Unit Testing (JUnit 5)
All operations are tested using **dummy movie objects**.

**Tests include:**
- File loading (`testLoadMovies_FileOpensSuccessfully`)  
- Adding movies (`testAddMovie`)  
- Removing movies (`testRemoveMovie`)  
- Editing movies (`testEditMovie`)  
- Custom action (`testMovieScariness`)  
- CSV upload (`testUploadCSV`)  

Tests are located in `src/MovieManagerTest.java` and can be run in **IntelliJ IDEA**.

---

## Phase 2 Completion Status

| Requirement                    | Status |
|--------------------------------|--------|
| File open                       | ✅     |
| Add object                      | ✅     |
| Remove object                   | ✅     |
| Update object (any attribute)   | ✅     |
| Custom action (scariness)       | ✅     |
| Dummy objects for tests         | ✅     |
| JUnit tests executable          | ✅     |

**Phase 2 has all requirements implemented and verified.**

---

## 🎬 CLI Usage Instructions
Run the project (`Main.java`) to access the main menu and manage your horror movie database.

### Main Menu Options

#### Show all movies
Displays all movies in a table with attributes:
- Title, Director, Year, Rating, Runtime, Votes, Watched

#### Add a new movie
Prompts for:
- Title  
- Year  
- Director  
- Rating (0.0 - 10.0)  
- Runtime (minutes)  
- Votes  
- Watched (yes/no or true/false)  

Saves the movie immediately to `movies.csv`.

#### Delete a movie
- Lists movies with index numbers.  
- Enter the index to remove a movie from the database.

#### Upload movies from CSV file
- Enter a CSV file path to add multiple movies at once.  
- Invalid lines are omitted, and errors are displayed.

#### Edit a movie
- Select a movie by index.  
- Update any attribute or leave the current value unchanged.  
- Changes are saved instantly.

#### Determine Scariness of a movie
- Select a movie by index.  
- Displays the scariness score (`0.0 - 10.0`) based on rating, votes, runtime, and watched status.

#### Exit
Closes the program.

---

## Example Workflow

=== HORROR MOVIES MANAGER (CLI) ===

Show all movies

Add a new movie

Delete a movie

Upload movies from CSV file

Edit a movie

Determine Scariness of a movie

Exit
Choose an option (1-7): 2

--- Add a new movie ---
Title: The Shining
Year (e.g. 1980): 1980
Director: Stanley Kubrick
Rating (0.0 - 10.0): 8.4
Runtime minutes (>0): 146
Votes (0 or greater): 500000
Watched? (yes/no): yes
✅ Movie added successfully!
