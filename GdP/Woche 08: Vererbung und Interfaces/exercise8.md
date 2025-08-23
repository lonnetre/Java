Aufgabe 3: Nullstellensuche

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◍
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: Mathe, Funktionen, Interfaces, Vererbung

In dieser Aufgabe soll automatisiert die Nullstelle einer mathematischen Funktion gefunden werden. Da es allerdings sowohl verschiedene Arten von Funktionen als auch verschiedene Möglichkeiten zur Berechnung von Nullstellen gibt, soll in dieser Aufgabe möglichst viel abstrakt (d.h. ohne eine konkrete Implementierung einer Funktion bzw. eines Verfahrens) gearbeitet werden.

Um Nullstellen einer Funktion zu finden, muss es möglich sein, diese an diskreten Positionen abzutasten, d.h., für ein konkretes x den Funktionswert abzufragen. Außerdem ist es meist hilfreich, auch die Ableitung der Funktion an der Position zu kennen. Definieren Sie deshalb ein Interface Sampleable, das für ein gegebenes x den Funktionswert f(x) berechnet und zurückgibt. Außerdem soll es möglich sein, für ein x die Ableitung f'(x) an der Stelle abzufragen. Wählen Sie selbstständig geeignete Methodennamen und Datentypen.

Die konkreten Algorithmen zur Nullstellensuche sollen alle eine Methode findRoot anbieten, die für eine abtastbare Funktion eine Nullstelle berechnet. Definieren Sie dieses Verhalten mit Hilfe einer Oberklasse RootFindingAlgorithm. Neben der abtastbaren Funktion soll der Methode auch ein Interval für x übergeben werden, das angibt, in welchem Bereich die Nullstelle gesucht werden soll. Definieren Sie in der Oberklasse über einen Parameter  𝜖  (z.B.: 0.001) außerdem, um wieviel f(x) von der Null abweichen darf, um trotzdem näherungsweise als Null zu gelten.

Legen Sie eine Testklasse an, in der die zukünftigen Implementierungen auf Ihre zeitliche Performance getestet werden. In einer Methode logRootFinding soll für eine gegebene Funktion und ein gegebenes Verfahren die Nullstellensuche durchgeführt, dessen Zeit gemessen und schließlich die gemessene Zeit ausgegeben werden. Verwenden Sie zur Zeitmessung die Methode <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html#currentTimeMillis()" target=_blank>System.currentTimeMillis()</a>. Geben Sie außerdem aus, wie nah der Algorithmus an die Nullstelle herangefunden hat.

Nachdem jetzt alles strukturell vorbereitet ist, sollen konkrete Funktionen und Verfahren zur Nullstellensuche implementiert werden. Um den Rahmen dieser Aufgabe klein zu halten, sollen nur zwei verschiedene Funktionsarten implementiert werden:
Schreiben Sie eine Klasse SinusFunction, die die Sinus-Funktion repräsentiert. Implementieren Sie das Interface Sampleable.
Implementieren Sie eine Klasse PolynomialFunction. Ein Polynom ist folgerndermaßen aufgebaut: 𝑓(𝑥)=𝑎0+𝑎1𝑥+...+𝑎𝑛−2𝑥𝑛−2+𝑎𝑛−1𝑥𝑛−1+𝑎𝑛𝑥𝑛 . Schreiben Sie einen Konstruktor, über den die Koeffizienten (a_n ... a_0) bestimmt werden. Implementieren Sie anschließend das Interface Sampleable.

Auch für die Verfahren zur Nullstellensuche sollen nur zwei Varianten programmiert werden:
Schreiben Sie eine vereinfachte Nullstellensuche StepwiseSearch, die die Nullstelle einer Funktion sucht, indem Sie die Funktion in kleinen Schritten abtastet. Lassen Sie die Klasse dazu von RootFindingAlgorithm erben.
Implementieren Sie das Newton-Verfahren zur Nullstellensuche.

Legen Sie einige Funktionen an und messen Sie, welches Verfahren die Nullstellen schneller findet.
Aufgabe 4: Color Dominion

Schwierigkeitsgrad: ★★★★☆
Zeitaufwand: ◍◍◑
Code-Review-Service:  ja, geben Sie alle Dateien hier ab
Keywords: Spiellogik, Dokumentation, Framework, Bibliothek, Interfaces, Vererbung

Ziel dieser Aufgabe ist es, dass Sie sich mit einer unbekannten API auseinandersetzen. Die Schwierigkeit der Aufgabe liegt also nicht in einem komplizierten Algorithmus, sondern darin, eine unbekannte Dokumentation durchzulesen, die Funktionsweise des gegebenen Codes zu verstehen und die relevanten Klassen und Methoden zu finden. Nehmen Sie sich die Zeit, um alles in Ruhe durchzulesen.

In dieser Aufgabe soll ein kleines Spiel entwickelt werden. Auf einem zweidimensionalen Spielbrett mit quadratischen Feldern, ähnlich einem Schachbrett, treten zwei Spielfiguren gegeneinander an. Eine wird automatisch vom Computer gesteuert, die andere von einem menschlichen Gegenspieler. Jeder Spieler wählt im Verlauf des Spiels Felder aus (der menschliche Spieler durch einen Mausklick), die er besetzen möchte. Der Gewinner des Spiels ist derjenige, der zuerst alle Felder besetzt hat.


Ein Spielbrett mit rot und blau besetzten Feldern
Die wichtigsten Klassen für das Spiel sind:

BaseGame: Die Klasse BaseGame ist das Grundgerüst für das Spiel und kümmert sich unter anderem darum, dass alles richtig gezeichnet wird und dass die Methoden für die Spiellogik mit den richtigen Parametern zur richtigen Zeit aufgerufen werden (beispielsweise werden Mausklicks so aufbereitet, dass automatisch die Methode clickedTile() des DominionInterface aufgerufen wird). Um ein Spiel starten zu können, benötigen Sie eine Instanz der Klasse BaseGame, auf der Sie die Methode run() aufrufen können. Anschließend initialisiert BaseGame die Spielwelt durch den Aufruf der Methode setupWorld(). Nach Spielstart wird zyklisch update() mit der aktuellen Spielzeit aufgerufen. Mit Hilfe der Methode setWinner() kann das Spiel gestoppt und ein Gewinner gekürt werden.

Figure: Repräsentiert eine Spielfigur. Jeder Spielfigur wird eine Farbe zugeordnet. Die Figure kann sich zu Feldern bewegen (moveTo(DominionTile)) und soll diese besetzen, wenn Sie darauf ankommt. Eine Figur ist entweder ein Spieler-Charakter oder ein Nicht-Spieler-Charakter (engl. non-player character, npc). Eine spielbare Figur wird durch einen Mausklick zu einem neuen Ziel geschickt. Sie soll aber nur neue Ziele annehmen, wenn sie nicht läuft. Ein NPC soll nach dem Erreichen eines Feldes für eine bestimmte Zeit (pauseTime) rasten. Dazu können Sie das Attribut walkOnTime und dessen Getter und Setter verwenden. Das Attribut soll nach dem Erreichen des Ziels die Spielzeit speichern, zu der sie das nächste Feld (das nächstgelegene Feld in der Umgebung der Figur, das nicht in der eigenen Farbe markiert ist) besucht.

DominionTile: Einzelnes Feld des Spielbretts. Kann von einer Figur besetzt werden (engl. owner) und wird in der Farbe des besetzenden Spielers markiert.

DominionTileManager: Verwaltet das eigentliche Spielbrett. Das Spielbrett ist ein zweidimensionales Array bestehend aus DominionTile-Instanzen. Zu Beginn wird in setupMapTiles(BaseGame world) jedes Feld mit einem neuen DominionTile belegt. In der obigen Abbildung läuft gerade einer der beiden Spieler vom Feld  𝑓3,2  (Spalte 3, Zeile 2) nach  𝑓2,2 . Die Abbildung oben zeigt eine schematische Ansicht des (isometrischen) Spielbretts.

Dominion: In Dominion ist die Spiellogik enthalten. Eine Dominion-Instanz besitzt zwei Spielfiguren und ein Spielbrett. BaseGame kümmert sich darum, dass die Instanzmethode setupWorld() der Klasse Dominion aufgerufen wird, bevor das Spiel gestartet wird. In dieser werden die Spielfiguren initialisiert und alle Felder des Spielbretts zufällig entweder dem Spieler oder dem NPC zugeordnet. Nach Spielstart wird zyklisch update() aufgerufen. Hier kann geprüft werden, ob eine Figur gewonnen hat oder ob der NPC ein neues Ziel suchen soll. Außerdem gibt es in Dominion Instanzmethoden, die aufgerufen werden, wenn eine Figur ein neues Ziel suchen soll (chooseTarget()), eine Figur ein Ziel erreicht hat (reachedTarget()) und ein DominionTile vom menschlichen Spieler angeklickt wurde (clickedTile()).
Framework

Für diese Aufgabe ist eine Bibliothek gegeben, die die grafische Ausgabe und viele Spielmechaniken für Sie zur Verfügung stellt. Ihre Aufgabe ist es, die Klassen DominionTileManager und Dominionzu implementieren. Im gegebenen Framework (dominion.jar) sind alle anderen Klassen enthalten und können innerhalb einer .java-Datei verwendet werden, sobald die Bibliothek eingebunden und die benötigten Klassen importiert wurden. In der Dokumentation der Bibliothek können Sie genauer nachlesen, welche Funktionalität die einzelnen Methoden der beschriebenen Klassen haben und welche Funktion die Klassen der beiden zu implementierenden Interfaces erfüllen sollen.

Bisher haben Sie thematisch zusammenpassende Klassen in Paketen gruppiert (z.B. die Klassen Book, Magazine und Shelf für die Bücherregal-Aufgabe). Es ist allerdings nicht möglich, Java-Bibliotheken (Jar-Dateien) nur für genau ein Paket zugänglich zu machen. Würde man das Dominion-Framework für diese Aufgabe also in Ihr gesamtes IntelliJ-IDEA-Projekt einbinden, so würde Sie das Framework bei sämtlichen anderen Aufgaben im Semester stören, da z.B. automatische Import-Anweisungen von IntelliJ IDEA immer das dominion.jar berücksichtigen würden, obwohl Sie gar nicht mehr an der Dominion-Aufgabe arbeiten.

Java bietet neben der Projekt- und Paket-Ebene noch eine Zwischenstufe an: Module. Module gruppieren mehrere thematisch zusammenpassende Pakete. Im Gegensatz zu Paketen ist es bei Modulen möglich, ein Framework nur für genau ein Modul einzubinden. Wir empfehlen daher, immer ein eigenes Modul für eine Aufgabe anzulegen, wenn ein externes Framework in der Aufgabe verwendet werden soll.

Legen Sie per Rechtsklick auf das Projekt → New → "Module..." ein neues Modul namens dominion an.
Laden Sie die Bibliothek herunter, verschieben Sie die Datei in den Modulordner innerhalb des IntelliJ IDEA Projekts und binden Sie das Framework ein, indem Sie in IntelliJ IDEA per Rechtsklick auf die Datei "Add as Library..." und danach das Level "Module Library" auswählen.
Öffnen Sie die Dokumentation zu der Bibliothek, indem Sie die heruntergeladene ZIP-Datei entpacken und die Datei index.html in einem Browser öffnen. Alles Wichtige für die Aufgabe finden Sie unter gdi.game.dominion.
Innerhalb des Moduls dominion sollen alle benötigten Klassen für diese Aufgabe in einem Paket dominion gruppiert werden.
DominionTileManager

Zunächst benötigen Sie ein Spielbrett, auf dem Sie Figuren bewegen können. Dieses soll als 2D-Array von DominionTile-Instanzen in einer eigenen Klasse verwaltet werden, damit die Spielwelt darauf zugreifen kann.

Erstellen Sie eine Klasse DominionTileManager.
Legen Sie ein Attribut für ein zweidimensionales Array von DominionTile-Instanzen an.
Schreiben Sie einen Konstruktor, der zwei ganzzahlige Parameter erwartet. Der erste soll die Anzahl der Spalten des Spielbretts angeben, der zweite die Anzahl der Zeilen. Erzeugen Sie ein entsprechend großes Array und legen Sie es in Ihrem Attribut ab.
BaseGame und auch andere Klassen aus dem Framework sind auf das Spielbrett angewiesen und benötigen einige Methoden daraus, um ihre Aufgaben erfüllen zu können. Die Klassen arbeiten daher intern mit dem Interface DominionTileManagerInterface, zu dem Sie nun die nötige Implementierung schreiben müssen. Lassen Sie DominionTileManager das Interface DominionTileManagerInterface implementieren, um die korrekte Funktionalität sicherzustellen. Eine Beschreibung der zu implementierenden Methoden finden Sie in der Dokumentation des Interfaces.
Dominion

Die grundlegende Spiellogik wird bereits von der Klasse BaseGame bereitgestellt. Einige Spiel-spezifische Methoden fehlen allerdings noch. Dafür muss BaseGame intern auf Methoden des DominionInterface zurückgreifen.

Erstellen Sie eine Klasse Dominion.
Die Klasse Dominion muss mindestens einen Konstruktor bereitstellen, der genau einen Parameter vom Typ DominionTileManagerInterface erwartet. Diesem Konstruktor können somit Instanzen einer Klasse, die DominionTileManagerInterface implementiert, übergeben werden. Speichern Sie die übergebene Instanz in einem Attribut ab.
Legen Sie weitere Attribute für zwei Spielfiguren an (Typ Figure). Eine dieser Figuren werden Sie später selbst mit der Maus steuern können, die andere bewegt sich selbstständig.
Lassen Sie Dominion das Interface DominionInterface implementieren. Eine Beschreibung der zu implementierenden Methoden finden Sie wieder in der Dokumentation des Interfaces.
Legen Sie in einer eigenen Klasse eine main-Methode an. Erzeugen Sie eine Instanz vom Typ Dominion mit einem Spielbrett der Größe 5x7. Erzeugen Sie ein Objekt der Klasse BaseGame. Der Konstruktor der Klasse BaseGame erwartet den Parameter args der main-Methode und eine Dominion-Instanz. Starten Sie das Spiel, indem Sie die Methode run() der BaseGame-Instanz aufrufen.
Anmerkung: Sie können die Schwierigkeit des Spiels ändern, indem Sie die PauseDuration des NPC verändern (Standard: 2.0) oder die Größe des Spielbretts anpassen.

Hinweis: Sollte die grafische Ausgabe auf Ihrem Computer nicht flüssig laufen, starten Sie das Programm mit dem Run Argument --slowMachine.

<span style="color: rgb(0,0,255);">Challenge [optional]</span>

Immer nur das nächste unbesetzte Feld auszuwählen, ist nicht der beste Weg für den NPC. Entwickeln Sie einen Algorithmus, der die Felder effizienter abläuft.