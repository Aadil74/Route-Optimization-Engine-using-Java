// Main.java
// APPLICATION ENTRY POINT and CONTROLLER.
// Manages the console menu loop and coordinates all other classes.
// Does NOT contain any business logic — purely user interaction.

import java.util.Scanner;
import java.awt.Desktop;
import java.io.File;

public class Main {

    // --- Core application objects (created once, shared across methods) ---
    private static TransportNetwork network;
    private static RouteEngine      routeEngine;
    private static AIAdvisor        aiAdvisor;
    private static JourneyLogger    logger;
    private static Scanner          scanner;

    // =========================================================
    //  LAUNCH WEB UI
    // =========================================================
    private static void launchWebUI() {
        try {
            File htmlFile = new File("src/smarttransit.html");

            if (!htmlFile.exists()) {
                System.out.println("  [INFO] Web UI file not found, running console mode.");
                return;
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(htmlFile.toURI());
                System.out.println("  [INFO] SmartTransit Web UI launched in browser.");
            }

        } catch (Exception e) {
            System.out.println("  [WARN] Could not launch browser: " + e.getMessage());
        }
    }

    // =========================================================
    //  ENTRY POINT
    // =========================================================
    public static void main(String[] args) {

        launchWebUI(); // ← Opens browser automatically on startup

        // Initialise all components
        network     = new TransportNetwork();
        routeEngine = new RouteEngine(network);
        aiAdvisor   = new AIAdvisor();
        logger      = new JourneyLogger();
        scanner     = new Scanner(System.in);

        printBanner();

        // --- MAIN APPLICATION LOOP ---
        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("\n  Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 1:
                    handleViewStations();
                    break;
                case 2:
                    handleFindRoute();
                    break;
                case 3:
                    network.displayNetworkStats();
                    break;
                case 4:
                    handleStationSearch();
                    break;
                case 5:
                    printSessionSummary();
                    running = false;
                    break;
                default:
                    System.out.println("\n  [!] Invalid choice. Enter a number from 1 to 5.");
            }
        }

        scanner.close();
    }

    // =========================================================
    //  MENU HANDLERS
    // =========================================================

    private static void handleViewStations() {
        network.displayAllStations();
    }

    private static void handleFindRoute() {

        network.displayAllStations();

        System.out.print("\n  Enter ORIGIN station ID: ");
        int originId = readInt();

        System.out.print("  Enter DESTINATION station ID: ");
        int destId = readInt();

        Station origin = network.getStation(originId);
        Station dest   = network.getStation(destId);

        if (origin == null || dest == null) {
            System.out.println("\n  [ERROR] One or both station IDs are invalid. Please try again.");
            return;
        }

        if (originId == destId) {
            System.out.println("\n  [ERROR] Origin and destination cannot be the same station.");
            return;
        }

        System.out.println("\n  🔍 Calculating optimal route...");
        System.out.println("  From : " + origin.getName());
        System.out.println("  To   : " + dest.getName());

        RouteResult result = routeEngine.findShortestRoute(originId, destId);

        if (result == null) {
            System.out.println("\n  [!] No route found between these stations.");
            System.out.println("  This may indicate the stations are not connected in the network.");
            logger.logFailedSearch(origin.getName(), dest.getName());
            return;
        }

        result.display();

        System.out.println("\n  🤖 Generating travel advisory...");
        String advisory = aiAdvisor.getAdvisory(result);
        System.out.println("\n  ╔══════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                    AI TRAVEL ADVISORY                        ║");
        System.out.println("  ╠══════════════════════════════════════════════════════════════╣");
        System.out.println("  " + advisory);
        System.out.println("  ╚══════════════════════════════════════════════════════════════╝");

        logger.logJourney(origin.getName(), dest.getName(), result, advisory);
    }

    private static void handleStationSearch() {
        System.out.print("\n  Enter station name to search: ");
        scanner.nextLine();
        String query = scanner.nextLine();

        Station found = network.findStationByName(query);
        if (found == null) {
            System.out.println("  [!] No station found matching: \"" + query + "\"");
        } else {
            System.out.println("\n  ✔ Station found:");
            System.out.println(found);
        }
    }

    // =========================================================
    //  UI HELPERS
    // =========================================================

    private static void printBanner() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════════╗");
        System.out.println("  ║         SmartTransit — Urban Route Optimization Engine        ║");
        System.out.println("  ║               AI-Assisted Transit Advisory System             ║");
        System.out.println("  ║                      Author: Ahmed Aadil                      ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════════╝");
    }

    private static void printMainMenu() {
        System.out.println("\n  ┌─────────────────────────────────────────┐");
        System.out.println("  │             MAIN MENU                   │");
        System.out.println("  ├─────────────────────────────────────────┤");
        System.out.println("  │  1.  View All Stations                  │");
        System.out.println("  │  2.  Find Shortest Route  ◀ CORE       │");
        System.out.println("  │  3.  Network Connectivity Report        │");
        System.out.println("  │  4.  Search Station by Name             │");
        System.out.println("  │  5.  Exit                               │");
        System.out.println("  └─────────────────────────────────────────┘");
    }

    private static void printSessionSummary() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                     SESSION SUMMARY                          ║");
        System.out.println("  ╠══════════════════════════════════════════════════════════════╣");
        System.out.printf( "  ║   Routes calculated this session: %-27s║%n",
                logger.getSessionJourneyCount());
        System.out.println("  ║   Journey history saved to: journey_log.txt                  ║");
        System.out.println("  ║   Thank you for using SmartTransit, Ahmed Aadil!             ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════════╝\n");
    }

    private static int readInt() {
        if (scanner.hasNextInt()) {
            int val = scanner.nextInt();
            scanner.nextLine();
            return val;
        } else {
            scanner.nextLine();
            return -1;
        }
    }
}