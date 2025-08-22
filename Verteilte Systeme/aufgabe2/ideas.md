

## 🔍 **Aufgabenübersicht (Übungsaufgabe 2 – Fernaufrufsystem)**

| Teilaufgabe | Was du machen sollst                                                     | Wo stehen die Infos?                                               | Ziel der Aufgabe                                                                               |
| --------------------------------------------------- | ------------------------------------------------------------------------ | ------------------------------------------------------------------ | ---------------------------------------------------------------------------------------------- |
| **2.1** Stubs & Skeletons                           | Eigenes Fernaufrufsystem mit dynamischen Proxies entwickeln              | `02-Stubs-und-Skeletons_handout.pdf`, Video-Transkript, Folien 2.3 | Lokale Aufrufe sollen sich wie Fernaufrufe verhalten (Transparenz), keine Java RMI-Nutzung     |
| **2.2** VSInvocationHandler & VSRemoteReference     | Stub-Verhalten implementieren (`invoke()`), RemoteReference nutzen       | Folien 2.3:1, Aufgabenblatt, Reflection-Teil, Beispielcode         | Umsetzung des Proxies: Verbindung aufbauen, Anfrage senden, Antwort zurückgeben                |
| **2.3** Exception Handling                          | Exceptions müssen korrekt vom Server zum Client übertragen werden        | Folie 2.1:3, Video-Transkript, `InvocationTargetException`         | Exceptions dürfen nicht verschwinden → sollen wie Rückgabewerte übertragen werden              |
| **2.4** Call-by-Reference / Rückrufe                | Call-by-Reference für Rückrufe unterstützen (nicht alles Call-by-Value!) | Folien 2.1:4 und 2.4:1–5, Aufgabenstellung, Transkript             | Server kann Methoden auf Client-Objekten aufrufen, wenn diese remote sind (z. B. EventHandler) |
| **2.5** (Optional) Performanzvergleich mit Java RMI | Vergleichsmessung: Antwortzeiten eigener Aufrufe vs. Java RMI            | Folien 2.5, Aufgabe 2.5 im Aufgabenblatt                           | Zeigen, ob dein System schneller als RMI ist – „kleine Belohnung“ möglich                      |

---

## ✅ **Was du konkret tun musst (pro Teil)**

### 🔹 **2.1: Stubs & Skeletons bauen**

* Nutze **dynamische Proxies** → `Proxy.newProxyInstance(...)`
* Implementiere `VSInvocationHandler`, der:

  * Verbindung zum Server herstellt
  * Methode + Argumente serialisiert
  * Antwort empfängt
  * Rückgabewert oder Exception zurückgibt
* **Nutze** `VSRemoteReference` (enthält Host, Port, Objekt-ID)

**Hilft dir dabei:**

* Handout-Folie **2.3:1–9**
* Video-Transkript (00:35–01:02)
* Java-Tutorial zur [Reflection API](https://docs.oracle.com/javase/tutorial/reflect/index.html)

---

### 🔹 **2.2: `invoke()` korrekt implementieren**

* Alles in **`VSInvocationHandler.invoke(...)`**
* Schritte:

  1. Verbindung mit `VSObjectConnection` öffnen
  2. Anfrage-Objekt erzeugen (z. B. `VSRequest`)
  3. Senden über TCP
  4. Antwort empfangen (`VSResponse`)
  5. Rückgabewert oder Exception behandeln
* **Parameter- und Rückgabebehandlung beachten**

**Hilft dir dabei:**

* Codegerüst im Aufgabenblatt
* Folie **2.3:5–6**, Beispiel `invoke`
* Kommentarblock im Java-Code, den du gepostet hast

---

### 🔹 **2.3: Exception Handling**

* Wenn eine Methode auf dem Server `throw` macht:

  * Muss per `InvocationTargetException` gefangen werden
  * Exception serialisieren und an Client schicken
  * Im Stub: Exception wieder **auspacken und werfen**

**Hilft dir dabei:**

* Folie **2.1:3**
* Transkript **01:38–02:06**
* Java-Klassen: `InvocationTargetException`, `Throwable`

---

### 🔹 **2.4: Call-by-Reference / Rückrufe**

* Prüfen, ob Parameter ein exportiertes Remote-Objekt ist:

  * Wenn ja → **Call-by-Reference** (Proxy übergeben)
  * Wenn nein → **Call-by-Value** (serialisieren)
* Auch Rückgabewerte beachten!
* Prüfen mit:

```java
if (VSRemote.class.isAssignableFrom(param.getClass())) { ... }
 ```
* Beim Rückruf: Server ruft Methode auf Client-Objekt auf → über Stub

**Hilft dir dabei:**

* **Folien 2.1:4 + 2.4:1–5**
* Transkript **02:30–03:15**
* Kommentar in deinem Code
* Aufgabe in deinem Skript: *„Erweiterung der Implementierung zur Unterstützung von Rückrufen“*

---

### 🔹 **2.5: Evaluierung (optional für 5 ECTS + Belohnung)**

* Miss Antwortzeiten für `getAuctions()` bei:

  * eigenem System
  * Java RMI
* Mehrere Durchläufe machen, Mittelwerte bilden
* Vergleich grafisch darstellen (z. B. Diagramm)

**Hilft dir dabei:**

* **Folie 2.5:1–6**
* Aufgabe 2.5 im PDF
* Hinweis: `setTcpNoDelay(true)` auf beiden Seiten setzen
* Hinweis: Belohnung möglich, wenn du Java RMI „fair schlägst“

---

## 🎁 **Zusätzliche Hinweise aus dem Video und Transkript**

* Exceptions dürfen **nicht verschwinden** – sie müssen so behandelt werden, als kämen sie lokal
* Rückrufe müssen **Remote-Referenz** verwenden, keine Kopie
* Proxies helfen dabei, **Transparenz** zu wahren
* Prüfen auf `Serializable` vs. `Remote` ist entscheidend
* Skeletons brauchen `toGenericString()` zur Methodenerkennung

---

## ✅ Fazit: Wie findest du alle Infos?

| Was du suchst               | Wo es steht                                                                                                                      |
| --------------------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| Architekturübersicht        | `02-Stubs-und-Skeletons_handout.pdf`, Folien **2.3**                                                                             |
| InvocationHandler & Proxies | Video-Transkript + Folien **2.3:5–6**                                                                                            |
| Exception Handling          | Folie **2.1:3** + Transkript **ab 01:30**                                                                                        |
| Call-by-Reference           | Folien **2.1:4**, **2.4:1–5**, Transkript **ab 02:30**                                                                           |
| Performanzvergleich         | Folien **2.5:1–6**, Aufgabe **2.5**, Hinweis zur „kleinen Belohnung“                                                             |
| Java Reflection API         | Folien **2.2:1–4**, offizielles Tutorial: [Java Reflection Tutorial](https://docs.oracle.com/javase/tutorial/reflect/index.html) |

---

Wenn du möchtest, kann ich dir diese ganze Übersicht als PDF oder Notizzettel exportieren – oder eine Mindmap zum Lernen bauen. Sag einfach Bescheid!
