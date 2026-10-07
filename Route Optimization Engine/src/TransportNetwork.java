// TransportNetwork.java
// Manages ALL stations and the connections between them.
// The network is stored as a 2D ADJACENCY MATRIX — a core CS data structure.
// matrix[i][j] = travel time in minutes from station i to station j.
// matrix[i][j] = 0 means NO direct connection exists.

public class TransportNetwork {

    // Maximum number of stations our network can hold
    public static final int MAX_STATIONS = 15;

    // The adjacency matrix — a 2D array of travel times
    // This is the GRAPH that Dijkstra's algorithm will search
    private int[][] matrix;

    // Array of Station objects — parallel to the matrix rows/columns
    private Station[] stations;
    private int stationCount;

    // --- CONSTRUCTOR ---
    public TransportNetwork() {
        matrix       = new int[MAX_STATIONS][MAX_STATIONS]; // All zeroes by default = no connections
        stations     = new Station[MAX_STATIONS];
        stationCount = 0;
        loadNetwork(); // Pre-load all stations and routes
    }

    // --- PRIVATE: loadNetwork() ---
    // Hardcoded fictional-but-realistic city network.
    // Fictional keeps the demo clean and fully under our control.
    private void loadNetwork() {

        // --- CHENNAI STATIONS ---
        // Real stations across Chennai's MTC Bus, MRTS, and Metro networks
        addStation(new Station(0,  "Chennai Central",         "TRAIN", "CENTRAL"));
        addStation(new Station(1,  "Koyambedu CMBT",          "BUS",   "WEST"   ));
        addStation(new Station(2,  "Guindy",                  "METRO", "SOUTH"  ));
        addStation(new Station(3,  "T-Nagar Bus Terminus",    "BUS",   "CENTRAL"));
        addStation(new Station(4,  "Tambaram",                "TRAIN", "SOUTH"  ));
        addStation(new Station(5,  "Anna Nagar Tower",        "METRO", "NORTH"  ));
        addStation(new Station(6,  "Velachery",               "METRO", "SOUTH"  ));
        addStation(new Station(7,  "Chennai Airport",         "TRAIN", "SOUTH"  ));
        addStation(new Station(8,  "Egmore",                  "TRAIN", "CENTRAL"));
        addStation(new Station(9,  "Adyar Bus Depot",         "BUS",   "SOUTH"  ));
        addStation(new Station(10, "Porur Junction",          "BUS",   "WEST"   ));
        addStation(new Station(11, "Chromepet",               "TRAIN", "SOUTH"  ));
        addStation(new Station(12, "Vadapalani",              "METRO", "CENTRAL"));

        // --- CHENNAI ROUTES ---
        // Travel times in minutes based on approximate real-world durations
        addRoute(0,  8,  10);  // Chennai Central ↔ Egmore
        addRoute(0,  3,  20);  // Chennai Central ↔ T-Nagar
        addRoute(0,  5,  35);  // Chennai Central ↔ Anna Nagar
        addRoute(0,  2,  30);  // Chennai Central ↔ Guindy
        addRoute(8,  5,  25);  // Egmore ↔ Anna Nagar
        addRoute(8,  3,  15);  // Egmore ↔ T-Nagar
        addRoute(1,  5,  20);  // Koyambedu ↔ Anna Nagar
        addRoute(1,  10, 18);  // Koyambedu ↔ Porur
        addRoute(1,  12, 15);  // Koyambedu ↔ Vadapalani
        addRoute(3,  12, 12);  // T-Nagar ↔ Vadapalani
        addRoute(3,  2,  18);  // T-Nagar ↔ Guindy
        addRoute(3,  9,  22);  // T-Nagar ↔ Adyar
        addRoute(2,  7,  12);  // Guindy ↔ Chennai Airport
        addRoute(2,  6,  15);  // Guindy ↔ Velachery
        addRoute(2,  11, 25);  // Guindy ↔ Chromepet
        addRoute(7,  11, 18);  // Chennai Airport ↔ Chromepet
        addRoute(11, 4,  15);  // Chromepet ↔ Tambaram
        addRoute(6,  9,  20);  // Velachery ↔ Adyar
        addRoute(6,  4,  30);  // Velachery ↔ Tambaram
        addRoute(10, 12, 20);  // Porur ↔ Vadapalani
        addRoute(10, 1,  18);  // Porur ↔ Koyambedu
        addRoute(12, 2,  20);  // Vadapalani ↔ Guindy
        addRoute(9,  4,  35);  // Adyar ↔ Tambaram
    }

    // --- PRIVATE: addStation() ---
    private void addStation(Station s) {
        if (stationCount < MAX_STATIONS) {
            stations[stationCount] = s;
            stationCount++;
        }
    }

    // --- PUBLIC: addRoute() ---
    // Sets travel time in BOTH directions (undirected graph)
    public void addRoute(int fromId, int toId, int travelTime) {
        matrix[fromId][toId] = travelTime; // Direction A → B
        matrix[toId][fromId] = travelTime; // Direction B → A (same time both ways)
    }

    // --- PUBLIC: getTravelTime() ---
    // Returns the direct travel time between two stations.
    // Returns 0 if no direct connection exists.
    public int getTravelTime(int fromId, int toId) {
        return matrix[fromId][toId];
    }

    // --- PUBLIC: getStation() ---
    public Station getStation(int id) {
        if (id >= 0 && id < stationCount) {
            return stations[id];
        }
        return null;
    }

    // --- PUBLIC: getStationCount() ---
    public int getStationCount() {
        return stationCount;
    }

    // --- PUBLIC: findStationByName() ---
    // Case-insensitive partial name search.
    // Returns the first match found, or null if nothing matches.
    public Station findStationByName(String query) {
        String lowerQuery = query.toLowerCase().trim();
        for (int i = 0; i < stationCount; i++) {
            if (stations[i].getName().toLowerCase().contains(lowerQuery)) {
                return stations[i];
            }
        }
        return null;
    }

    // --- PUBLIC: displayAllStations() ---
    // Prints the full station list so users can pick their origin/destination.
    public void displayAllStations() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║                  STATION DIRECTORY                       ║");
        System.out.println("  ╠══════════════════════════════════════════════════════════╣");
        for (int i = 0; i < stationCount; i++) {
            System.out.println(stations[i]); // Calls Station.toString()
        }
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
    }

    // --- PUBLIC: displayNetworkStats() ---
    // Counts and displays how many direct connections each station has.
    // Also flags any station with only 1 connection (isolated risk alert).
    public void displayNetworkStats() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║               NETWORK CONNECTIVITY REPORT                ║");
        System.out.println("  ╠══════════════════════════════════════════════════════════╣");

        for (int i = 0; i < stationCount; i++) {
            int connections = 0;
            // Count non-zero entries in row i of the matrix
            for (int j = 0; j < stationCount; j++) {
                if (matrix[i][j] > 0) connections++;
            }
            String alert = (connections <= 1) ? "  ⚠ LOW CONNECTIVITY" : "";
            System.out.printf("  ║  %-28s | %d connection(s)%-18s║%n",
                    stations[i].getName(), connections, alert);
        }
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
    }
}
