// RouteResult.java
// A simple DATA CONTAINER class that holds the output of Dijkstra's algorithm.
// Separating results into their own class makes the code cleaner and reusable —
// other classes (AIAdvisor, JourneyLogger) can receive this object and read from it.

public class RouteResult {

    private int[] path;              // Array of station IDs in order: origin → destination
    private int   totalTime;         // Total travel time in minutes
    private TransportNetwork network; // Reference needed to look up station names

    // --- CONSTRUCTOR ---
    public RouteResult(int[] path, int totalTime, TransportNetwork network) {
        this.path    = path;
        this.totalTime = totalTime;
        this.network = network;
    }

    // --- GETTERS ---
    public int[]  getPath()      { return path;      }
    public int    getTotalTime() { return totalTime;  }
    public int    getStopCount() { return path.length - 2; } // Excludes origin and destination

    // --- PUBLIC: getPathAsString() ---
    // Builds a human-readable string of the route for display and logging.
    // Example: "Central Terminal → Riverside → Southpark Terminal"
    public String getPathAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.length; i++) {
            sb.append(network.getStation(path[i]).getName());
            if (i < path.length - 1) {
                sb.append(" → "); // Arrow separator between station names
            }
        }
        return sb.toString();
    }

    // --- PUBLIC: display() ---
    // Prints the full formatted route result to the console.
    public void display() {
        System.out.println("\n  ╔══════════════════════════════════════════════════════════════╗");
        System.out.println("  ║                   OPTIMAL ROUTE FOUND                        ║");
        System.out.println("  ╠══════════════════════════════════════════════════════════════╣");

        // Print each step of the journey with individual segment times
        System.out.println("  ║  JOURNEY BREAKDOWN:                                          ║");
        for (int i = 0; i < path.length; i++) {
            Station s = network.getStation(path[i]);
            if (i == 0) {
                System.out.printf("  ║   🚀 DEPART: %-46s║%n", s.getName() + " [" + s.getType() + "]");
            } else {
                int legTime = network.getTravelTime(path[i - 1], path[i]);
                System.out.printf("  ║        ↓  (%2d min)%n", legTime);
                if (i == path.length - 1) {
                    System.out.printf("  ║   🏁 ARRIVE: %-46s║%n", s.getName() + " [" + s.getType() + "]");
                } else {
                    System.out.printf("  ║   🔄 TRANSIT: %-45s║%n", s.getName() + " [" + s.getType() + "]");
                }
            }
        }

        System.out.println("  ╠══════════════════════════════════════════════════════════════╣");
        System.out.printf( "  ║   Total Stops (intermediate): %-31s║%n", getStopCount());
        System.out.printf( "  ║   Estimated Travel Time:      %-31s║%n", totalTime + " minutes");
        System.out.println("  ╚══════════════════════════════════════════════════════════════╝");
    }
}
