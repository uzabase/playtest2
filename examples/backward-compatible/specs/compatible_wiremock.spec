# Playtest1との互換性をテストする - WireMock

## API<apiName>のURL<url>に<callCount>回GETリクエストされた
* URL"/ping"にGETリクエストを送る
* API"ProxiedAPI"のURL"/ping"に"1"回GETリクエストされた

## API<apiName>のURL<url>に<callCount>回PUTリクエストされた
* URL"/doge"にPUTリクエストを送る
* API"ProxiedAPI"のURL"/doge"に"1"回PUTリクエストされた

## API<apiName>のURL<url>にヘッダー<header>で、GETリクエストされた
* URL"/pingWithHeader"にヘッダー"great-answer: 42"で、GETリクエストを送る
* API"ProxiedAPI"のURL"/pingWithHeader"にヘッダー"great-answer: 42"で、GETリクエストされた

## API<apiName>のURL<url>にヘッダー<header>で、PUTリクエストされた
* URL"/dogeWithHeader"にヘッダー"name: kabosu"で、PUTリクエストを送る
* API"ProxiedAPI"のURL"/dogeWithHeader"にヘッダー"name: kabosu"で、PUTリクエストされた