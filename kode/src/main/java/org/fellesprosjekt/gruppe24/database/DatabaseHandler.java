package org.fellesprosjekt.gruppe24.database;

import java.util.List;

public abstract class DatabaseHandler<T> {
    /**
     * Lagrer et objekt i databasen. Id-en _skal_ være unik.
     *
     * I stedet for å returnere Id-en i funksjonen, kan vi heller
     * sette Id-en på objektet selv, og returnere hele greia.
     * Med dette gir vi serveren (eller andre evt. brukere av DBen)
     * mulighet til å bestemme selv hva de vil gjøre.
     *
     * @param t Objektet som skal lagres.
     * @return Objektet (inkl. id!) hvis vellykket. `null` hvis ikke.
     */
    public abstract T insert(T t);

    /**
     * Henter objektet med tilhørende id.
     * @param id Id-en til objektet.
     * @return Objektet hvis vellykket. `null` hvis ikke.
     */
    public abstract T get(int id);

    /**
     * Henter alle objekter av typen T.
     * @return En  liste med alle objektene.
     */
    public abstract List<T> getAll();

    /**
     * Sletter objektet fra databasen.
     * @param t Objektet som skal slettes.
     * @return `true` hvis det ble slettet, `false` hvis ikke.
     */
    public abstract boolean delete(T t);
    
    /**
     * Oppdaterer verdiene for det gitte objektet i databasen
     * @param t Objektet som skal oppdateres
     * @return <b>true</b> hvis oppdateringen var vellykket. <b>false</b> dersom en feil oppstod.
     */
    public abstract boolean update(T t);

}
