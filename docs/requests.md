# Mye brukt `Requests` og `Response`

### Login

Request

```java
Request req = new Request(Request.Type.AUTH, User.class, loginInfo);
```

Response

```java
Reponse res = new Response(Request.Type.AUTH, User.class, user);
```

