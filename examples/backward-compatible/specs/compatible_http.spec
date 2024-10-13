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

## URL<url>にDELETEリクエストを送る
* URL"/doge"にDELETEリクエストを送る
* レスポンスステータスコードが"204"である

## URL<url>にヘッダー<header>で、DELETEリクエストを送る
* URL"/dogeWithHeader"にヘッダー"name: kabosu"で、DELETEリクエストを送る
* レスポンスステータスコードが"204"である

## レスポンスのJSONの<jsonPath>が<pattern>である
* URL"/bigBallOfMud"にGETリクエストを送る
* レスポンスのJSONの"$.name"が"B\\w{2} B\\w{3} of M\\w{2}"である

レスポンスのJSONの<jsonPath>がnullである
レスポンスのJSONの<jsonPath>が小数の<expected>である

## レスポンスのJSONの<jsonPath>が真偽値の<expected>である
* URL"/bigBallOfMud"にGETリクエストを送る
* レスポンスのJSONの"$.primes[0]"が真偽値の"false"である
* レスポンスのJSONの"$.primes[1]"が真偽値の"true"である

## レスポンスのJSONの<jsonPath>が真偽値のfalseである
* URL"/bigBallOfMud"にGETリクエストを送る
* レスポンスのJSONの"$.primes[0]"が真偽値のfalseである

## レスポンスのJSONの<jsonPath>が真偽値のtrueである
* URL"/bigBallOfMud"にGETリクエストを送る
* レスポンスのJSONの"$.primes[1]"が真偽値のtrueである

## レスポンスのJSONの<jsonPath>が整数の<expected>である
* URL"/bigBallOfMud"にGETリクエストを送る
* レスポンスのJSONの"$.serialNumber"が整数の"42"である

レスポンスのJSONの<jsonPath>が存在しない

## レスポンスのJSONの<jsonPath>が文字列の<expected>である
* URL"/bigBallOfMud"にGETリクエストを送る
* レスポンスのJSONの"$.name"が文字列の"Big Ball of Mud"である

レスポンスのJSONの<jsonPath>の配列に、Key<key>の値が<value>である要素が存在する
レスポンスのJSONの<jsonPath>の配列の、UniqueKey<uniqueKey>の値が<filterValue>である要素の<key>が、小数値の<expected>である
レスポンスのJSONの<jsonPath>の配列の、UniqueKey<uniqueKey>の値が<filterValue>である要素の<key>が、真偽値の<expected>である
レスポンスのJSONの<jsonPath>の配列の、UniqueKey<uniqueKey>の値が<filterValue>である要素の<key>が、整数値の<expected>である
レスポンスのJSONの<jsonPath>の配列の、UniqueKey<uniqueKey>の値が<filterValue>である要素の<key>が、文字列の<expected>である
レスポンスのJSONの<jsonPath>の配列の長さが<length>である
レスポンスのJSONの配列<arrayJsonPath>が、タイムゾーン付きの日付/時間<orderKey>の降順に並んでいる
レスポンスのJSONの配列<arrayJsonPath>が、タイムゾーン付きの日付/時間<orderKey>の昇順に並んでいる
レスポンスのJSONの配列<arrayJsonPath>が、数値<orderKey>の降順に並んでいる
レスポンスのJSONの配列<arrayJsonPath>が、数値<orderKey>の昇順に並んでいる
レスポンスのJSONの配列<jsonPath>に、小数値<value>が存在しない
レスポンスのJSONの配列<jsonPath>に、小数値<value>が存在する
レスポンスのJSONの配列<jsonPath>に、整数値<value>が存在しない
レスポンスのJSONの配列<jsonPath>に、整数値<value>が存在する
レスポンスのJSONの配列<jsonPath>に、文字列<value>が存在しない
レスポンスのJSONの配列<jsonPath>に、文字列<value>が存在する


## レスポンスステータスコードが<statusCode>である
* URL"/ping"にGETリクエストを送る
* レスポンスステータスコードが"200"である

## レスポンスヘッダーに<key>が存在し、その値が<value>である
* URL"/ping"にGETリクエストを送る
* レスポンスヘッダーに"content-type"が存在し、その値が"application/json"である