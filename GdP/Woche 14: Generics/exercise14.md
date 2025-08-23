Aufgabe 1: Würfel-Historie

Schwierigkeitsgrad: ★★☆☆☆
Zeitaufwand: ◑
Code-Review-Service: nein
Keywords: Zufallszahlen, Seeds

Schreiben Sie ein Programm, das einen 10-seitigen Würfel solange würfelt, bis eine 10 gewürfelt wurde. Geben Sie am Ende zuerst die gesamte Würfelsumme aus und anschließend die Würfelwerte in der Reihenfolge, in der sie gewürfelt wurden. Nutzen Sie dazu die LinkedList-Klasse. Verwenden Sie für die Zufallszahlen nicht Math.random(), sondern die Klasse Random, mit der Sie direkt Ganzzahlen in einem gewünschten Wertebereich generieren können.

Beim Programmieren mit Zufallszahlen ist es besonders schwierig, Bugs zu finden, wenn diese von den Zufallszahlen abhängig sind. Deshalb ist es oft sinnvoll, während der Entwicklungsphase mit gleichbleibenden Zufallszahlen zu arbeiten, damit Fehler reproduziert werden können. Die Klasse Random bietet dazu einen Konstruktor an, dem ein Seed übergeben wird. Die Zufallszahlen werden dann in Abhängigkeit von diesem Seed berechnet. Solange also der gleiche Seed verwendet wird, werden auch die gleichen Zufallszahlen berechnet. Ändern Sie Ihr Programm so ab, das ein gleichbleibender Seed verwendet wird. Kontrollieren Sie, dass Ihr Programm beim wiederholten Starten die gleichen Zahlen würfelt.
Aufgabe 2: Generische Lauflängenkodierung

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◕
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: Generische Datentypen, Testen

In Woche 13 haben Sie eine verkettete Liste implementiert, die einen langen Text mit einer Lauflängenkodierung platzeffizient abspeichert. In dieser Woche soll die Implementierung nun so abgeändert werden, dass die Liste generische Datentypen abspeichert (nicht mehr nur char) .

Laden Sie die Musterlösung zu der Aufgabe aus Woche 13 herunter und ändern Sie die Klassen CharList, RunLengthEncodedList und RunLengthEncodedNode so ab, dass diese generische Datentypen verwenden. Benennen Sie dazu auch das Interface CharList in GenericList um.

Die Methode toString() aus dem Interface GenericList soll weiterhin die gesamte Liste als String zurückgeben (d.h. es soll für jedes Element toString() aufgerufen werden).
Der Testcase soll weiterhin mit Buchstaben testen. Wenn Sie möchten, können Sie zusätzliche Tests für andere Datentypen ergänzen.
Aufgabe 3: Yoga-Choreographie

Schwierigkeitsgrad: ★★★★★
Zeitaufwand: ◍◍◍◍◍
Code-Review-Service: ja, geben Sie die Dateien hier ab
Keywords: JSON, Spezifikation, Rekursion, Backtracking, Memoization

In dieser Aufgabe schreiben Sie ein Programm, das für eine vorgegebene Dauer eine möglichst passgenaue Yoga-Choreographie erstellt.

Allgemeine Beschreibung

Gegeben sei ein fiktives Unternehmen, das seinen Kundinnen und Kunden ein Programm zur Verfügung stellt, mit dem sie sich ihre individuelle Yoga-Choreographie generieren lassen können. Damit die Kundinnen und Kunden ohne Trainerin oder Trainer die Yoga-Posen genau nachvollziehen können, hat das Unternehmen ein Model beauftragt, mit dem die angebotenen Yoga-Posen gefilmt wurden. Jeder Video-Clip ist dabei genau so lang, wie die Kundinnen und Kunden die jeweilige Pose halten müssen (z.B. "Plank" für 18 Sekunden).

Zusätzlich wurden mehrere Übergänge zwischen den verschiedenen Posen gefilmt (z.B. von "Plank" zu "Downward-Facing Dog"). Auch diese Übergang-Video-Clips haben eine konkrete Länge.
Da der Aufwand allerdings zu groß wäre, von allen  𝑛  Posen zu allen  𝑛−1 -anderen Posen einen Übergang zu filmen, existiert nicht zu jedem Posen-Paar ein gefilmter Übergang. Es ist allerdings sichergestellt, dass es von jeder Pose einen Übergang zu mindestens einer anderen Pose gibt. Übergänge von einer Pose zu der gleichen Pose gibt es nicht.

Das Ziel des von Ihnen zu implementierenden Programmes ist es, dass der User eine Zeitangabe vorgibt (z.B. 20 Minuten) und Ihr Programm dann die Video-Clips der verfügbaren Yoga-Posen und der Übergänge zwischen den Posen so kombiniert, dass am Ende eine Choreographie erstellt wurde, deren Dauer so genau wie möglich der Zeitangabe entspricht. Falls es keine perfekte Lösung gibt, soll die nächstbessere kürzere oder längere Lösung gefunden werden (ggf. sogar eine leere Lösung).

Beispiel 1

Gewünschte Dauer: 60 Sekunden
Generierte Choreographie:

1. Pose [videoId=1, name="Boat", durationInSeconds=30, trainedParts=[ABDOMEN]]
2. Transition [videoId=101, name="Boat->Cat", durationInSeconds=10, from=Boat, to=Cat]
3. Pose [videoId=3, name="Cat", durationInSeconds=6, trainedParts=[ABDOMEN, BACK]]
4. Transition [videoId=110, name="Cat->Cow", durationInSeconds=8, from=Cat, to=Cow]
5. Pose [videoId=6, name="Cow", durationInSeconds=6, trainedParts=[ABDOMEN, BACK]]
Total duration: 60/60

Beispiel 2

Gewünschte Dauer: 49 Sekunden
Generierte Choreographie:

1. Pose [videoId=2, name="Bridge", durationInSeconds=24, trainedParts=[ABDOMEN, BACK, HAMSTRINGS]]
2. Transition [videoId=108, name="Bridge->Seated Forward Bend", durationInSeconds=10, from=Bridge, to=Seated Forward Bend]
3. Pose [videoId=18, name="Seated Forward Bend", durationInSeconds=14, trainedParts=[LEGS, HAMSTRINGS]]
Total duration: 48/49

JSON

JSON ist ein Dateiformat, mit dem Objekte gut leserlich in einer Text-Datei gespeichert werden können. Anschließend können die Objekte vergleichsweise einfach von einem Programm wieder eingelesen werden. Die zur Verfügung stehenden Yoga-Posen und Übergänge sollen für diese Aufgabe als JSON-Dateien übergeben werden.

Um die zwei (beispielhaften) Yoga-Posen "Boat" und "Bridge" an das Programm zu übergeben, muss somit eine JSON-Datei existieren, die folgendermaßen aussieht:

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
11
12
13
14
[
    {
        "videoId": 1,
        "name": "Boat",
        "trainedParts": ["ABDOMEN"],
        "durationInSeconds": 30
    },
    {
        "videoId": 2,
        "name": "Bridge",
        "trainedParts": ["ABDOMEN", "BACK", "HAMSTRINGS"],
        "durationInSeconds": 24
    }
]
Download
Sie müssen sich für diese Aufgabe nicht selbst darum kümmern, die Dateien einzulesen. Die Klasse Database im zur Verfügung gestellten Framework liest die übergebenen JSON-Dateien ein und gibt Ihnen die Werte als Java-Objekte zurück.

Sie können zum Testen Ihres Programmes die zur Verfügung gestellten JSON-Dateien yoga_poses.json und yoga_transitions.json verwenden. Achten Sie allerdings darauf, dass Ihr Programm allgemeingültig bleibt und auch für andere Yoga-Posen und Übergänge funktioniert.
Konkret heißt das: Das GdP-Team wird für den Code-Review-Service andere Yoga-Daten verwenden, um Ihren Code zu testen (Diese werden zusammen mit der Musterlösung veröffentlicht).

Tipp: Die Yoga-Posen und Übergänge in den Dateien haben alle eine gerade Anzahl an Sekunden. Das heißt, Sie können sehr einfach testen, ob Ihr Programm auch dann Ergebnisse findet, wenn keine perfekte Lösung existiert, indem Sie das Programm mit einer ungeraden Dauer starten.

Aufgabe

Erstellen Sie ein neues Modul yoga. Laden Sie die vorgegebene Bibliothek herunter und binden Sie diese in Ihr Modul ein. Eine Dokumentation zu der Bibliothek finden Sie hier.

Erstellen Sie eine Klasse SessionDesigner samt main-Methode. Implementieren Sie in dieser Klasse die oben beschriebene Funktionalität. Beachten Sie dazu die folgenden Punkte:
Die Zeitangabe des Users und die verwendeten Yoga-Daten sollen über die Runtime Arguments (d.h. main-Parameter) nach einem vorgegebenen Schema erfolgen. Der User Ihres Programmes soll dazu beim Programmstart die gewünschte Dauer als ganze Sekunden und danach die beiden Pfade zu den Yoga-Posen und Übergängen (in dieser Reihenfolge) übergeben. Zum Beispiel für eine Minute (d.h. 60 Sekunden):
java SessionDesigner 60 my/path/to/yoga_poses.json my/path/to/yoga_transitions.json

Das Programm soll anschließend die generierte Choreographie zusammen mit der tatsächlich Dauer im Vergleich zu der gewünschten Dauer nach dem folgenden Schema ausgeben (beachten Sie die Beispielausgaben oben!):
1. Pose x
2. Transition y
...
n. Pose z
Total duration: tatsächliche Dauer/gewünschte Dauer

Falls es mehrere Lösungen gibt, die gleich gut sind, ist es egal, welche der Lösungen am Ende ausgegeben wird.

Ihr Algorithmus soll das Problem rekursiv lösen. Sie dürfen innerhalb Ihrer Lösung natürlich auch Schleifen verwenden, allerdings soll das eigentliche Kernproblem (d.h. die verschiedenen Video-Clips miteinander zu kombinieren) rekursiv gelöst werden. Wenn Sie sich unsicher sind, ob Sie an einer bestimmten Stelle Schleifen verwenden dürfen, fragen Sie in einer Rechnerübung nach.

Eine (nicht leere) Choreographie muss immer mit einer Yoga-Pose beginnen und mit einer Yoga-Pose enden. Zwischen zwei Yoga-Posen muss immer ein passender Übergang stattfinden. Zwischen zwei Übergängen muss immer die passende Yoga-Pose stehen. Falls eine Yoga-Pose und ein Übergang nicht zusammenpassen, können sie nicht direkt miteinander kombiniert werden.

Yoga-Posen und Übergänge dürfen in einer Choreographie mehrfach vorkommen.

Zu dieser Teilaufgabe soll in einem PDF eine kurze schriftliche Erklärung abgegeben werden, wie Ihr implementierter Algorithmus funktioniert. Dieses Erklär-PDF soll nicht die Kommentare in Ihrem Code ersetzen, sondern Ihren Code ergänzen und Ihre Tutorinnen und Tutoren beim Korrigieren unterstützen.
Die Erklärung soll nicht länger als eine halbe Seite sein (sinnvolle Schriftgröße). Sie dürfen Bilder ins PDF ergänzen, falls diese bei der Erklärung helfen. Ausschnitte Ihres Codes sollen soweit wie möglich vermieden werden.

Solange Sie Ihr Programm mit einer Zeitangabe von maximal 2 Minuten starten, sollte Ihr Programm relativ schnell terminieren. Sobald allerdings längere Choreographien gewünscht werden, wird Ihr Programm vermutlich sehr lange laufen.
Beschleunigen Sie die Ausführung Ihres Programmes, indem Sie Memoization verwenden.
Über die Runtime Arguments soll gesteuert werden, ob das Programm in seiner unbeschleunigten Fassung (Teilaufgabe 2) oder in der beschleunigten Fassung (Teilaufgabe 3) laufen soll. Sobald der Parameter -accelerated als letzter Parameter übergeben wird, soll die beschleunigte Version laufen.
Tipp: Indem Sie jetzt Ihren funktionierenden Code aus Teilaufgabe 2 abändern, laufen Sie Gefahr, dass Sie aus Versehen Bugs erzeugen, mit denen Ihre alte Lösung nicht mehr funktioniert. Nutzen Sie Versionskontrolle oder zumindest eine Sicherungskopie der Java-Datei, um Ihren Fortschritt nicht zu verlieren.

Geben Sie zusätzlich zu der oben beschriebenen Ausgabe am Ende aus, wie lange (in Sekunden) die Berechnung gedauert hat. Nutzen Sie dazu die Methode <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/System.html#nanoTime()" target=_blank>System.nanoTime()</a>.

Das Ziel sollte es sein, dass Ihr Programm mit den vorgegebenen Yoga-Posen und Übergängen und für eine gewünschte Dauer von 1801 Sekunden ein Ergebnis in unter einer Minute liefert (Referenzsystem ist der Informatik-CIP).

Erklären Sie wieder auf maximal einer halben PDF-Seite, wie Sie die Beschleunigung umgesetzt haben. Die Erklärungen von beiden Teilaufgaben sollen auf einer gemeinsamen PDF-Seite stehen.

Für alle, die schon mehr Vorwissen haben: Das Parallelisieren des Programmes zur Beschleunigung ist für diese Aufgabe nicht erlaubt.

 Geben Sie die Datei SessionDesigner.java und das Erklär-PDF  hier ab.
Achten Sie bei der Abgabe darauf, dass Ihr Code viele Kommentare enthält, die Ihren Code erklären (am besten dokumentieren Sie den Code noch zusätzlich mit JavaDoc).

