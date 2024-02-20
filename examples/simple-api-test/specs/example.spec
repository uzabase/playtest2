# Hello, world

* GETメソッドでパス"/ping"にリクエストを送る

## Assert response status code
* レスポンスのステータスコードが
* 整数値の"200"である

## Assert response headers
* レスポンスのヘッダーの
* キー"Content-Length"に対応する値が
* 文字列の"25"である

## Assert response headers contains some value
* レスポンスのヘッダーの
* 文字列の"Content-Length: 25"を含んでいる
* 文字列の"Content-Type: application/json"を含んでいる

## Assert response body
* レスポンスのボディが
* JSONのパス"$.message"に対応する値が
* 文字列の"pong"である

## Assert wiremock
* API"InnerAPI"のパス"/ping"に
* ("InnerAPI"に)GETリクエストされた