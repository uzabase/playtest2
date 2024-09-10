# Playtest1との互換性をテストする - HTTP

## URL<url>にGETリクエストを送る
* URL"/ping"にGETリクエストを送る
* レスポンスステータスコードが"200"である

## URL<url>にヘッダー<header>で、GETリクエストを送る
* URL"/pingWithHeader"にヘッダー"great-answer: 42"で、GETリクエストを送る
* レスポンスステータスコードが"200"である

## URL<url>にPUTリクエストを送る
* URL"/doge"にPUTリクエストを送る
* レスポンスステータスコードが"204"である

## URL<url>にヘッダー<header>で、PUTリクエストを送る
* URL"/dogeWithHeader"にヘッダー"name: kabosu"で、PUTリクエストを送る
* レスポンスステータスコードが"204"である

## レスポンスステータスコードが<statusCode>である
* URL"/ping"にGETリクエストを送る
* レスポンスステータスコードが"200"である