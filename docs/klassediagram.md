# Klassediagram

Eksempel på klassediagram:

| Kalender |
|----------|
|+ owner: User|
|+ meetings: List<Meeting>|

| Meeting |
|----------|
|+ description: String|
|+ room: Room|
|+ from: LocalDateTime|
|+ to: LocalDateTime|
|+ participant: List<User>|
|+ leader: User|

| User |
|----------|
|+ name: String|
|+ calendar: Kalender|
|+ group: List<Group>|
|+ username: String|
|+ password: String|

| Group |
|----------|
|+ name: String|
|+ members: List<Entity>|
|+ calendar: Kalender|

| Notifications |
|----------|
|+ user: User|
|+ meeting: Meeting|

| Room |
|----------|
|+ capacity: int|
|+ name: String|
|+ location: String|






