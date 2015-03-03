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
eller server kan gjøre noe med, annet enn å printe dem ut).


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
req.type = Request.Type.POST;
req.payload = loginInfo;
```
response:
```java
Response res = new Response();
res.type = Reponse.Type.OK;
res.payload = user;
```

### Logge ut:

```java
LogoutRequest req = new LogoutRequest();
req.type = Request.Type.POST;
```
(trenger vi å vente på respons?)


### Få én bruker:

request:
```java
UserRequest req = new UserRequest();
req.type = Request.Type.GET;
req.payload = id;
```
response:
```java
Response res = new Response();
res.type = Response.Type.OK;
res.payload = user;
```


### Få alle brukere:

request:
```java
UserRequest req = new UserRequest();
req.type = Request.Type.LIST;
```
response:
```java
Response res = new Response();
res.type = Response.Type.OK;
res.payload = users;
```

### Få en brukers grupper

request:
```java
GroupRequest req = new GroupRequest();
req.type = Request.Type.LIST;
req.payload = user;
```
reseponse:
```java
Response res = new Response();
res.type = Response.Type.OK;
res.payload = groups;
```

### Få en gruppes medlemmer

request:
```java
UserRequest req = new UserRequest();
req.type = Request.Type.LIST;
req.payload = group;
```
reseponse:
```java
Response res = new Response();
res.type = Response.Type.OK;
res.payload = entities;
```

### Server sender en `Notification`:

_merk at det her er serveren som sender requesten._

request:
```java
NotificationRequest req = new NotificationRequest();
req.type = Request.Type.POST;
req.payload = notification;
```
response:
```java
Response res = new Reponse();
res.type = Reponse.Type.OK;
```



# Problemer

 - Hva skal en klient sende til serveren for å melde om deltakelse på et møte?
  - det er rart å poste tilbake en notification, men det er også rart å poste 
  et helt event der man har lagt til seg selv under `participants`.
  - Det går an å bare markede den som lest når den er sendt.



