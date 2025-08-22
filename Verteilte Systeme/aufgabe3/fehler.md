
## 1. Zwei Fehlerkategorien

1. **Anwendungsfehler**

   * Entstehen auch bei lokalen Aufrufen (z. B. vergessene Ausnahmen, ungültige Eingaben).
   * Werden vom Fernaufrufsystem transparent weitergereicht (z. B. `InvocationException`).

2. **Fernaufrufsfehler**

   * Tritt nur bei verteilten Aufrufen auf (Server-Crash, Überlastung, Netzwerkprobleme).
   * Können sein:

     * Nachrichtenverlust
     * Nachrichtendelay
     * Neuordnung von Nachrichten
     * Verbindungsabbruch
   * Sofern möglich, behandelt das RMI-System sie intern; bei Dauerfehlern wird eine `RemoteException` geworfen.

---

## 2. Komplexes Fehlermodell

* Lokal: Aufrufer und Methode stürzen gemeinsam ab; keine Netzwerkebene.
* Remote: Aufrufer und Serverseite unabhängig – nur eine Seite kann betroffen sein.
* Netzwerk introduces zusätzliche Fehler, die lokal nicht existieren.

Keine vollständige Transparenz möglich, da manche Fehler (dauerhafter Verbindungsabbruch) nicht intern behoben werden können.

---

## 3. Fernaufruf-Semantiken

Ziel: **Kommunikationsfehler** tolerieren. Hauptsemantiken:

| Semantik          | Garantie                                                               |
| ----------------- | ---------------------------------------------------------------------- |
| **Maybe**         | Anfrage wird vielleicht ausgeführt; Default ohne speziellen Mech.      |
| **At-least-once** | Mindestens einmal ausgeführt; Client wiederholt bis Antwort kommt.     |
| **At-most-once**  | Maximal einmal ausgeführt; Server cached Antworten, liefert Duplikate. |
| **Last-of-many**  | Nur neueste Antwort akzeptiert; Client versieht Aufruf mit Version.    |

---

### 3.1 Maybe

* Standardverhalten ohne Zusatzlogik.
* Erfolgreiche Ausführung oder fehlende Antwort beide akzeptabel.

### 3.2 At-least-once

* Client sendet, wartet Timeout, wiederholt endlos, bis eine Antwort kommt.
* Akzeptiert die **erste** Antwort (kann veraltet sein).
* Server kann Methoden mehrfach ausführen → unerwünschte Mehrfachnebenwirkungen bei Zustandsänderungen.

### 3.3 At-most-once

* Wiederholungen erlaubt, aber Server führt Anfragen nur einmal aus.
* Caching aller Request-Response-Paare auf Serverseite.
* Bei erneutem Eintreffen einer Anfrage liefert der Server die gespeicherte Antwort.
* Erfordert **Garbage Collection** der Antwort-Caches.

### 3.4 Last-of-many

* Client versieht jeden Retry mit einer **Sequenznummer**.
* Server führt mehrfach aus, Client akzeptiert **nur** die Antwort mit der höchsten Sequenznummer.
* Serverseitig **kein** Cache nötig, Client muss jedoch Versionierung verwalten.

---

## 4. Voraussetzungen für die Implementierung

* **Eindeutige Identifikation** jeder Anfrage:

  * Client-ID, Objekt-ID, Methoden-ID, Aufrufzähler.
* **Versionierung** bei Last-of-many: zusätzlicher Zähler für Retries.
* **Timeouts** und **Exception-Handling** für `SocketTimeoutException` vs. `IOException`.

---

## 5. Idempotenz

* Entscheidet, ob Mehrfachausführungen problematisch sind.
* **Mathematisch**: f(f(x)) = f(x).
* **Praktisch**: keine Seiteneffekte bei mehrfacher Ausführung.
* Beispiele idempotenter Operationen:

  * Lesen
  * Setzen eines Wertes
  * Löschen einer Auktion
* Nicht idempotent: Zähler erhöhen.

---

## 6. Garbage Collection bei At-Most-Once

Mögliche Strategien:

1. **Client-gesteuert**: Client sendet Freigabe, wenn Antwort nicht mehr gebraucht wird (problematisch bei Client-Ausfall).
2. **Ersatz durch neuen Aufruf**: Beim neuen Aufruf desselben Clients wird alter Cache-Eintrag gelöscht (nur eine Antwort pro Client).
3. **Timeout-basiert**: Server verwirft Antworten nach einer Frist automatisch (unabhängig vom Client).

Bei allen Strategien muss gewährleistet bleiben, dass die **At-Most-Once-Semantik** nicht verletzt wird (kein erneutes Ausführen, wenn Cache-Eintrag bereits gelöscht).

