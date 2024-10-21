# ![logo](logo.png)

[![Tests](https://github.com/uzabase/playtest2/actions/workflows/test.yaml/badge.svg)](https://github.com/uzabase/playtest2/actions/workflows/test.yaml)
[![JitPack](https://jitpack.io/v/uzabase/playtest2.svg)](https://jitpack.io/#uzabase/playtest2)
[![Maven Central Version](https://img.shields.io/maven-central/v/com.uzabase.playtest2/playtest2)](https://central.sonatype.com/artifact/com.uzabase.playtest2/playtest2)

## Installation

テストプロジェクトに必要な依存関係を記述します。Playtest2では、現在以下のモジュールを提供しています。

| モジュール名            | 必須  | 説明                                    |
|-------------------|-----|---------------------------------------|
| playtest-core     | [x] | Playtest2の設定やアサーションのためのモジュール          |
| playtest-http     | [ ] | HTTPリクエストを送信して、レスポンスを検証可能にするモジュール     |
| playtest-wiremock | [ ] | WireMockを使って外部APIへのリクエストを検証可能にするモジュール |

## Usage

Playtest2では設定のためのDSLを提供しています。`playtest2`関数に各モジュールが提供する設定関数(`http`, `wireMock`など)で設定を追加していくことで、Playtest2が提供するステップを利用することができるようになります。

```kotlin
import com.uzabase.playtest2.core.config.Configuration.Companion.playtest2
import com.uzabase.playtest2.core.config.plus
import com.uzabase.playtest2.http.config.http
import com.uzabase.playtest2.wiremock.config.wireMock

class Hooks {
    @BeforeSuite
    fun beforeSuite() {
        playtest2 {
            http(URI("http://localhost:8080").toURL()) +
            wireMock("InnerAPI", URI("http://localhost:3000").toURL())
        }
    }
}
```

Playtest2が提供するステップを利用すると以下のようにGaugeのスペック(`.spec`)を記述することができます。

```example.spec
## /pingに対するテスト
* パス"/ping"に
* メソッド"GET"で
* メディアタイプ"application/json"で
* リクエストを送る
* レスポンスのステータスコードが
* 整数値の"200"である
```

Gaugeのコンセプト(`.cpt`)に利用することもできます。

```example.cpt
# GETメソッドでパス<path>にリクエストを送る
* パス<path>に
* メソッド"GET"で
* メディアタイプ"application/json"で
* リクエストを送る
```