# Horror Movies Database Management System (DMS) – Phase 2

## **Project Overview**
This project is a **Command-Line Interface (CLI) database manager** for horror movies, using **CSV files for persistence**. Phase 2 focuses on **unit testing** and ensuring that all CRUD operations and custom features work correctly.  

---

## **Features Implemented (Phase 2)**

### 1️ File Handling
- Movies are stored and loaded from a CSV file (`movies.csv`).  
- JUnit tests confirm that files **open successfully** and handle valid/invalid lines.

### 2️ Add Movies
- Users can **add new movies** via CLI or programmatically.  
- JUnit tests validate that movies are correctly inserted into the database.

### 3️ Remove Movies
- Movies can be **removed** by index.  
- JUnit tests verify successful removal.

### 4️ Update Movies
- Any movie attribute can be **edited** via CLI.  
- JUnit tests simulate edits by replacing an old movie with updated data.

### 5️ Custom Action: Scariness Score
- Each movie has a **scariness score** calculated from rating, votes, runtime, and watched status.  
- JUnit tests verify that scores are **between 0.0 and 10.0**.

### 6️ CSV Upload
- Users can **upload movies from an external CSV file**.  
- CSV format is validated; invalid lines are reported but skipped.  
- JUnit tests verify insertion count and errors.

---

## **Unit Testing (JUnit 5)**
- All operations are **fully tested** using dummy movie objects.  
- Tests include:  
  - File loading (`testLoadMovies_FileOpensSuccessfully`)  
  - Adding movies (`testAddMovie`)  
  - Removing movies (`testRemoveMovie`)  
  - Editing movies (`testEditMovie`)  
  - Custom action (`testMovieScariness`)  
  - Uploading CSV (`testUploadCSV`)  

- Tests are located in `src/MovieManagerTest.java` and are executable in **IntelliJ IDEA**.

---

## **Phase 2 Completion Status**
| Requirement | Status |
|------------|--------|
| File open | 
| Add object | 
| Remove object | 
| Update object (any attribute) | 
| Custom action (scariness) | 
| Dummy objects for tests |
| JUnit tests executable | 

**All requirements for Phase 2 are fully implemented and verified.**  

---

## 🎬 CLI Usage Instructions

After running the project (`Main.java`), the CLI provides a menu to manage your horror movie database.  

### **Main Menu Options**
1. **Show all movies**  
   - Displays all movies in a table with their attributes (title, director, year, rating, runtime, votes, watched).

2. **Add a new movie**  
   - Prompts for each movie attribute:
     - Title  
     - Year  
     - Director  
     - Rating (0.0 - 10.0)  
     - Runtime (minutes)  
     - Votes  
     - Watched (yes/no or true/false)  
   - Saves the movie immediately to `movies.csv`.

3. **Delete a movie**  
   - Lists all movies with index numbers.  
   - Enter the number of the movie to delete it from the database.

4. **Upload movies from CSV file**  
   - Enter the file path of a CSV file to upload multiple movies at once.  
   - Invalid lines are skipped, and errors are displayed.

5. **Edit a movie**  
   - Select a movie by number.  
   - For each attribute, you can either enter a new value or leave blank to keep the current value.  
   - Changes are saved immediately.

6. **Calculate Scariness of a movie**  
   - Select a movie by number.  
   - Displays its **scariness score** (0.0 – 10.0) based on rating, votes, runtime, and watched status.

7. **Exit**  
   - Closes the program.

---

### **Example Workflow**
```text
=== HORROR MOVIES MANAGER (CLI) ===
1. Show all movies
2. Add a new movie
3. Delete a movie
4. Upload movies from CSV file
5. Edit a movie
6. Calculate Scariness of a movie
7. Exit
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
