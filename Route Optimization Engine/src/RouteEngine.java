// RouteEngine.java
// THE CORE OF THE PROJECT — implements Dijkstra's Shortest Path Algorithm.
//
// WHAT IS DIJKSTRA'S ALGORITHM?
// Given a starting station, it finds the MINIMUM travel time to every other
// station in the network. We then trace back the path to our destination.
//
// TIME COMPLEXITY: O(N²) for this array-based implementation — perfectly
// appropriate for academic demonstration with small networks.

public class RouteEngine {

    private TransportNetwork network; // Reference to the network (graph)

    // --- CONSTRUCTOR ---
    public RouteEngine(TransportNetwork network) {
        this.network = network; // Inject the network — this is called DEPENDENCY INJECTION
    }

    // =========================================================
    //  DIJKSTRA'S ALGORITHM
    // =========================================================
    // Returns a RouteResult object containing the full path and total time.
    // Returns null if no path exists between origin and destination.
    public RouteResult findShortestRoute(int originId, int destinationId) {

        int n = network.getStationCount(); // Total number of stations

        // dist[i] = best known travel time from origin to station i
        // Start with "infinity" for all stations (Integer.MAX_VALUE / 2 avoids overflow)
        int[] dist = new int[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE / 2; // Represents "not yet reached"
        }
        dist[originId] = 0; // Distance from origin to itself is always 0

        // visited[i] = true once we have finalized the shortest path to station i
        boolean[] visited = new boolean[n]; // All false by default

        // prev[i] = the station we came FROM to reach station i on the best path
        // Used to reconstruct the full route at the end
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            prev[i] = -1; // -1 means "no previous station yet"
        }

        // --- MAIN DIJKSTRA LOOP ---
        // Runs N times — once for each station in the network
        for (int iteration = 0; iteration < n; iteration++) {

            // STEP 1: Find the unvisited station with the smallest known distance
            int current = -1;
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    if (current == -1 || dist[i] < dist[current]) {
                        current = i; // This station is our best candidate
                    }
                }
            }

            // If smallest distance is still "infinity", remaining stations are unreachable
            if (dist[current] == Integer.MAX_VALUE / 2) break;

            // STEP 2: Mark this station as permanently visited (finalized)
            visited[current] = true;

            // If we just finalized our destination, we can stop early
            if (current == destinationId) break;

            // STEP 3: RELAXATION — update distances to all neighbors of 'current'
            for (int neighbor = 0; neighbor < n; neighbor++) {
                int travelTime = network.getTravelTime(current, neighbor);

                // Only consider stations with a direct connection (travelTime > 0)
                // and not yet finalized
                if (travelTime > 0 && !visited[neighbor]) {
                    int newDist = dist[current] + travelTime;

                    // If going through 'current' is faster than what we knew before — UPDATE
                    if (newDist < dist[neighbor]) {
                        dist[neighbor]  = newDist;  // Better distance found
                        prev[neighbor]  = current;  // Remember we came from 'current'
                    }
                }
            }
        }

        // --- NO PATH EXISTS ---
        if (dist[destinationId] == Integer.MAX_VALUE / 2) {
            return null; // Destination is unreachable from origin
        }

        // --- RECONSTRUCT THE PATH ---
        // Walk backwards from destination to origin using the prev[] array
        // then reverse to get the correct order
        int[] tempPath   = new int[n];
        int   pathLength = 0;
        int   step       = destinationId;

        while (step != -1) {
            tempPath[pathLength] = step;
            pathLength++;
            step = prev[step]; // Move to the previous station
        }

        // Reverse the path (currently destination → origin, we want origin → destination)
        int[] finalPath = new int[pathLength];
        for (int i = 0; i < pathLength; i++) {
            finalPath[i] = tempPath[pathLength - 1 - i];
        }

        // Package everything into a RouteResult and return
        return new RouteResult(finalPath, dist[destinationId], network);
    }
}
