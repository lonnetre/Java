Aufgabe 3: JUnit

Schwierigkeitsgrad: ★★☆☆☆
Zeitaufwand: ◕
Code-Review-Service: ja, geben Sie die Dateien Rectangle.java und RectangleTest.javaals Paket JUnitRectangle(Unterordner um zip) hier ab
Keywords: Testen, Fehlersuche, JUnit

Wer programmiert, macht Fehler – das ist normal. Es ist allerdings wichtig, diese Fehler so früh wie möglich zu finden und zu beheben.

Wenn man gerade eine neue Methode schreibt, ist es noch vergleichsweise einfach, daran zu denken, die fertige Methode am Ende auch gründlich mit allen möglichen Werten zu testen. Aber was ist, wenn durch diese neue Methode plötzlich eine ganz andere Stelle im Code kaputtgeht (z.B. weil in der neuen Methode eine Referenzvariable auf null gesetzt wird, die vorher noch nie null war und der alte Code diesen Fall nicht abfängt)? Genau genommen muss nach jeder kleinen Änderung im Code das gesamte Programm wieder vollständig durchgetestet werden, um solche Fehler zu vermeiden. Das ist mit manuellem Testen nicht mehr praktikabel.

JUnit ist ein Framework zum Testen von Java-Programmen. Dabei handelt es sich um sogenannte "Unit Tests", d.h. es werden die einzelnen Komponenten (z.B. Methoden) der Software getestet. Mit JUnit schreibt man Java-Code zum Testen des eigentlichen Programms. Danach kann der JUnit-Test immer wieder schnell und bequem gestartet werden.

Viele Firmen betreiben heutzutage das sog. Test-driven Development. Das bedeutet, dass zuerst die Tests für die Software geschrieben werden, noch bevor das eigentliche Programm oder ein neues Feature implementiert wird. So wird von Anfang an über die Tests definiert, wie sich das Programm zu verhalten hat. Außerdem vereinfacht es die Entwicklung, da Fehler so früh wie möglich identifiziert werden können.

Beispiel

Gegeben sei folgender Code zur Berechnung des Umfangs eines Rechtecks:

1
2
3
4
5
public class Rectangle {
    public static int computePerimeter(int width, int height) {
        return width + height * 2;
    }
}
Download
Dann könnte ein JUnit-Testcase zu der Methode computePerimeter() so aussehen:

1
2
3
4
5
6
7
8
9
10
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
 
public class RectangleTest {
    @Test                                                              // Hinweis an JUnit: diese Methode ist ein Test
    void shouldComputePerimeter() {                                    // Methode (kein static!) ist immer void und package-private
        int perimeter = Rectangle.computePerimeter(5, 4);              // Methodenaufruf mit width = 5 und height = 4
        Assertions.assertEquals(18, perimeter, "Perimeter is not 18"); // prüft, ob erwarteter Wert in Variable perimeter steht
    }
}
Download
Aufgabe

Laden Sie sich die beiden Dateien Rectangle.java und RectangleTest.java herunter. Die Datei RectangleTest sollte Ihnen jetzt einen Fehler anzeigen, da JUnit noch nicht in das Projekt eingebunden ist. Bewegen Sie den Mauszeiger über die @Test-Annotation und wählen Sie im erscheinenden Tooltip "More actions..." → "Add JUnit5.8.1" aus. Bestätigen Sie danach den Dialog "Download Library vom Maven Repository". Jetzt sollten Ihnen keine Fehler mehr angezeigt werden.

Starten Sie den Test, indem Sie entweder per Rechtsklick in die Datei "Run 'RectangleTest'" auswählen oder links neben der Klassendefinition neben der Zeilennummer auf das kleine Play-Icon klicken. Das Icon wird Ihnen auch bei jeder einzelnen Test-Methode angezeigt. So können Sie auch nur einzelne Tests statt allen laufen lassen. (In diesem Code-Beispiel gibt es bisher nur einen Test.)

In der Ausgabe sollte Ihnen jetzt angezeigt werden, dass der Test fehlgeschlagen ist, und es sollte der Fehler "Perimeter is not 18" ausgegeben werden. Die Methode computePerimeter() beinhaltet tatsächlich einen Fehler (Punkt vor Strich) und in der Variable perimeter wird somit 13 anstatt 18 für den Rechtecksumfang gespeichert.

Beheben Sie den Semantikfehler in computePerimeter() und starten Sie den Test erneut. In der Ausgabe links unten sollte nun mit einem grünen Haken markiert sein, dass der Testcase shouldComputePerimeter() erfolgreich durchgelaufen ist.

Starten Sie den Testcase erneut – diesmal mit der Option "Run 'RectangleTest' with Coverage". Sie erhalten damit zusätzlich zu den Testergebnissen eine Übersicht, wieviel Prozent Ihres Codes von Testfällen abgeprüft werden. Bei diesem kleinen Code-Beispiel sollten Ihnen 100% angezeigt werden. In der Methode computePerimeter() sollten jetzt außerdem links neben den Zeilennummern alle Zeilen grün markiert werden. Zeilen, die nur eine Klammer beinhalten, werden teilweise nicht berücksichtigt. Zeilen, die durch keinen Testcase abgeprüft werden (z.B. ein if-Rumpf für spezielle Parameterwerte), sind rot markiert (sollte in diesem Beispiel nicht der Fall sein).

Ein einziger Testcase reicht natürlich nicht aus, um eine Methode gründlich zu testen. Für eine Methode sollten immer mehrere Testfälle geschrieben werden, die insbesondere auch Randfälle (z.B. 0 oder negative Werte) abprüfen. Schreiben Sie mehrere Tests, die die Implementierung der Methode computePerimeter() testen. Achten Sie insbesondere darauf, dass ein Umfang nie negativ sein kann. Werden der Methode computePerimeter()negative Parameter übergeben, soll der Testcase eine ArithmeticException erwarten.
Den offiziellen JUnit-User-Guide finden Sie hier. Die Dokumentation finden Sie hier. Hauptsächlich interessant ist die Klasse Assertions. Die Links dienen Ihnen nur als Nachschlagewerk. Sie müssen nicht alles lesen und verstehen.
Wählen Sie sinnvolle Namen für Ihre Testcases. In GdP halten wir uns an die Konvention, dass die Testmethode mit "should" beginnen sollte und danach beschreibt, welche Funktionalität welches Ergebnis hervorrufen soll (z.B. shouldComputePerimeterWithAWidthOfZero). Damit die Ausgabe der Tests einfacher lesbar ist, können Sie die Annotation @DisplayName("Mein Text") über jeder Test-Methode verwenden.
Um abzuprüfen, ob eine ArithmeticException geworfen wird, verwenden Sie die Methode <a href="https://junit.org/junit5/docs/current/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html#assertThrows(java.lang.Class,org.junit.jupiter.api.function.Executable)" target=_blank>Assertions.assertThrows()</a> wie folgt:
Assertions.assertThrows(ArithmeticException.class, () -> {
    // Hier Aufruf der zu testenden Methode
});
Aufgabe 4: Datumsanzeige

Schwierigkeitsgrad: ★★☆☆☆
Zeitaufwand: ◕
Code-Review-Service: nein
Keywords: Kalender, Dateien schreiben / lesen, Exceptions

In dieser Aufgabe schreiben Sie ein Programm, das eine einfache HTML-Seite mit dem aktuellen Datum erzeugt, die Sie in Ihrem Browser ansehen können. Web Development ist ein großes und komplexes Thema, aber eine kleine, lokale HTML-Seite können Sie auch ohne weiteres Wissen erzeugen.

Websites werden mit der Auszeichnungssprache (engl. Markup Language) HTML (HyperText Markup Language) geschrieben. Ein einfaches HTML-Grundgerüst sieht folgendermaßen aus; der Text zwischen den Body Tags (Zeile 3) kann beliebig geändert werden:

1
2
3
4
5
<!DOCTYPE html>
    <body>
        Hallo Welt!
    </body>
</html>
Download
Laden Sie die Datei herunter und kontrollieren Sie, dass Sie die Datei in einem Browser ansehen können und Ihnen der Text "Hallo Welt!" angezeigt wird.
Schreiben Sie ein Programm, das das heutige Datum und die aktuelle Uhrzeit ermittelt. Verwenden Sie dazu die Klasse Calendar und die Methode getTime()(beinhaltet sowohl Datum als auch Uhrzeit).
Erzeugen Sie einen String, der das einfache HTML-Grundgerüst enthält. Zwischen den Body Tags soll das Datum und die Uhrzeit stehen.
Ihr Programm soll den HTML-String in einer Datei namens date.html abspeichern. Legen Sie dazu ein neues FileWriter-Objekt an, schreiben Sie den String in die Datei und schließen Sie den FileWriter am Ende. Gehen Sie sinnvoll mit eventuellen Exceptions um.
Testen Sie, ob die Datei erstellt wird und öffnen Sie die Datei mit einem Webbrowser.
Schreiben Sie eine Methode, die den Inhalt der Datei wieder einliest und auf der Kommandozeile ausgibt. Falls dabei etwas schiefgeht, soll eine Exception geworfen werden. Hilfreiche Java-Klassen sind die Klasse File, um auf eine Datei im Dateisystem zuzugreifen, und die Klasse Scanner, mit der eine Datei zeilenweise ausgelesen werden kann.
Aufgabe 5: Balloons

Schwierigkeitsgrad: ★★★★☆
Zeitaufwand: ◍◍◍◍
Code-Review-Service: ja, geben Sie ihre Dateien als Paket balloons hier ab
Keywords: Spiellogik, Grafik, Sichtbarkeit, Referenzen, Vererbung, Dokumentation, Framework, Bibliothek

Die Aufgabe ist umfangreich und deshalb verlängern wir die Abgabe um die gesamte Winterpause auf den 7. Januar.
Wichtig: In der Zeit vom 23.12. bis 07.01. ist das GdP-Team nicht erreichbar. Fangen Sie deshalb frühzeitig mit der Bearbeitung an, um Fragen in den Rechnerübungen klären zu können!

In dieser Aufgabe programmieren Sie ein Spiel, bei dem mit einem Dartpfeil Luftballons abgeworfen werden.


Der Dartpfeil links (schwarzer Strich) wird auf die Luftballons geworfen
Demo-Video
In dieser Aufgabe begegnen Ihnen folgende Fachbegriffe aus der Computergrafik:

<a href="https://de.wikipedia.org/wiki/Sprite_(Computergrafik)">Sprite</a>: Ein Bild, das einen Gegenstand, Spielcharakter o.ä. repräsentiert.
<a href="https://de.wikipedia.org/wiki/Bildsynthese">Rendern</a>: Ein virtuelles Bild erzeugen; etwas mit dem Computer zeichnen
Frame: Um die Illusion eines sich bewegenden Bildes zu erzeugen, werden schnell hintereinander leicht unterschiedliche Einzelbilder (= Frames) angezeigt. Die Anzahl an angezeigten Frames pro Sekunde wird als FPS bezeichnet. Spiele sollten mindestens mit 30 FPS laufen, um eine flüssige Animation anzuzeigen.
Erstellen Sie ein neues Modul balloons, laden Sie die Bibliothek für diese Aufgabe herunter und binden Sie diese in das Modul ein. Eine Beschreibung der Bibliothek finden Sie in der Dokumentation. Erstellen Sie im Modul ein neues Paket namens balloons und legen Sie sämtliche Klassen dieser Aufgabe in diesem Package an.

Legen Sie eine Klasse BalloonGame an, die von gdi.game.sprite.SpriteWorld erbt. Machen Sie sich außerdem mit der Dokumentation von SpriteWorld vertraut. SpriteWorld erbt Methoden aus mehreren Klassen. Verschaffen Sie sich insbesondere einen Überblick über die grundlegende Funktionalität der Klassen World und AbstractSpriteWorld.
Die Welt soll die Größe  800×600  Pixel (Breite x Höhe) haben. (Die Fenstergröße kann je nach Betriebssystem etwas davon abweichen.)
Legen Sie in einer Klasse Main eine main()-Methode an und starten Sie darin das Spiel mithilfe der run()-Methode. Sie sollten jetzt sehen, dass sich ein Fenster öffnet. Testen Sie Ihr Spiel regelmäßig im Verlauf der Aufgabe.
Überschreiben Sie in BalloonGamedie Methode setupWorld() so, dass der Titel des Fensters "Balloon Game" lautet.
Überschreiben Sie die Methode renderBackground() so, dass ein hellblauer Hintergrund gezeichnet wird. Machen Sie sich dazu grob mit der Java-Klasse <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/Graphics2D.html" target=_blank>Graphics2D</a> vertraut. Konfigurieren Sie zunächst die richtige Farbe für das übergebene Graphics2D-Objekt und zeichnen Sie danach mit der Methode <a href="https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html#fillRect(int,%20int,%20int,%20int)">fillRect()</a> ein ausgefülltes Rechteck in der richtigen Größe.

Implementieren Sie eine Klasse Balloon, die von gdi.game.sprite.Sprite erbt.
Balloon-Objekte sollen über Konstruktor-Parameter an einer konkreten (x, y)-Koordinate innerhalb der Welt platziert werden können.
Ein normaler Luftballon ist ein perfekter Kreis und hat einen Durchmesser von 100 Pixeln.
Luftballons besitzen eine zufällige Farbe (<a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/Color.html" target=_blank>java.awt.Color</a>). Ihr Spiel soll mindestens fünf unterschiedliche Farben für Luftballons unterstützen.

Hinweis: Sie können Luftballons erst nach Bearbeiten von Teilaufgabe 5 im Spielfenster sehen.

Ein BalloonGame soll (zunächst) drei Luftballons besitzen. Diese sollen zum Spielbeginn zufällig in der Welt verteilt werden; das linke Drittel soll dabei leer bleiben.

Überschreiben Sie in der Klasse Balloondie Methode renderLocal() und zeichnen Sie den Luftballon mittig an seiner (x, y)-Position. Wenn Sie möchten, können Sie auch den unteren Zipfel des Luftballons ergänzen. Dieser soll jedoch für die weitere Aufgabe keine Bedeutung mehr haben.
Beachten Sie zum Rendern folgende zwei Hinweise:
Hinweis 1: In der Computergrafik ist es üblich, dass der Ursprung eines Koordinatensystems in der linken oberen Ecke liegt. Die x-Achse verläuft nach rechts, die y-Achse nach unten.
Hinweis 2: Die Methode renderLocal() besitzt ein eigenes Koordinatensystem, dessen Ursprung genau an der aktuellen (Welt-)Position des Sprites liegt. In der Dokumentation zur Klasse AbstractSpriteWorld finden Sie ein Bild, das dieses Koordinatensystem (Sprite Space) visualisiert. Wenn Sie also in renderLocal() eine der Methoden von <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.desktop/java/awt/Graphics2D.html" target=_blank>Graphics2D</a> mit der Koordinate (0, 0) aufrufen, erscheint das Gezeichnete (bzw. seine linke obere Ecke) nicht etwa an der Welt-Koordinate (0, 0) sondern direkt an der Welt-Position des Sprites. 

Luftballons sollen mit einer konstanter Geschwindigkeit von 100 Pixeln pro Sekunde immer wieder abwechselnd nach oben und unten schweben. Überschreiben Sie dazu die Methode update().
Beachten Sie für die Bewegung Folgendes: Damit eine gleichmäßige Animation entsteht, muss sich ein Luftballon in jeder (Milli-)Sekunde um die gleiche Distanz bewegen. Die Methode update() wird zwar regelmäßig, aber nicht in perfekt gleichen zeitlichen Abständen aufgerufen. Die Bewegung um eine konstante Distanz würde somit nicht nur leicht ruckeln sondern auch auf unterschiedlichen Computern unterschiedlich schnell ablaufen. Berücksichtigen Sie daher für Ihre Animation die vergangene Zeit (in Sekunden) deltaTime seit dem letzten update()-Aufruf.
Achten Sie darauf, dass ein Luftballon nie komplett aus der sichtbaren Welt verschwindet, sondern immer mindestens der halbe Luftballon zu sehen ist. (Dass das Fenster kleiner und größer skaliert werden kann und dadurch Teile der Welt ausgeblendet werden können, dürfen Sie ignorieren.)

Ein BalloonGame soll dem Spieler oder der Spielerin außerdem einen Dartpfeil zur Verfügung stellen. Erstellen Sie dazu eine Klasse Dart. Zu Beginn des Spiels soll der Dartpfeil am linken Rand auf mittlerer Höhe platziert werden und horizontal ausgerichtet sein. Ein Dartpfeil hat die Länge 60 Pixel und soll als schwarze Linie gerendert werden.

 Bevor ein Dartpfeil abgeschossen wird, hat der Spieler oder die Spielerin die Möglichkeit, mit den Tastatur-Pfeiltasten Oben (key code 38) und Unten (key code 40) den Abschusswinkel des Dartpfeils zu verändern. Nutzen Sie die vererbte Methode keyDown() in der BalloonGame-Klasse, um die vom User gedrückte Taste abzufragen. Wenn eine der Tasten gedrückt ist, soll der Dartpfeil in einer gleichmäßigen Bewegung entsprechend nach oben oder unten rotiert werden. Wählen Sie selbstständig eine sinnvolle Rotationsgeschwindigkeit.
Die Information, dass der Spieler oder die Spielerin den Dartpfeil im aktuellen Frame nach oben oder unten rotieren möchte, ist über die keyDown()-Methode nur in der Klasse BalloonGame bekannt. Benötigt wird die Information allerdings in der Klasse Dartin der update()-Methode. Erweitern Sie Ihren Code so, dass das BalloonGame die Information an die Dart-Klasse weitergeben kann und dass ein Dartpfeil die Information zum richtigen Zeitpunkt, nämlich wenn die update()-Methode aufgerufen wird, verarbeiten kann.
Sie dürfen für die Rotation entweder bestehende Funktionalität des Frameworks verwenden oder die Rotation manuell berechnen/rendern.
Achten Sie darauf, dass der Dartpfeil nur in den sichtbaren Bereich abgeschossen werden kann (also nicht auf die linke Seite rotiert werden kann).

Sobald der User die Leertaste (key code 32) drückt und wieder loslässt (keyUp()), soll der Dartpfeil entsprechend seines Winkels abgeschossen werden.
Sorgen Sie wieder dafür, dass die Information aus der BalloonGame-Klasse an den Dartpfeil weitergeleitet wird. Wie bei allen Teilaufgaben gilt weiterhin: Entscheiden Sie selbstständig, wann Sie zusätzliche Methoden und Attribute brauchen.
Ein Dartpfeil fliegt mit einer Geschwindigkeit von 600 Pixeln pro Sekunde.
Nach einem abgeschlossenen Schuss soll dem Spieler oder der Spielerin ein neuer Dartpfeil zur Verfügung gestellt werden.

Tipp: Berechnen Sie zuerst den Richtungvektor, um den sich der Dartpfeil pro Sekunde bewegt. Die Richtung entspricht dem Winkel des Dartpfeils und die Länge dessen Geschwindigkeit. Teilen Sie den Vektor danach in eine horizontale und eine vertikale Komponente auf (siehe Bild unten). Nutzen Sie dazu die mathematischen Eigenschaften eines rechtwinkligen Dreiecks.

Während des Fluges wirkt die Erdanziehung auf den Dartpfeil. Beachten Sie dazu auch das Demo-Video oben in der Aufgabe.
Ändern Sie die Flugbahn des Dartpfeils so ab, dass er zusätzlich zu seiner Flugbewegung in jedem Frame durch die Gravitation nach unten gezogen wird. Das heißt, in jedem Frame wirkt eine zusätzliche, abwärts gerichtete Geschwindigkeit von  490.5px𝑠2⋅𝑡  Pixeln pro Sekunde, die den Dartpfeil dauerhaft ablenkt.  𝑡  bezeichnet dabei die vergangene Zeit in Sekunden seit dem letzten Frame.
Passen Sie den Winkel des Dartpfeils entsprechend seiner Flugbahn an.

Sobald die vordere Spitze des Dartpfeils einen Luftballon berührt, platzt dieser, d.h. er soll aus dem Spiel entfernt werden. Implementieren Sie diese Spiellogik in der BalloonGame-Klasse und besorgen Sie sich die benötigten Informationen aus den Klassen Dart und Balloon.

Sind keine Luftballons mehr im Spiel vorhanden, soll dem Spieler oder der Spielerin eine Nachricht über den Sieg angezeigt werden und das Spiel gestoppt werden. Ob Sie die Siegesmeldung im Spielfenster oder in der Konsole anzeigen, bleibt Ihnen überlassen.

Hinweis: Die Methode stop() stoppt das Spiel so schnell wie möglich, aber es kann passieren, dass die update()-Methode vorher noch einmal aufgerufen wird. Sollte dadurch eine Siegesmeldung auf der Konsole doppelt erscheinen, ist das in Ordnung.

Zusätzlich zu den normalen Luftballons soll es besonders robuste Luftballons geben, die zwei Treffer aushalten, bevor sie platzen. Implementieren Sie das Verhalten in einer Klasse ToughBalloon.
Nach dem ersten Treffer ändert der Ballon seine Farbe.
Bei jedem anfänglichen Luftballon im Spiel soll es sich mit einer Wahrscheinlichkeit von 25% um so einen robusten Luftballon handeln.

Schließlich soll es noch eine dritte Variante von Luftballon geben: TrojanBalloon. Wird ein trojanischer Luftballon abgeschossen, platzt er und lässt in seiner Nähe zwei kleinere normale Luftballons frei, die noch abgeschossen werden müssen.
Bei jedem anfänglichen Luftballon im Spiel soll es sich mit einer Wahrscheinlichkeit von 25% um einen trojanischen Luftballon handeln.

Geben Sie den Paket-Ordner balloons , in dem sich die Dateien Balloon.java, BalloonGame.java, Dart.java, Main.java, ToughBalloon.java und TrojanBalloon.java befinden, im Code-Review-Service mit ab.Achten Sie darauf, nicht den gleichnamigen Modul-Ordner abzugeben!
Wenn Sie möchten, können Sie Ihr Programm noch durch eigene Spielideen und Features erweitern. Wir würden uns freuen, viele kreative Spielvarianten zu sehen! 
Beschreiben Sie alle zusätzlichen Features.


Unterteilung der Bewegung in eine horizontale und vertikale Komponente.