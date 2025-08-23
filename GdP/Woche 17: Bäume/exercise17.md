Aufgabe 2: JavaDoc

Schwierigkeitsgrad: ★★☆☆☆
Zeitaufwand: ◑
Code-Review-Service: nein
Keywords: Dokumentatieren, Kommentieren

In GdP sind Ihnen bisher schon mehrfach JavaDoc-Kommentare begegnet, z.B. in der Klasse Matrix:

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
/**
 * Multiplies all elements in the 2D array by the given factor
 * @param matrix a 2D array (first dimension is the row, second dimension is the column)
 * @param factor a factor to multiply the array with
 * @return a new array whose elements are the product of the given matrix and the factor.
 * The method returns <code>null</code> if the given matrix is <code>null</code>.
 */
public static double[][] multiply(double[][] matrix, double factor) {
    // ...
}
JavaDoc-Kommentare sind dazu da, Benutzern Ihres Code (d.h. in der Regel anderen Programmiererinnen und Programmierern) zu erklären, wie dieser Code verwendet wird, also eine Art Bedienungsanleitung. Im Gegensatz zu normalen Kommentaren im Code, sollte bei JavaDoc-Kommentaren darauf verzichtet werden, Implementationsdetails zu beschreiben (außer es ist für den Benutzer wichtig). Für die reine Bedienbarkeit einer find()-Methode ist es beispielsweise irrelevant, wie diese intern nach einem Element sucht. Wichtig ist stattdessen, dass der Benutzer weiß, wie die Methode zu verwenden ist.

JavaDoc-Kommentare stehen vor allem über Methoden, Klassen und Attributen und beginnen immer mit /** und enden mit */. Sie sehen daher ähnlich zu normalen Blockkommentaren aus, die mit /* beginnen, sind aber eine eigene Art von Kommentar. Die Sternchen zu Beginn jeder Zeile sind optional, aber gebräuchlich (IntelliJ IDEA ergänzt diese automatisch).

JavaDoc-Kommentare sind in der Sprache HTML geschrieben, d.h. es können Tags wie <code> verwendet werden (siehe Beispiel), um Variablen in der Beschreibung hervorzuheben. Nach einem erklärenden Teil, der die Methode, Klasse o.ä. zusammenfasst, wird über tags Genaueres über die Benutzbarkeit angegeben.

Die gebräuchlichsten Tags sind:

Der Tag @param <variable name> gibt an, dass es sich bei <variable name> um einen Parameter der Methode handelt. Direkt dahinter folgt eine kurze Beschreibung des Parameters. Hier stehen beispielsweise auch Sonderfälle, z.B. "... or <code>null</code> if the element is unknown".

Der Tag @return beschreibt, was die Methode zurückgibt. Auch hier stehen überlicherweise Sonderfälle, wie im Beispiel oben der null-Fall, falls die übergebene Matrix null ist.

Der Tag @throws <exception class> gibt an, ob die Methode eine überprüfte Ausnahme (checked Exception) wirft und dahinter, in welchem Fall das passiert.

Der Tag {@link} an einer beliebigen Stelle im JavaDoc-Kommentar erlaubt es, einen Verweis auf eine andere Methode, Klasse, o.ä. zu setzen. Diese sollten allerdings sparsam und nur falls nötig verwendet werden:
1
2
3
4
5
/** Stores all messages that have been posted */
private static final List<String> messages = new ArrayList<>();
 
/** Stores the indices of messages in the {@link #messages}-list by the tags that they contain */
private static final Map<String, Set<Integer>> messagesByTag = new HashMap<>();
Aufgabe

Kommentieren Sie Code Ihrer Wahl mit JavaDoc-Kommentaren. Nutzen Sie beispielsweise die nachfolgende "Binärer Suchbaum"-Aufgabe dafür.
Generieren Sie die HTML-Seiten für Ihre Dokumentation. Klicken Sie dazu in IntelliJ IDEA auf Tools → Generate JavaDoc... Anschließend können Sie auswählen, in welchem Umfang die Dokumentation erzeugt werden soll (ganzes Projekt, Modul, etc.) und ab welcher Sichtbarkeit Dokumentation enthalten sein soll (z.B. nur für öffentliche Klassen). Anschließend sollte sich die generierte Dokumentation automatisch in Ihrem Browser öffnen. Falls nicht, öffnen Sie manuell die index.html-Datei, die im angegebenen Output directory erzeugt wurde.
Falls der Fehler "unmappable character" auftritt, tragen Sie in das Feld "Command line arguments" Folgendes ein: -encoding ISO-8859-1
Aufgabe 3: Binärer Suchbaum

Schwierigkeitsgrad: ★★★★☆
Zeitaufwand: ◍◍
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: Sortieren, Tiefensuche, Breitensuche

Letzte Woche haben Sie wieder mit Student-Objekten und Listen gearbeitet. Diese Woche sollen die Student-Objekte statt in einer Liste in einem Binärbaum verwaltet werden.

Implementieren Sie einen binären Suchbaum, in dem Referenzen auf Student-Instanzen gespeichert werden, die anhand ihrer Matrikelnummer sortiert sind. Implementieren Sie dazu eine eigene Knotenklasse StudentNode und eine Baumklasse StudentTree, über die ein Benutzer auf die Daten im Baum zugreifen kann.

Ein StudentTree soll folgende Methoden zur Verfügung stellen:

get(int matriculationNumber): Sucht das Student-Objekt mit der gegebenen Matrikelnummer und gibt dieses zurück. Ist das Objekt nicht im Baum enthalten, soll null zurückgegeben werden.

add(Student student): Fügt die übergebene Student-Instanz dem Baum hinzu. Dabei wird darauf geachtet, dass die Suchbaum-Eigenschaften erhalten bleiben. Beachten Sie, dass niemals zwei Studierende mit der gleichen Matrikelnummer im Baum enthalten sein dürfen.

printDepthFirst(): Gibt den Baum anhand einer Tiefensuche (Kindknoten werden vor Geschwisterknoten besucht) nach dem nachfolgenden Schema aus. Im Beispiel sind die Buchstaben nach ihrer Position im Alphabet sortiert. Linke Knoten werden mit ├── markiert, rechte mit └── .
<pre>└── M
    ├── K
    │   ├── D
    │   │   ├── A
    │   └── L
    └── P
        ├── N
        └── S
            └── U
                ├──T
</pre>

printBreadthFirst(): Gibt den Baum anhand einer Breitensuche (Geschwisterknoten werden vor Kindknoten besucht) nach dem folgenden Schema aus:
<pre>M
K P 
D L N S 
A - - - - - - U
- - - - - - - - - - - - - - T -
</pre>

height(): Gibt die Höhe des Baumes zurück.

remove(int matriculationNumber): Entfernt die Student-Instanz mit der gegebenen Matrikelnummer aus dem Baum. Beim Entfernen wird darauf geachtet, dass die Suchbaum-Eigenschaften erhalten bleiben. Gibt es keine Student-Instanz mit der gesuchten Matrikelnummer, soll eine überprüfte Ausnahme (checked Exception) namens StudentDoesNotExistException geworfen werden.
<span style="color: rgb(0,0,255);">Challenge [optional]</span> Aufgabe 4: Taschenrechner

In den Vorlesungsfolien haben Sie Bäume gesehen, die Rechenoperationen abspeichern (Zahlen und Grundrechenarten). Implementieren Sie einen einfachen Taschenrechner, der Formeln auf der Kommandozeile entgegennimmt, z.B.: 12 + 23 * 4 / 5 - 101 + 56, die Zahlen und Operatoren in einer Baum-Struktur speichert und die Formel schließlich auswertet.