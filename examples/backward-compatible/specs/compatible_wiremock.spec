# Playtest1との互換性をテストする - WireMock

## API<apiName>のURL<url>に<callCount>回GETリクエストされた
* URL"/ping"にGETリクエストを送る
* API"ProxiedAPI"のURL"/ping"に"1"回GETリクエストされた

## API<apiName>のURL<url>に<callCount>回POSTリクエストされた
* URL"/dogeWithJson"にPOSTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithJson"に"1"回POSTリクエストされた

## API<apiName>のURL<url>に<callCount>回PUTリクエストされた
* URL"/doge"にPUTリクエストを送る
* API"ProxiedAPI"のURL"/doge"に"1"回PUTリクエストされた

## API<apiName>のURL<url>に<callCount>回DELETEリクエストされた
* URL"/doge"にDELETEリクエストを送る
* API"ProxiedAPI"のURL"/doge"に"1"回DELETEリクエストされた

## API<apiName>のURL<url>にヘッダー<header>で、GETリクエストされた
* URL"/pingWithHeader"にヘッダー"great-answer: 42"で、GETリクエストを送る
* API"ProxiedAPI"のURL"/pingWithHeader"にヘッダー"great-answer: 42"で、GETリクエストされた

## API<apiName>のURLパス<urlPath>にクエリパラメータ<queryParameterName>が<queryParameterValue>でGETリクエストされた
* URL"/dogeWithQuery?name=kabosu"にGETリクエストを送る
* API"ProxiedAPI"のURLパス"/dogeWithQuery"にクエリパラメータ"name"が"kabosu"でGETリクエストされた

## API<apiName>のURL<url>にボディ<json>でPOSTリクエストされた
* URL"/dogeWithJson"にボディ"{\"message\":\"funny\"}"で、POSTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithJson"にボディ"{\"message\":\"funny\"}"でPOSTリクエストされた

## API<apiName>のURL<url>にボディ<json>、ヘッダー<header>で、POSTリクエストされた
* URL"/dogeWithHeaderAndJson"にボディ"{\"message\": \"funny\"}"、ヘッダー"name: kabosu"で、POSTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithHeaderAndJson"にボディ"{\"message\": \"funny\"}"、ヘッダー"name: kabosu"で、POSTリクエストされた

## API<apiName>のURL<url>にヘッダー<header>で、POSTリクエストされた
* URL"/dogeWithJson"にボディ"{\"message\":\"funny\"}"で、POSTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithJson"にボディ"{\"message\":\"funny\"}"でPOSTリクエストされた

## API<apiName>のURL<url>にパス<jsonPath>に文字列<value>を持つJSONでPOSTリクエストされた
* URL"/dogeWithJson"にボディ"{\"message\":\"funny\"}"で、POSTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithJson"にパス"$.message"に文字列"funny"を持つJSONでPOSTリクエストされた

## API<apiName>のURL<url>にヘッダー<header>で、PUTリクエストされた
* URL"/dogeWithHeader"にヘッダー"name: kabosu"で、PUTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithHeader"にヘッダー"name: kabosu"で、PUTリクエストされた