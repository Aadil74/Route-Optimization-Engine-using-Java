// Station.java
// MODEL class — represents a single node (stop/station) in the transport network.
// Equivalent to a city bus stop or train station on a map.

public class Station {

    // --- ENCAPSULATION: all fields private ---
    private int id;           // Unique numeric identifier (used as array index)
    private String name;      // Human-readable station name
    private String type;      // Transport type: "BUS", "TRAIN", or "METRO"
    private String zone;      // Area of the city: "NORTH", "SOUTH", "CENTRAL", etc.

    // --- CONSTRUCTOR ---
    public Station(int id, String name, String type, String zone) {
        this.id   = id;
        this.name = name;
        this.type = type;
        this.zone = zone;
    }

    // --- GETTERS ---
    public int getId()       { return id;   }
    public String getName()  { return name; }
    public String getType()  { return type; }
    public String getZone()  { return zone; }

    // --- toString: used when printing station info in menus ---
    @Override
    public String toString() {
        return String.format("  [%2d] %-28s | %-6s | Zone: %s", id, name, type, zone);
    }
}
