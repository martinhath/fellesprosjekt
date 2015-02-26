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
Request req = new Request(Request.Type.GET, User.class, login_info);
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
