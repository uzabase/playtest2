# Hello, world

* GETメソッドでパス"/ping"にリクエストを送る

## Assert response status code
* レスポンスのステータスコードが
* 整数値の"200"である

## Assert response headers
* レスポンスのヘッダーの
* キー"Content-Length"に対応する値が
* 文字列の"4"である

## Assert response headers contains some value
* レスポンスのヘッダーの
* 文字列の"Content-Length: 4"を含んでいる

## Assert wiremock
* API"InnerAPI"のパス"/ping"に
* ("InnerAPI"に)GETリクエストされた