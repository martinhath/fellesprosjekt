# Rework av API

Første utkast til API ble rotete og lite intuitivt. Her er forsøk nr. 2.


## Request og Response

Som i forige API har vi `Request`-objekter og `Response`-objekter.
Ulikt fra forige API, så er disse abstrakte, og vi subklasser dem, til
f.eks. `LoginRequest`, eller `UserRequest`.

| Request |
|---------|
| + type: `Request.Type` |
| + payload : `Object` |

| Request.Type |
|--------------|
| POST |
| PUT |
| GET |
| LIST | 

| Response |
|----------|
| + type: `Response.Type` |
| + payload: `Object` |

| Response.Type |
|---------------|
| OK |
| FAIL |

Dersom `type == Reponse.Type.FAIL`, __skal__ `payload` være en streng, og helst inneholde
en menneskelig lesbar feilmelding (vi velger altså å ikke støtte feilmeldinger som klient
eller server kan gjøre noe med).


### Beskrivelse

APIet er forsatt 'noe' HTTP inspirert. Vi bruker `POST` for å sende informasjon,
uten den antakelsen at det nødvendigvis skal bli lagret, eksempelvis ved innloggin.
Dersom vi gir informasjon som _skal_ bli lagret, bruker vi `PUT`.
For å få et bestemt objekt, bruker vi `GET`, og sender med en `id` som `payload`.

Fordelen med dette designet over det gamle, er at subklassen `MeetingRequest` kan se slik ut:
```java
MeetingRequest req = new MeetingRequest();
req.payload = meeting;
```


# Eksempler:

_merk: ikke fullstendig API referanse_

### Logge inn:
```java
LoginRequest req = new LoginRequest();
req.setType(Request.Type.POST);
req.setPayload(loginInfo);
```
Deretter vil klienten vente på repons fra serveren, som skal inneholde
```java
Response res = new Response();
res.setType(Reponse.Type.OK);
res.setPayload(user);
```

### Logge ut:
```java
LogoutRequest req = new LogoutRequest();
req.setType(Request.Type.POST);
```
(trenger vi å vente på respons?)


### Få alle brukere:
request:
```java
Request req = new Request();
req.setType(Request.Type.LIST);
```
response:
```java
Response res = new Response();
res.setType(Response.Type.OK);
res.setPayload(users);
```


### Server sender en `Notification`:

_merk at det her er serveren som sender requesten._

request:
```java
NotificationRequest req = new NotificationRequest();
req.setType(Request.Type.POST);
req.setPayload(notification);
```
response:
```java
Response res = new Reponse();
res.setType(Reponse.Type.OK);
```



# Problemer

 - Hva skal en klient sende til serveren for å melde om deltakelse på et møte?



