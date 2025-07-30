import java.util.ArrayList;

public class Station {
    private String name;
    private ArrayList<Connection> connections;

    public Station(String name) {
        this.name = name;
        this.connections = new ArrayList<>(); // 初期化時に空のリストを作成
    }

    // この駅から出発する接続情報を追加するメソッド
    public void addConnection(Connection connection) {
        this.connections.add(connection);
    }

    // 駅名を取得する
    public String getName() {
        return this.name;
    }

    // 接続情報のリストを取得する
    public ArrayList<Connection> getConnections() {
        return this.connections;
    }
}