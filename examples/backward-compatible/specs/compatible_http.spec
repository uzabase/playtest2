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

## URL<url>にボディ<requestBody>、ヘッダー<header>で、PUTリクエストを送る
* URL"/dogeWithJsonAndHeader"にボディ"{\"bark\": \"bow-wow\"}"、ヘッダー"name: kabosu"で、PUTリクエストを送る
* レスポンスステータスコードが"204"である

## URL<url>にボディ<requestBody>で、PUTリクエストを送る
* URL"/dogeWithJson"にボディ"{\"bark\": \"bow-wow\"}"で、PUTリクエストを送る
* レスポンスステータスコードが"204"である

## レスポンスステータスコードが<statusCode>である
* URL"/ping"にGETリクエストを送る
* レスポンスステータスコードが"200"である

## URL<url>にPOSTリクエストを送る
* URL"/doge"にPOSTリクエストを送る
* レスポンスステータスコードが"201"である

## URL<url>にヘッダー<header>で、POSTリクエストを送る
* URL"/dogeWithHeader"にヘッダー"name: kabosu"で、POSTリクエストを送る
* レスポンスステータスコードが"201"である

## URL<url>にボディ<requestBody>、ヘッダー<header>で、POSTリクエストを送る
* URL"/dogeWithHeaderAndJson"にボディ"{\"message\": \"funny\"}"、ヘッダー"name: kabosu"で、POSTリクエストを送る
* レスポンスステータスコードが"201"である

## URL<url>にボディ<requestBody>で、POSTリクエストを送る
* URL"/dogeWithJson"にボディ"{\"message\":\"funny\"}"で、POSTリクエストを送る
* レスポンスステータスコードが"201"である

