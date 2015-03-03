# Nettverk

_for oversikt over klassene, se filene som ligger i `docs/klassediagram/`_


All kommunikasjon mellom klient og tjener skjer med `Request` og `Response`.
Disse klassene er ikke egentlig så veldig forskjellige, men det gjør det
enklere å skille mellom hvem av server og klient som ber om noe.

## Request

Den som sender en `Request` er den som ber om noe, f.eks. 'få brukeren med denne login-infoen',
eller 'få alle gruppene til denne brukeren'.
Førstnevnte `Request` kan se slik ut:

```java
Request req = new Request(Request.Type.AUTH, User.class, loginInfo);
```

Sistnevnte kan se slik ut:

```java
Request req = new Request(Request.Type.LIST, Calendar.class, user);
```

Merk at `model` feltet, her `Calendar.class`, ikke er klassen til `payload`, men klassen til
det vi skal få tilbake. Siden vi i tillegg har `Request.Type.LIST`, skal typen til dataen
vi får tilbake her være `List<Calendar>`.


## Response

For å svare på en `Request` sender man en `Response`. Svarene på requestene over
kan se slik ut:

```java
Response res = new Response(Response.Type.SUCCESS, User.class, user);
```

og

```java
Reponse res = new Response(Response.Type.SUCCESS, Calendar.class, list);
```


# Request og Reponse

Her skriver vi en __komplett__ liste over alle `Request`s og `Response`s som vi bruker.

Alle feilresponser er på denne formen:
```java
Response res = new Response(Response.Type.FAILURE, String.class, errMsg);
```

## Div

### Login

Request:
```java
Request req = new Request(Request.Type.POST, User.class, loginInfo);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, User.class, user);
```

### Logout

Request:
```java
// TODO: Finn på noe bedre enn dette!
Request req = new Request(Request.Type.AUTH, User.class, null);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, User.class, null);
```


### GET med id:

Gjelder for egentlig alle klasser:

Request:
```java
Request req = new Request(Request.Type.GET, <Klasse>.class, id);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, <Klasse>.class, item);
```

## Brukere

### Få alle brukere

Request:
```java
Request req = new Request(Request.Type.LIST, User.class, null);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, User.class, users);
```

### Få brukeren med en gitt id 

Request:
```java
Request req = new Request(Request.Type.GET, User.class, id);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, User.class, user);
```

## Kalender

### Få kalenderen med en gitt id

Request:
```java
Request req = new Request(Request.Type.GET, Calendar, id);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, Calendar, calendar);
```


### Få kalenderen til en bruker eller gruppe

Request:
```java
Request req = new Request(Request.Type.GET, Calendar.class, entity);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, Calendar.class, calendar);
```


## Grupper

### Få gruppen med en gitt id

Request:
```java
Request req = new Request(Request.Type.GET, Group, id);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, Group, group);
```


### Få gruppene til en bruker eller gruppe

Request:
```java
Request req = new Request(Request.Type.LIST, Group.class, entity);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, Group.class, listGroups);
```

## Møter

### Få møtet med en gitt id

Request:
```java
Request req = new Request(Request.Type.GET, Meeting.class, id);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, Meeting, meeting);
```

### Få alle møter til en bruker eller gruppe

Request:
```java
Request req = new Request(Request.Type.LIST, Meeting.class, entity);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, Meeting, listMeetings);
```

## Notifications

Disse er litt spesielle, fordi de sendes som en request _fra serveren_.

Request:
```java
Request req = new Requset(Requset.Type.POST, Notification.class, notification);
```
Response:
```java
// TODO: fiks:
// trenger vi egentlig noen?
// Vi kan sende bekreftelse om at vi har fått den, og alt er good. Hvis serveren
// ikke får en respons, kan den lagre notifikasjonen, og sende den neste gang 
// brukeren får kontakt med serveren.
```


