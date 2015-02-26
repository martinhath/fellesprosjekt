# Klassediagram

| Entity |
|--------|
|+ name: `String`|
|+ calendar: `Calendar`

| Calendar |
|----------|
|+ owner: `User`|
|+ meetings: `List<Meeting>`|

| Meeting |
|----------|
|+ description: `String`|
|+ room: `Room`|
|+ from: `LocalDateTime`|
|+ to: `LocalDateTime`|
|+ participant: `List<User>`|
|+ leader: `User`|

| User : Entity|
|----------|
|+ group: `List<Group>`|
|+ username: `String`|
|+ password: `String`|
|**Funksjoner**|
|+ `bool verifyPassword(String)`|

| Group : Entity|
|----------|
|+ members: `List<Entity>`|

| Notifications |
|----------|
|+ user: `User`|
|+ meeting: `Meeting`|

| Room |
|----------|
|+ capacity: `int`|
|+ name: `String`|
|+ location: `String`|

