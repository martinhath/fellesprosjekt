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

Her skriver vi en _komplett_ liste over alle `Request`s og `Response`s som vi bruker.

## Div

### Login

Request:
```java
Requset req = new Request(Request.Type.AUTH, User.class, loginInfo);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, User.class, user);
// eller
Response res = new Response(Response.Type.FAILURE, User.class, null);
```

### Logout

Request:
```java
Requset req = new Request(Request.Type.AUTH, User.class, null); // er dette litt vel jalla?
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, User.class, null);
// eller
Response res = new Response(Response.Type.FAILURE, User.class, user);
```


### GET med id:

Gjelder for egentlig alle klasser:

Request:
```java
Requset req = new Request(Request.Type.GET, <Klasse>.class, id);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, <Klasse>.class, item);
// eller
Response res = new Response(Response.Type.FAILURE, <Klasse>.class, null);
```


## Kalender

### Få kalender til en bruker

Request:
```java
Requset req = new Request(Request.Type.GET, Calendar.class, user);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, Calendar.class, calendar);
// eller
Response res = new Response(Response.Type.FAILURE, Calendar.class, null);
```


## Grupper

### Få gruppene til en bruker

Request:
```java
Requset req = new Request(Request.Type.LIST, Group.class, user);
```
Response:
```java
Response res = new Response(Response.Type.SUCCESS, Group.class, listGroups);
// eller
Response res = new Response(Response.Type.FAILURE, Group.class, null);
```

