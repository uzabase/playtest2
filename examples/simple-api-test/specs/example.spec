# Hello, world

* GETメソッドでパス"/ping"にリクエストを送る

## Assert response status code
* レスポンスのステータスコードが
* 整数値の"200"である

## Assert response headers
* レスポンスのヘッダーの
* キー"content-length"に対応する値が
* 文字列の"25"である

## Assert response headers contains some value
* レスポンスのヘッダーが
* 文字列の"content-length: 25"を含んでいる
* 文字列の"content-type: application/json"を含んでいる

## Assert response body
* レスポンスのボディが
* JSONのパス"$.message"に対応する値が
* 文字列の"pong"である

## Assert wiremock
* API"InnerAPI"についてメソッド"GET"でパス"/ping"に
* リクエストが送られた回数が
* 整数値の"1"である

## Assert table
* レスポンスのヘッダーが
* 以下のテーブルである
| key            | value           |
|----------------|-----------------|
| connection     | keep-alive      |
| content-length | 25              |
| content-type   | application/json|
