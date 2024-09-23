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

## API<apiName>のURL<url>にボディ<json>でPUTリクエストされた
* URL"/dogeWithJson"にボディ"{\"bark\":\"bow-wow\"}"で、PUTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithJson"にボディ"{\"bark\":\"bow-wow\"}"でPUTリクエストされた

## API<apiName>のURL<url>にボディ<json>、ヘッダー<header>で、PUTリクエストされた
* URL"/dogeWithHeaderAndJson"にボディ"{\"bark\":\"bow-wow\"}"、ヘッダー"name: kabosu"で、PUTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithHeaderAndJson"にボディ"{\"bark\":\"bow-wow\"}"、ヘッダー"name: kabosu"で、PUTリクエストされた

## API<apiName>のURL<url>にヘッダー<header>で、PUTリクエストされた
* URL"/dogeWithHeader"にヘッダー"name: kabosu"で、PUTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithHeader"にヘッダー"name: kabosu"で、PUTリクエストされた

## API<apiName>のURL<url>にパス<jsonPath>に文字列<value>を持つJSONでPUTリクエストされた
* URL"/dogeWithJson"にボディ"{\"bark\":\"bow-wow\"}"で、PUTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithJson"にパス"$.bark"に文字列"bow-wow"を持つJSONでPUTリクエストされた

## API<apiName>のURL<url>にヘッダー<header>で、DELETEリクエストされた
* URL"/dogeWithHeader"にヘッダー"name: kabosu"で、DELETEリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithHeader"にヘッダー"name: kabosu"で、DELETEリクエストされた

## API<apiName>のURL<url>にGETリクエストされた
* URL"/ping"にGETリクエストを送る
* URL"/ping"にGETリクエストを送る
* API"ProxiedAPI"のURL"/ping"にGETリクエストされた

## API<apiName>のURL<url>にGETリクエストされていない
* URL"/ping"にGETリクエストを送る
* API"ProxiedAPI"のURL"/pong"にGETリクエストされていない

## API<apiName>のURL<url>にPOSTリクエストされた
* URL"/doge"にPOSTリクエストを送る
* URL"/doge"にPOSTリクエストを送る
* API"ProxiedAPI"のURL"/doge"にPOSTリクエストされた

## API<apiName>のURL<url>にPOSTリクエストされていない
* URL"/doge"にPOSTリクエストを送る
* API"ProxiedAPI"のURL"/dog"にPOSTリクエストされていない

## API<apiName>のURL<url>にPUTリクエストされた
* URL"/doge"にPUTリクエストを送る
* URL"/doge"にPUTリクエストを送る
* API"ProxiedAPI"のURL"/doge"にPUTリクエストされた

## API<apiName>のURL<url>にPUTリクエストされていない
* URL"/doge"にPUTリクエストを送る
* API"ProxiedAPI"のURL"/dog"にPUTリクエストされていない

## API<apiName>のURL<url>にDELETEリクエストされた
* URL"/doge"にDELETEリクエストを送る
* URL"/doge"にDELETEリクエストを送る
* API"ProxiedAPI"のURL"/doge"にDELETEリクエストされた

## API<apiName>のURL<url>にDELETEリクエストされていない
* URL"/doge"にDELETEリクエストを送る
* API"ProxiedAPI"のURL"/dog"にDELETEリクエストされていない
