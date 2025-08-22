
### **Allgemeines Ziel**

Ihr erweitert euer **Java RMI-basiertes Fernaufrufsystem** um **Fehlertoleranz** durch verschiedene **Aufrufsemantiken**. Außerdem sollt ihr **Fehler im Kommunikationssystem simulieren**, um die Mechanismen zu testen.

---

### **1. Aufrufsemantiken**

Ihr implementiert eine oder beide dieser Semantiken:

* **Last of Many (LoM):**

  * Mehrfache Anfragen möglich – nur die letzte zählt.
  * Nutzung von **Sequenznummern** zur Identifikation.
  * **Timeouts** notwendig, um fehlgeschlagene Aufrufe zu erkennen.

* **At Most Once (AMO):** (nur für 7,5 ECTS erforderlich)

  * Jede Anfrage wird **maximal einmal** ausgeführt.
  * Ergebnis muss **gespeichert** werden (idempotent).
  * **Garbage Collection** nötig für gespeicherte Antworten.

Die Semantik wird in den **Stub und Skeleton** implementiert:

* Client-seitig: `VSInvocationHandler`
* Server-seitig: `VSServer`

---

### **2. Fehler-Simulation**

Ihr sollt Fehler simulieren, um die Semantiken zu testen:

* **Verlust von Nachrichten** (z. B. durch Socket-Abbruch)
* **Verzögerung von Nachrichten** (z. B. künstliche Wartezeit)

**Nicht erforderlich:**

* Teilverlust oder Beschädigung von Nachrichten

**Vorgehen:**

* Implementiert eine Klasse `VSBuggyObjectConnection` (am besten als Unterklasse von `VSObjectConnection`).
* Fehler werden in `sendObject()` und `receiveObject()` eingebaut.
* Fehlerintensität soll **variabel** sein (gelegentlich bis ständig).
* Fehlerarten sollen auch **kombiniert** vorkommen können.

---

### **3. Socket-Timeouts**

Um zu verhindern, dass Leseoperationen ewig blockieren:

* Verwendet `setSoTimeout(ms)` am Socket.
* Fangt gezielt `SocketTimeoutException` und `IOException`.

---

### **4. Auswahl der Aufrufsemantik**

* Für **jede Methode** soll individuell festgelegt werden, **welche Semantik** verwendet wird.
* Das geschieht über **Annotationen** im Interface.

Beispiel:

```java
@VSAnnotation(VSMethodType.ReadAccess)
String get(String key);

@VSAnnotation(VSMethodType.WriteAccess)
void put(String key, String value);
```

* Annotation wird zur **Laufzeit** über die **Reflection API** ausgelesen.

---

### **5. Eigene Annotationen**

* Definiert mit `@interface`, z. B. `@VSAnnotation`.
* Nutzt `@Retention(RetentionPolicy.RUNTIME)`, damit sie zur Laufzeit sichtbar ist.
* Enum z. B. `VSMethodType` mit Werten `ReadAccess` und `WriteAccess` erlaubt Klassifikation.

---

### **Zusätzliche Hinweise**

* Die Fehlerbehandlung und die Semantik-Implementierung sollten **flexibel und realistisch** getestet werden.
* **Testfälle** sollten möglichst viele Kombinationen und Fehlerarten abdecken.
