

## ✅ Ziel der Aufgabe

In dieser Aufgabe soll Java RMI vollständig durch ein eigenes Fernaufrufsystem ersetzt werden. Ziel ist es, **transparente Fernaufrufe** zu ermöglichen, die sich für den Client so verhalten, als wären sie lokale Aufrufe. Dazu gehört:

* Dynamische Erzeugung von **Stubs und Skeletons**
* Unterstützung von **Rückrufen (Callbacks)**
* **Korrekte Fehler- und Exception-Behandlung**
* **Transparenz** bei der Nutzung verteilter Objekte

---

## 🖥 Neue Klassen auf Client-Seite

### **VSRemoteReference**

* Speichert **Host**, **Port** und **Objekt-ID** eines entfernten Objekts
* Dient zur **Lokalisierung** des Objekts bei Fernaufrufen

### **VSInvocationHandler**

* Wandelt **lokale Methodenaufrufe in Fernaufrufe** um
* Entscheidet dynamisch über die Art der Parameterübergabe:

  * **Call-by-Value** (Werte werden serialisiert übertragen)
  * **Call-by-Reference** (nur Referenz übertragen, z. B. für Rückrufe)
* Prüft zur Unterscheidung den Typ der Objekte (z. B. mit `isAssignableFrom`)

  * Beispiel: `Serializable.class.isAssignableFrom(obj.getClass())`

---

## 🗄 Server-Seitige Komponenten

### **VSServer**

* Verwaltet eingehende **Verbindungen vom Client**
* Führt den **Nachrichtenaustausch** (Senden/Empfangen) durch

### **VSRemoteObjectManager**

* Hält **Referenzen auf exportierte Objekte**
* Führt eingehende **Fernaufrufe auf den entsprechenden Objektmethoden** aus

---

## ❗ Exception Handling

Ein zentrales Ziel der Aufgabe ist der **transparente Umgang mit Exceptions** bei Fernaufrufen.

* Bei der Ausführung auf dem Server kann eine Exception auftreten

  * Diese darf **nicht zum Absturz** des Servers führen
  * Stattdessen wird sie mit `**InvocationTargetException**` **gefangen**

### Ablauf:

1. Server fängt die Exception während des Methodenaufrufs ab
2. Exception wird **serialisiert und an den Client gesendet**
3. Der **Stub auf Client-Seite entpackt** die Exception
4. Die Exception wird dort **erneut geworfen**, als wäre sie lokal aufgetreten

> 💡 So bleibt für den Client die Transparenz gewahrt: er merkt nicht, ob die Exception lokal oder remote auftrat.

---

## 🌐 Fehlerquellen bei Fernaufrufen

Fernaufrufe können an Fehlern scheitern, die bei lokalen Aufrufen nicht auftreten:

* Server ist nicht erreichbar
* Netzwerkprobleme (z. B. Timeout, Verbindungsabbruch)
* Inkonsistente Objekte durch unvollständige Übertragungen

### Wichtig:

Solche Fehler sollen möglichst vom **Fernaufrufsystem selbst behandelt** werden (z. B. Retry-Logik, Timeouts, Logging), **nicht** direkt in der Anwendungsschicht.

> 🎯 Ziel: **Maximale Transparenz** – für den Aufrufer sieht es wie ein gewöhnlicher Methodenaufruf aus.

---

## 🔄 Rückrufe (Callbacks)

Rückrufe erlauben es dem Server, eine Methode auf einem vom Client übergebenen Objekt auszuführen.

### Wichtig:

* Rückrufe erfordern **Call-by-Reference**, nicht Call-by-Value.
* Das bedeutet:

  * Es wird **keine Kopie des Objekts** übertragen
  * Stattdessen erhält der Server eine **Remote-Referenz** auf das Client-Objekt

### Voraussetzungen:

* Das Objekt muss:

  * **exportiert sein**
  * eine geeignete **Remote-Schnittstelle** implementieren

---

## ✅ Behandlung von Parametern und Rückgabewerten

Nicht nur die **Parameter**, sondern auch **Rückgabewerte** einer Methode können Remote-Referenzen sein. Daher:

* Der **InvocationHandler auf Client-Seite** muss für jeden Parameter prüfen:

  * Handelt es sich um ein `Serializable`? → **Call-by-Value**
  * Handelt es sich um ein Remote-Objekt? → **Call-by-Reference**
* Das Gleiche gilt für den Rückgabewert einer Methode
* Typprüfung z. B. mit:

  ```java
  if (RemoteInterface.class.isAssignableFrom(obj.getClass())) { ... }
  ```

---

## 📌 Zusätzlicher Hinweis

* In separaten Videos werden zusätzliche Details zur **Funktionsweise von Stubs/Skeletons** und zu **Rückrufen** behandelt. Diese solltest du ebenfalls anschauen, um die volle Funktionalität deines Systems zu verstehen und korrekt umzusetzen.


