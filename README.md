#HALLLO!!

Gruppe 24 er best! 


# Hva er ting!?!

 - `docs/` inneholder dokumentasjon.
 - `kode/` inneholder all server- og klientkode.


## Hvordan bygger jeg prosjektet? 

For å bygge prosjektet trenger du Maven. Dette er mest sansynlig inkludert i ditt IDE (som enten er Eclipse, eller IntelliJ).
Her er akkurat hva du må gjøre for å på ting til å funke:

 - File -> Import
 - Under Maven, velg `Existing Maven Projects`
 - Trykk `Browse`, og velg finn frem til `kode` mappen.
 - Nå vil `/pom.xml` være markert i listen. Trykk Finish.

Dette er grunnen til at vi bruker maven. Først må vi sette opp en `run configuration`.

 - Run -> Run Configuration
 - Gå ned til Maven Build, og trykk `new` (øverst til venstre)
 - For `Base directory`, trykk `Browse Workspace`, og trykk `Ok` (ja, med en gang). 
 - Skriv `package` under Goals.
 - Trykk `Apply`, og `Run`. 
 - Nå skal du bygge `.jar`s. Smud.

Merk at ingen ting kjøres. `.jar` filene ligger under `kode/target/`, og heter noe liknende `server-jar-with-dependencies.jar` og `client-jar-with-dependencies.jar`.
Hvis du er i terminalen, og i `fellesprosjekt/kode/`, kan du skrive `java -jar target/server-with-dependencies.jar`.

