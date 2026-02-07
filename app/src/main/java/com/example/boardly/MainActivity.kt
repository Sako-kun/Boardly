package com.example.boardly

// Firebase関連の正しいインポート
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.boardly.ui.theme.BoardlyTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BoardlyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        BoardScreen() //ここで呼び出す画面を指定
                    }
                }
            }
        }
    }
}

@Composable //設計図：部品の定義
fun BoardScreen() {
    // Firebase Firestore を使うための窓口（インスタンス）を取得
    val db = Firebase.firestore
    var text by remember { mutableStateOf("") }

    // 1. 縦に並べる箱を作る
    Column(
        modifier = Modifier
            .fillMaxSize() // 画面いっぱいに広げる
            .padding(16.dp) // 四端に16dpの余白を作る
    ) {
        // 2. タイトルを表示
        Text(
            text = "Boardly Shared Note",
            style = MaterialTheme.typography.headlineMedium
        )

        // 3. 少し隙間をあける
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("ここにメッセージを入力…") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // 保存するデータ（Map形式。JavaのHashMapと同じ感覚です）
                val data = hashMapOf(
                    "content" to text,
                    "timestamp" to com.google.firebase.Timestamp.now()
                )

                // "messages" というフォルダの中に、"fixed_room" という名前で保存する
                db.collection("messages").document("fixed_room")
                    .set(data)
                    .addOnSuccessListener {
                        println("保存成功！")
                    }
                    .addOnFailureListener { e ->
                        println("エラー発生: $e")
                    }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("送信する")
        }
    }
}

@Preview(showBackground = true) //確認用：開発ツール上のプレビュー
@Composable
fun BoardlyPreview() {
    BoardlyTheme {
        BoardScreen()
    }
}