Aufgabe 4: Autofahrt

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◍◔
Code-Review-Service:  nein
Keywords: Konstruktoren, Simulation, Überladen

In dieser Aufgabe simulieren Sie vereinfacht die Fahrt eines E-Autos.

Erstellen Sie eine Klasse Car. Die Klasse soll folgende Attribute beinhalten:
Batteriekapazität (in kWh)
Aktueller Ladestand (in kWh)
Stromverbrauch pro Kilometer
Insgesamt gefahrene Kilometer
Die Batteriekapazität soll nur beim Erzeugen eines Autos gesetzt werden können.
Ein Konstruktor ohne Parameter soll die Batteriekapazität auf 50 kWh, den Stromverbrauch auf 1.59 kWh/km und alle anderen Attribute auf einen Initialwert von 0.0 setzen.
Schreiben Sie einen weiteren Konstruktor, der die Batteriekapazität als Eingabeparameter erhält. Der Stromverbrauch soll wiederum auf den Wert 1.59 kWh/km gesetzt werden.
Schreiben Sie einen Parameterkonstruktor, der als Eingabeparameter die Batteriekapazität und den Stromverbrauch pro 100 Kilometer erhält. Setzen Sie die entsprechenden Attribute auf die übergebenen Werte.
Reduzieren Sie redundanten Code, indem Sie die Konstruktoren sich gegenseitig aufrufen lassen.
Schreiben Sie eine Methode double charge(double chargingPower_kW, double duration_h), die das Auto laden soll. Erhöhen Sie entsprechend den Ladestand des Autos, aber achten Sie darauf, dass die Batteriekapazität nicht überschritten wird. Geben Sie zurück, mit wieviel Energie (kWh) das Auto tatsächlich geladen wurde.
Überladen Sie charge so, dass das Auto auch auf einmal komplett aufgeladen werden kann. Die Methode soll wieder zurückgeben, mit wieviel Energie (kWh) das Auto tatsächlich geladen wurde.
Legen Sie eine Methode boolean drive(double distance_km) an, die das Fahrzeug um distance_km-viele Kilometer bewegen soll. Passen Sie eventuell betroffene Attribute entsprechend an.
Falls die Batterie nicht ausreichend geladen ist, soll das Auto nicht bewegt und eine entsprechende Warnung ausgegeben werden. Die Methode soll true zurückliefern, falls das Auto bewegt wurde, sonst false.
Legen Sie in der main-Methode eine Instanz der Klasse Car an. Laden Sie das Auto vollständig auf und fahren Sie damit. Testen Sie Ihre implementierten Methoden.
Aufgabe 5: Palindrom

Schwierigkeitsgrad: ★★☆☆☆
Zeitaufwand: ◕
Code-Review-Service: ja, geben Sie die Datei PalindromeChecker.java hier ab
Keywords: Strings, Kodierung

Ein Wort ist genau dann ein Palindrom, wenn es vorwärts und rückwärts gelesen das gleiche Wort ergibt, z.B.: "Rentner". Schreiben Sie ein Programm, das überprüft, ob es sich bei den übergebenen Wörtern um Palindrome handelt.

Erstellen Sie eine Klasse PalindromeChecker mit main-Methode. Die zu überprüfenden Wörter sollen direkt bei Programmstart über die Kommandozeile übergeben werden, d.h., sie sollen der main-Methode als Parameter übergeben werden.

Implementieren Sie den Palindrom-Check. Nutzen Sie dazu die Methoden der String-Klasse. Am Ende soll gruppiert ausgegeben werden, bei welchen Wörtern es sich um Palindrome handelt und bei welchen nicht.
Beispiel für die Programmargumente "Lagerregal" "Tisch" "Stift" und "Rentner":
<pre>These words are palindromes:
Lagerregal
Rentner

These words are not palindromes:
Tisch
Stift
</pre>

Aufgabe 6: Bücherregal

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◍
Code-Review-Service:  ja, geben Sie die Dateien Test.java,Book.java und Bookshelf.javahier ab
Keywords: Verwaltung, Klassen, Strings, Instanzen

In dieser Aufgabe schreiben Sie ein digitales Bücherregal, wie es grundlegend z.B. für einen E-Book-Reader verwendet werden könnte.

Erstellen Sie eine Klasse Book und modellieren Sie dessen Eigenschaften. Ein Buch besitzt einen Titel, einen Autor und ein Erscheinungsjahr. Ergänzen Sie einen Parameterkonstruktor, um die Attribute zu initialisieren.
Schreiben Sie eine Methode printInfo(), die die Eigenschaften des Buchs für einen Benutzer gut leserlich auf der Konsole ausgibt.
Erstellen Sie eine Klasse Bookshelf. Ein Bücherregal soll mehrere Bücher verwalten. Die Bücher sollen über einen Parameterkonstruktor übergeben werden.
Bevor Sie die folgenden Methoden in der Bookshelf-Klasse implementieren, legen Sie eine weitere Klasse Test an. Es ist guter Stil, die main-Methode in eine eigene Klasse auszulagern. Testen Sie Ihren Code ab sofort innerhalb der Test-Klasse.
Schreiben Sie eine Methode printBooks(), die alle Bücher im Bücherregal ausgibt.
Schreiben Sie eine Methode boolean containsBook(String title), die überprüft, ob ein Buch mit dem Titel im Bücherregal vorhanden ist. Die Groß- und Kleinschreibung soll hierfür irrelevant sein.
Implementieren Sie eine Methode boolean addBook(Book book), die das übergebene Buch dem Bücherregal hinzufügt. Das Buch soll allerdings nur dann hinzugefügt werden, wenn nicht schon ein anderes Buch mit dem gleichen Titel im Bücherregal vorhanden ist. Die Methode soll true zurückgeben, wenn das Buch erfolgreich hinzugefügt werden konnte und false, falls nicht.
<span style="color: rgb(0,0,255);">Challenge [optional]</span>

Verbessern Sie die Methode containsBook so, dass das Buch auch dann gefunden wird, wenn sich der Benutzer der Methode ein wenig vertippt hat. Es sollen maximal 3 Buchstabendreher, Fehlbuchstaben und zusätzliche Buchstaben toleriert werden. Geben Sie alle in Frage kommenden Bücher zurück.