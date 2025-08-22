### Fernaufrufsystem (Remote Method Invocation)

* Methodenaufrufe sollen nicht mehr lokal, sondern auf einem entfernten Rechner ausgeführt werden.
* Am Client übernimmt ein **Stub** als Platzhalter die Methode und wandelt sie in eine Nachricht um.
* Die Nachricht wird an den Zielrechner gesendet.
* Dort übernimmt ein **Skeleton** als Stellvertreter für den Client die Ausführung.
* Das Ergebnis wird an den Client zurückgeschickt.

---

### Auktionsdienst

* Bietet folgende Funktionen für den Client:

  * Neue Auktion registrieren.
  * Laufende Auktionen abfragen.
  * Gebote auf laufende Auktionen abgeben.
* Klasse **VSAuction**:

  * Repräsentiert eine Auktion.
  * Enthält Auktionsnamen und aktuelles Höchstgebot.
* **Eventhandler** kann mitgegeben werden, um über:

  * Neue Gebote,
  * Auktionsverlauf,
  * und Gewinner informiert zu werden.

---

### Server-Client-Kommunikation via Java RMI

* Server stellt Anwendung als **Remoteobjekt** bereit.
* Auktionsdienst muss über eine **Registry** bekannt gemacht werden.
* Client greift per Fernaufruf auf den Dienst zu (z. B. über Kommandozeile).

---

### Kommunikationsschicht

* Zwei zentrale Klassen:

  * **VSConnection**:

    * Versendet und empfängt Byte-Arrays beliebiger Länge.
    * Nutzt TCP-Verbindung.
  * **VSObjectConnection**:

    * Versendet und empfängt beliebige Objekte.
    * Übernimmt Marshaling und Unmarshaling (Objekt <-> Nachricht).

---

### Serialisierung und Datenkomprimierung

* Java bietet eingebaute Serialisierung (ObjectOutputStream), aber:

  * Das Format ist nicht kompakt.
* Ziel: Übertragene Datenmenge minimieren.
* Vorgehen:

  * Analyse der mit ObjectOutputStream erzeugten Daten einer Beispielklasse:

    * Besteht aus `int`, `String` und Objekt-Array.
  * Kompaktere Codierung der Daten entwickeln.

---

### Anpassbare Serialisierung

* Über Interface **Externalizable** kann benutzerdefinierte Serialisierung/Deserialisierung implementiert werden.
* Codierte Binärdaten können zur Analyse in Eclipse als **Compound Text** ausgegeben werden.

