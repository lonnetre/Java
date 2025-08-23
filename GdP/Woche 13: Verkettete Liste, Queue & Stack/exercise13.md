Aufgabe 3: Lauflängenkodierung

Schwierigkeitsgrad: ★★★★☆
Zeitaufwand: ◍◍◑
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: Listen, Interfaces, Testen

Für ein fiktives wissenschaftliches Experiment sollen die Teilnehmerinnen und Teilnehmer nacheinander verschiedene Tasten auf der Tastatur beliebig lange gedrückt halten und dann wieder loslassen. Dadurch entsteht eine lange Zeichenkette, die über lange Strecken hinweg das gleiche Zeichen beinhaltet, z.B.:

aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbccccccccccccccccccccccccaaaaaaaaaaaaaaaaaaaaaaaaaaakkkkkk...

Die Teilnehmerinnen und Teilnehmer haben außerdem jederzeit die Möglichkeit, ihre Eingabe zu ändern ("Lösche das 315. Zeichen, das ich eingegeben habe", "Ergänze ein 'b' als 12. Zeichen").

Um die Eingabe der Teilnehmerinnen und Teilnehmer mit einem möglichst geringen Speicherverbrauch abzuspeichern, aber trotzdem permanent weitere Tasteneingaben hinzufügen zu können, soll für dieses Experiment eine eigene Datenstruktur implementiert werden, die die Tasteneingabe mit der Lauflängenkodierung komprimiert.

Die Lauflängenkodierung haben Sie schon in der Veranstaltung "Grundlagen der Technischen Informatik" (GTI) kennengelernt. Falls Sie die Veranstaltung nicht belegen/belegt haben oder eine Erinnerung brauchen, finden Sie hier eine Kurzzusammenfassung:

Die Lauflängenkodierung wird verwendet, um Daten mit sich wiederholenden Symbolen zu komprimieren, d.h. mit weniger Speicher als unkomprimiert abzuspeichern. Statt den sich wiederholenden Symbolen selbst wird das Symbol nur einmal und zusätzlich seine Anzahl abgespeichert.

Beispiel:

Der String "AAAAAAAAAAAABBBBBBBB################FFFF" kann zu ('A', 12), ('B', 8), ('#', 16), ('F', 4) komprimiert werden (auch manchmal dargestellt als "A12B8#16F4").

Aufgabe

Implementieren Sie eine einfach verlinkte Liste, in der Zeichen abgespeichert werden. Die Liste soll die Zeichen intern lauflängenkodiert verwalten, d.h. der Text "AABBBBAAADDDDDEEEEEEEEEEE" soll mit nur fünf Knoten gespeichert werden.

Anmerkung: Für diese Aufgabe soll nur die Liste (und eventuell zusätzliche dafür benötigte Datenstrukturen) implementiert werden, nicht das beschriebene Experiment.

Definieren Sie zuerst ein Interface CharList für Ihre lauflängenkodierte Liste. Eine CharList soll folgende öffentliche Methoden zur Verfügung stellen:
void append(char symbol): Fügt das übergebene Zeichen hinten hinzu: append('B') für eine Liste AAAACBB → Liste AAAACBBB
String toString(): Gibt den String zurück, den die Liste repräsentiert: print() für Liste AAAACBB gibt "AAAACBB" zurück
char get(long index): Gibt das Zeichen an der Stelle index (0-basiert) zurück: get(4) für eine Liste AAAACBB gibt 'C' zurück
long size(): Gibt die Anzahl an Zeichen zurück: size() für eine Liste AAAACBB gibt 7 zurück
void insert(char symbol, long index): Fügt das übergebene Zeichen an der Stelle index (0-basiert) hinzu: insert('B', 2) für eine Liste AAAACBB → Liste AABAACBB
char remove(long index): Löscht das Zeichen an der Stelle index(0-basiert) und gibt es zurück: remove(4) für eine Liste AAAACBB → AAAABB und gibt 'C' zurück

Betreiben Sie Test-driven Development und schreiben Sie zuerst einen ausführlichen JUnit-Test, der die Interface-Methoden testet.
Tipp 1: Bei vielen Testcases wird die Ausgabe schnell unübersichtlich. Nutzen Sie innere Klassen und die @Nested-Annotation für sinnvolle Gruppierungen.
Tipp 2: Wenn Sie vor jedem Testcase die gleichen Startbedingungen schaffen müssen, nutzen Sie die @BeforeAll-Annotation.

Implementieren Sie die lauflängenkodierte Liste und testen Sie Ihre Implementierung währenddessen mit dem JUnit-Test. Testen Sie Ihren Code am Ende auch mit dem JUnit-Test der Musterlösung.
Achtung: Der JUnit-Test der Musterlösung testet nicht den internen Zustand der Liste, also z.B. die Anzahl der Knoten, sondern nur die Funktionalität der Interface-Methode.
