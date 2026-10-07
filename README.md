# 🚇 SmartTransit — AI-Assisted Urban Route Optimization Engine

> A console-based Java application modelling Chennai's public transport network as a weighted graph. Implements Dijkstra's shortest-path algorithm to compute optimal routes between real city stations, enhanced with an AI advisory layer and an interactive web interface.

**Author:** Ahmed Aadil  
**Domain:** Transportation Technology & Smart Mobility  
**Language:** Java (JDK 8+)  
**Institution:** University Project — Phase 1

---

## 📸 Preview

```
  ╔══════════════════════════════════════════════════════════════╗
  ║         SmartTransit — Urban Route Optimization Engine        ║
  ║               AI-Assisted Transit Advisory System             ║
  ║                      Author: Ahmed Aadil                      ║
  ╚══════════════════════════════════════════════════════════════╝

  ┌─────────────────────────────────────────┐
  │             MAIN MENU                   │
  ├─────────────────────────────────────────┤
  │  1.  View All Stations                  │
  │  2.  Find Shortest Route  ◀ CORE       │
  │  3.  Network Connectivity Report        │
  │  4.  Search Station by Name             │
  │  5.  Exit                               │
  └─────────────────────────────────────────┘
```

---

## 🌟 Features

- **Dijkstra's Shortest Path Algorithm** — finds the optimal route between any two Chennai stations across a 13-node weighted graph
- **Real Chennai Network** — 13 real stations including Chennai Central, Tambaram, Koyambedu, Anna Nagar, Velachery and more
- **AI Travel Advisory** — integrates Google Gemini API via pure Java `HttpURLConnection` to generate natural-language travel recommendations
- **Interactive Web UI** — self-contained HTML interface with Canvas-based map, animated route highlighting, and live journey breakdown
- **Network Connectivity Report** — identifies low-connectivity stations with visual bar charts
- **Journey Logger** — persists every route search to `journey_log.txt` with timestamps
- **Graceful Fallback** — rule-based advisory system activates automatically when no API key is configured
- **Auto Browser Launch** — web interface opens automatically when the Java application starts

---

## 🗂️ Project Structure

```
SmartTransit/
└── src/
    ├── Main.java               ← Entry point, console menu controller
    ├── Station.java            ← Model class (id, name, type, zone)
    ├── TransportNetwork.java   ← Adjacency matrix graph, Chennai data
    ├── RouteEngine.java        ← Dijkstra's algorithm implementation
    ├── RouteResult.java        ← Route data container, path display
    ├── AIAdvisor.java          ← Google Gemini API integration
    ├── JourneyLogger.java      ← File I/O, journey history
    └── smarttransit.html       ← Self-contained web interface
```

---

## 🧠 Core Concepts Demonstrated

| Concept | Implementation |
|--------|---------------|
| **Encapsulation** | `Station.java` — private fields, public getters/setters |
| **Abstraction** | `RouteEngine.java` — Dijkstra hidden behind `findShortestRoute()` |
| **Composition** | `RouteResult` holds a reference to `TransportNetwork` |
| **Single Responsibility** | Each class has exactly one job |
| **Dependency Injection** | `RouteEngine` receives `TransportNetwork` via constructor |
| **Graph Theory** | Weighted adjacency matrix `int[N][N]` |
| **Algorithm Design** | Dijkstra's O(N²) shortest path |
| **File I/O** | `java.io.FileWriter` for persistent journey logging |
| **API Integration** | REST call via `java.net.HttpURLConnection` |

---

## 🚉 Chennai Station Network

| ID | Station | Type | Zone |
|----|---------|------|------|
| 0 | Chennai Central | TRAIN | CENTRAL |
| 1 | Koyambedu CMBT | BUS | WEST |
| 2 | Guindy | METRO | SOUTH |
| 3 | T-Nagar Bus Terminus | BUS | CENTRAL |
| 4 | Tambaram | TRAIN | SOUTH |
| 5 | Anna Nagar Tower | METRO | NORTH |
| 6 | Velachery | METRO | SOUTH |
| 7 | Chennai Airport | TRAIN | SOUTH |
| 8 | Egmore | TRAIN | CENTRAL |
| 9 | Adyar Bus Depot | BUS | SOUTH |
| 10 | Porur Junction | BUS | WEST |
| 11 | Chromepet | TRAIN | SOUTH |
| 12 | Vadapalani | METRO | CENTRAL |

---

## ⚙️ How Dijkstra's Algorithm Works Here

```
1. Start at origin station → distance = 0, all others = ∞
2. Visit nearest unvisited station
3. Relax distances to all its neighbors:
   if (dist[current] + travelTime < dist[neighbor])
       update dist[neighbor] and record prev[neighbor]
4. Mark current as visited
5. Repeat until destination is finalized
6. Trace back path using prev[] array
7. Return RouteResult with path and total time
```

**Complexity:** O(N²) time · O(N) space · N = 13 stations

---

## 🚀 Getting Started

### Prerequisites
- JDK 8 or higher
- IntelliJ IDEA (recommended) or any Java IDE
- Internet connection (for AI advisory feature)

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/smarttransit.git
cd smarttransit
```

**2. Open in IntelliJ IDEA**
```
File → Open → select the SmartTransit folder → Trust Project
```

**3. Run the application**
```
Open Main.java → Shift + F10
```

The web interface opens automatically in your default browser.

---

## 🤖 Enabling AI Advisories (Optional)

The app works fully without an API key using the built-in rule-based advisor.

To enable real AI advisories:

1. Get a free API key at **aistudio.google.com**
2. Open `AIAdvisor.java`
3. Replace the placeholder:

```java
private static final String API_KEY = "YOUR_GEMINI_API_KEY_HERE";
```

> ⚠️ Never commit your real API key to GitHub. Use environment variables or a `.env` file for production use.

---

## 📋 Sample Output

```
  🔍 Calculating optimal route...
  From : Chennai Central
  To   : Tambaram

  ╔══════════════════════════════════════════════════════════════╗
  ║                   OPTIMAL ROUTE FOUND                        ║
  ╠══════════════════════════════════════════════════════════════╣
  ║  JOURNEY BREAKDOWN:                                          ║
  ║   🚀 DEPART: Chennai Central [TRAIN]                         ║
  ║        ↓  (30 min)
  ║   🔄 TRANSIT: Guindy [METRO]                                 ║
  ║        ↓  (25 min)
  ║   🔄 TRANSIT: Chromepet [TRAIN]                              ║
  ║        ↓  (15 min)
  ║   🏁 ARRIVE: Tambaram [TRAIN]                                ║
  ╠══════════════════════════════════════════════════════════════╣
  ║   Total Stops (intermediate): 2                              ║
  ║   Estimated Travel Time:      70 minutes                     ║
  ╚══════════════════════════════════════════════════════════════╝
```

---

## 🗺️ Web Interface

The included `smarttransit.html` provides a full visual interface:

- **Three-column layout** — sidebar controls, canvas map, results panel
- **Canvas-based map** — all 13 stations at real GPS coordinates
- **Animated route** — amber dashed line highlights the optimal path
- **No server required** — opens directly in any browser as a local file
- **Quick routes** — one-click demo buttons for instant presentation

---

## 📁 Output Files

| File | Description |
|------|-------------|
| `journey_log.txt` | Auto-generated log of all route searches with timestamps |
| `smarttransit.html` | Web interface, auto-launched on startup |

---

## 🔮 Future Enhancements

- [ ] Priority queue based Dijkstra — O(E log N) for larger networks
- [ ] A* algorithm implementation for comparison
- [ ] Real-time traffic data integration
- [ ] Multi-modal journey cost estimation
- [ ] Full Chennai MTC bus network (1000+ stops)
- [ ] JavaFX desktop GUI

---

## 📄 License

This project is submitted as an academic university project at Chennai Institute of Technology.  
© 2026 Ahmed Aadil. All rights reserved.
