// === EkiNaviApp.pde ===
// Java側で定義したクラスのオブジェクトを生成
RouteNavigator navigator;/////////
PFont font;
// UI部品の位置やサイズを管理する変数
int startX = 50, startY = 50, fieldW = 200, fieldH = 30;
int endX = 50, endY = 100;
int buttonX = 50, buttonY = 150, buttonW = 100, buttonH = 40;
// テキスト入力用の変数
String startStationText = "";
String endStationText = "";
// どちらのテキストフィールドがアクティブか
int activeField = 0; // 0:なし, 1:出発, 2:目的
// 探索結果を保持する変数
Path resultPath = null;
void setup() {
  size(600, 400);
  font = loadFont("MS-Gothic-16.vlw");  // 作成したフォント名に合わせて
  textFont(font);
  String path= sketchPath("線路グラフ実験.json");
  // Javaの計算クラスを初期化
  navigator = new RouteNavigator(this,path);
  
}
void draw() {
  background(240);
  // --- UIの描画 ---
  drawUI();
  // --- 結果の描画 ---
  drawResult();
}
// マウスがクリックされたときの処理
void mousePressed() {
  // 出発駅のテキストフィールドがクリックされたか
  if (mouseX > startX && mouseX < startX + fieldW && mouseY > startY && mouseY < startY + fieldH) {
    activeField = 1;
  }
  // 目的駅のテキストフィールドがクリックされたか
  else if (mouseX > endX && mouseX < endX + fieldW && mouseY > endY && mouseY < endY + fieldH) {
    activeField = 2;
  }
  // 検索ボタンがクリックされたか
  else if (mouseX > buttonX && mouseX < buttonX + buttonW && mouseY > buttonY && mouseY < buttonY + buttonH) {
    activeField = 0;
    // Javaのメソッドを呼び出して経路探索を依頼！
    println("検索中: " + startStationText + " -> " + endStationText);
    resultPath = navigator.navigate(startStationText, endStationText);
  }
  // それ以外の場所がクリックされたら、アクティブ状態を解除
  else {
    activeField = 0;
  }
}
// キーボードが押されたときの処理
void keyPressed() {
  if (activeField == 0) return; // テキスト入力欄が選択されていなければ何もしない
  String targetText = (activeField == 1) ? startStationText : endStationText;
  if (key == BACKSPACE) {
    if (targetText.length() > 0) {
      targetText = targetText.substring(0, targetText.length() - 1);
    }
  } else if (key != CODED) { // Shift, Altなどの特殊キーは無視
    targetText += key;
  }
  // 変更を反映
  if (activeField == 1) {
    startStationText = targetText;
  } else {
    endStationText = targetText;
  }
}
// UIを描画するヘルパー関数
void drawUI() {
  textSize(16);
  fill(0);
  textAlign(LEFT, CENTER);
  text("出発駅", startX, startY - 20);
  text("目的駅", endX, endY - 20);
  // テキストフィールドの描画
  drawTextField(startX, startY, fieldW, fieldH, startStationText, (activeField == 1));
  drawTextField(endX, endY, fieldW, fieldH, endStationText, (activeField == 2));
  // ボタンの描画
  fill(150, 180, 220);
  rect(buttonX, buttonY, buttonW, buttonH, 5);
  fill(0);
  textAlign(CENTER, CENTER);
  text("経路検索", buttonX + buttonW/2, buttonY + buttonH/2);
}
// テキストフィールドを描画する部品
void drawTextField(int x, int y, int w, int h, String txt, boolean isActive) {
  stroke(0);
  fill(255);
  rect(x, y, w, h);
  if (isActive) {
    stroke(0, 150, 255); // アクティブな枠は青く
    strokeWeight(2);
    noFill();
    rect(x, y, w, h);
    strokeWeight(1);
  }
  fill(0);
  textAlign(LEFT, CENTER);
  text(txt, x + 5, y + h/2);
  // カーソルの点滅
  if (isActive && frameCount % 60 < 30) {
    float cursorX = x + 5 + textWidth(txt);
    line(cursorX, y + 5, cursorX, y + h - 5);
  }
}
// 結果を描画するヘルパー関数
void drawResult() {

  textSize(20);
  fill(0);
  textAlign(LEFT, TOP);
  if (resultPath != null) {
    if (resultPath.getRouteText().isEmpty()) {
      fill(200, 0, 0);
      text("経路が見つかりませんでした。", startX, 220);
    } else {
      text("探索結果:", startX, 220);
      textSize(16);
      text(resultPath.getRouteText(), startX, 250);
    }
  } else {
    text("駅名を入力して検索してください。", startX, 220);
  }
}
