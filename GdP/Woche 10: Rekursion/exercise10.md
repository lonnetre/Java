Aufgabe 3: Binäre Suche

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◍
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: Suchalgorithmus, Rekursion

Ein wichtiges Problem, das beim Programmieren oft gelöst werden muss, ist, einen bestimmten Eintrag in einer großen geordneten Liste an Daten zu finden, beispielsweise Studierende anhand ihrer Matrikelnummer. Bei einer Vielzahl an Daten kann eine lineare Suche (=jeden Eintrag der Reihe nach überprüfen, ob er das gesuchte Element ist) allerdings sehr lange dauern.

Ein bekanntes Suchverfahren, welches das Problem deutlich schneller löst, heißt Binäre Suche. Dabei wird zunächst aus dem gesamten Suchraum das mittlere Element untersucht, ob es der gesuchte Eintrag ist. Falls nicht, wird überprüft, ob dieser Eintrag größer als das gesuchte Element ist. In dem Fall kann der Suchraum so halbiert werden, dass das mittlere Element und alle größeren Einträge ignoriert werden. Ist der Eintrag stattdessen kleiner, können das mittlere Element und alle kleineren Einträge von nun an ignoriert werden. Mit diesem kleineren Suchraum startet die Binäre Suche dann rekursiv von vorne – solange bis das gesuchte Element gefunden wurde oder der Suchraum kein Element mehr enthält.

Beispiel

Gegeben ist ein aufsteigend sortiertes int-Array. Es wird gesucht, ob die Zahl 45 vorhanden ist. Zu Beginn wird der komplette Suchraum von Index 0 bis Index 6 durchsucht. Nachdem das mittlere Element (9) kleiner ist als die gesuchte 45, wird der Suchraum im nächsten rekursiven Aufruf auf den Index-Bereich 4-6 eingeschränkt. Das mittlere Element (123) ist größer als die gesuchte 45, somit wird der Suchraum auf die restliche kleinere Hälfte eingegrenzt. In diesem Fall bleibt nur noch ein Element übrig, welches tatsächlich die gesuchte 45 ist.


Binäre Suche für ein int-Array
Java bietet für Arrays (und Collections – dazu bald mehr in der Vorlesung) mit der Methode binarySearch() direkt eine Implementierung der binären Suche an. Um Rekursion zu üben und das wichtige Konzept der binären Suche zu verstehen, um es auch in Zukunft auf eigenen Datenstrukturen anwenden zu können, soll allerdings in dieser Aufgabe auf die Methode verzichtet werden.

Aufgabe

Laden Sie den vorgegebenen Code im Package search herunter. Gegeben ist ein Array aus Studierenden, die einen Namen und eine Matrikelnummer kapseln.
Implementieren Sie eine rekursive binäre Suche nach einem/-r Student/-in der Methode binarySearch().
Testen Sie Ihren Code ausführlich – die Tücke liegt im Detail! Funktioniert Ihr Code für Arrays mit einer geraden und ungeraden Anzahl an Einträgen? Einem leeren Array? Für den ersten und den letzten Eintrag? Für ein Array mit  230  Einträgen? Selbst die Implementierung in der Java-API hatte bis 2006 einen Bug in der binarySearch()-Methode.
Aufgabe 4: Labyrinth

Schwierigkeitsgrad: ★★★★☆
Zeitaufwand: ◍◍◑
Code-Review-Service: ja, geben Sie die Datei WayFinder.java  hier ab
Keywords: Wegsuche, Backtracking, Rekursion

In dieser Aufgabe schreiben Sie einen Algorithmus, der in einem Labyrinth den Weg von einem Start- zu einem Zielpunkt findet.

Sie können diese Aufgabe in deutlich weniger als 100 Zeilen Code lösen. Der Algorithmus selbst ist kurz, benötigt aber ein gutes Verständnis für Rekursion und Backtracking.


Die Figur hat durch das Labyrinth zum Haus gefunden. Besuchte Felder sind grün markiert; der Weg ohne Sackgassen ist gelb umrahmt.
Demo-Video
Erstellen Sie ein neues Modul maze und binden Sie das gegebene Framework ein. Die zugehörige Dokumentation finden Sie hier. Die wichtigen Klassen für diese Aufgabe sind gdi.maze.Mazeund gdi.game.map.MapTile.

Legen Sie in der main()-Methode ein neues Maze an. Daraufhin sollte sich ein Fenster öffnen, in dem Sie das Labyrinth, eine Spielfigur und ein rotes Haus sehen. Schreiben Sie nun ein Programm, das die Spielfigur automatisch den Weg zum roten Haus (target) finden lässt. Lassen Sie am Ende den gelaufenen Weg anzeigen, allerdings ohne unnötige Umwege in Sackgassen (siehe Bild oben). Nutzen Sie dazu die Klasse gdi.maze.MapPath.