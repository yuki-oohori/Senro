import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 他のRouteFinderを装飾（デコレート）し、不通区間を避ける機能を追加するクラス
 */
public class AvoidanceRouteFinder implements RouteFinder {

    private final RouteFinder decoratedFinder; // 対象のRouteFinder
    private final List<Connection> unavailableConnections; // 避けるべき不通区間のリスト

    /**
     * コンストラクタ
     * @param decoratedFinder 時間最短や乗換最小など、基本となる探索アルゴリズム
     * @param unavailableConnections 不通区間のリスト
     */
    public AvoidanceRouteFinder(RouteFinder decoratedFinder, List<Connection> unavailableConnections) {
        this.decoratedFinder = decoratedFinder;
        this.unavailableConnections = unavailableConnections;
    }

    @Override
    public Path findRoute(Station start, Station end, Map<String, Station> graph) {
        System.out.println("<< 不通区間を考慮 >>");
        
        // 探索を実行する前に、グラフデータから不通区間を一時的に除外する
        // ※注意：この方法は元のgraphオブジェクトの状態を直接変更してしまいます。
        // 本来はグラフのディープコピーを作成して処理するのが安全ですが、ここでは説明を簡潔にするため、
        // try-finallyブロックで確実に元の状態に戻す方法を採ります。

        // どの駅のどの接続を無効にしたかを覚えておくリスト
        List<Map.entry<Station, Connection>> removedConnections = new ArrayList<>();
        
        try {
            // 不通区間に指定された接続を、グラフから一時的に削除
            for (Connection unavailable : unavailableConnections) {
                // 接続の出発点となる駅を探す
                for (Station station : graph.values()) {
                    if (station.getConnections().contains(unavailable)) {
                        station.getConnections().remove(unavailable);
                        removedConnections.add(Map.entry(station, unavailable)); // 元に戻すために記録
                        break;
                    }
                }
            }

            // 不通区間が除外されたグラフを使って、内包している探索アルゴリズムを実行
            return decoratedFinder.findRoute(start, end, graph);

        } finally {
            // 探索が終わったら、変更したグラフを必ず元の状態に戻す
            for (Map.entry<Station, Connection> entry : removedConnections) {
                entry.getKey().getConnections().add(entry.getValue());
            }
            System.out.println("<< グラフの状態を元に戻しました >>");
        }
    }
}