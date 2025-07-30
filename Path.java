// === Path.java ===
// 本来は駅のリストや所要時間などを保持する
// 今回はダミーとして、駅名のリストを文字列に変換する機能だけを持つ
import java.util.ArrayList;
public class Path {
  private ArrayList<String> stations;
  public Path(ArrayList<String> stations) {
    this.stations = stations;
  }
  // 結果を画面表示用の文字列に変換する
  public String getRouteText() {
    if (stations == null || stations.isEmpty()) {
      return "";
    }
    // "東京 -> 神田 -> 御茶ノ水 -> ..." のような文字列を作る
    return String.join(" -> ", stations);
  }
}
