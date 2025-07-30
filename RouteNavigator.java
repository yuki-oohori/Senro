import java.util.*;
import processing.data.*; 
import processing.core.PApplet;// ProcessingのJSONObject/JSONArrayを使う

public class RouteNavigator {
  private HashMap<String, ArrayList<Connection>> graph = new HashMap<>();

  public RouteNavigator(PApplet p,String jsonPath) {
    System.out.println("RouteNavigatorが初期化されました: " + jsonPath);
    try {
      loadGraphFromJson(p,jsonPath);
    } catch (Exception e) {
      System.err.println("線路データの読み込みに失敗: " + e.getMessage());
    }
  }

  private void loadGraphFromJson(PApplet p,String path) {
    JSONObject json = p.loadJSONObject(path);  // ← Processingの関数
    JSONArray stations = json.getJSONArray("stations");

    for (int i = 0; i < stations.size(); i++) {
      JSONObject station = stations.getJSONObject(i);
      String name = station.getString("name");
      JSONArray connections = station.getJSONArray("connections");

      ArrayList<Connection> connList = new ArrayList<>();
      for (int j = 0; j < connections.size(); j++) {
        JSONObject conn = connections.getJSONObject(j);
        connList.add(new Connection(
          conn.getString("destination"),
          conn.getString("line"),
          conn.getInt("time")
        ));
      }

      graph.put(name, connList);
    }

    System.out.println("駅データの読み込み完了: " + graph.size() + "駅");
  }

  public Path navigate(String start, String end) {
    if (!graph.containsKey(start) || !graph.containsKey(end)) {
      return new Path(new ArrayList<String>());
    }

    Queue<List<String>> queue = new LinkedList<>();
    Set<String> visited = new HashSet<>();

    queue.add(Arrays.asList(start));

    while (!queue.isEmpty()) {
      List<String> path = queue.poll();
      String current = path.get(path.size() - 1);

      if (current.equals(end)) {
        return new Path(new ArrayList<>(path));
      }

      if (!visited.contains(current)) {
        visited.add(current);
        for (Connection conn : graph.get(current)) {
          if (!visited.contains(conn.destination)) {
            List<String> newPath = new ArrayList<>(path);
            newPath.add(conn.destination);
            queue.add(newPath);
          }
        }
      }
    }

    return new Path(new ArrayList<String>());
  }

  class Connection {
    String destination;
    String line;
    int time;

    Connection(String destination, String line, int time) {
      this.destination = destination;
      this.line = line;
      this.time = time;
    }
  }
}
