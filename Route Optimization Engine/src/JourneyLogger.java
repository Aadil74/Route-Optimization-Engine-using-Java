// JourneyLogger.java
// Handles FILE I/O — saves every journey search to a text log file.
// This demonstrates Java's file writing capabilities (java.io package).
// The log persists between application runs — each search is appended.

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JourneyLogger {

    // The log file will be created in the project's root directory
    private static final String LOG_FILE = "journey_log.txt";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Counter for total journeys logged this session
    private int sessionJourneyCount;

    // --- CONSTRUCTOR ---
    public JourneyLogger() {
        sessionJourneyCount = 0;
        // Write a session start header to the log
        writeToFile("════════════════════════════════════════════════════════");
        writeToFile("  SESSION STARTED: " + LocalDateTime.now().format(FORMATTER));
        writeToFile("  SmartTransit — AI-Assisted Urban Route Optimization");
        writeToFile("  Author: Ahmed Aadil");
        writeToFile("════════════════════════════════════════════════════════");
    }

    // --- PUBLIC: logJourney() ---
    // Called every time a successful route is found.
    // Appends the journey details to the log file.
    public void logJourney(String originName, String destName, RouteResult result, String advisory) {
        sessionJourneyCount++;

        writeToFile("\n  Journey #" + sessionJourneyCount);
        writeToFile("  Timestamp : " + LocalDateTime.now().format(FORMATTER));
        writeToFile("  From      : " + originName);
        writeToFile("  To        : " + destName);
        writeToFile("  Route     : " + result.getPathAsString());
        writeToFile("  Time      : " + result.getTotalTime() + " minutes");
        writeToFile("  Stops     : " + result.getStopCount() + " intermediate");
        writeToFile("  Advisory  : " + advisory.replace("\n", " ").trim());
        writeToFile("  ─────────────────────────────────────────────────────");

        System.out.println("\n  ✔ Journey saved to journey_log.txt");
    }

    // --- PUBLIC: logFailedSearch() ---
    // Records when no route was found between two stations.
    public void logFailedSearch(String originName, String destName) {
        writeToFile("\n  [NO ROUTE] " + LocalDateTime.now().format(FORMATTER)
                + " | " + originName + " → " + destName);
    }

    // --- PRIVATE: writeToFile() ---
    // Core file-writing method. Uses 'append = true' so we never overwrite old logs.
    private void writeToFile(String text) {
        // try-with-resources automatically closes the file even if an error occurs
        try (PrintWriter pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(text);
        } catch (IOException e) {
            // Log writing should never crash the app — fail silently
            System.out.println("  [WARN] Could not write to log file: " + e.getMessage());
        }
    }

    // --- PUBLIC: getSessionJourneyCount() ---
    public int getSessionJourneyCount() {
        return sessionJourneyCount;
    }
}