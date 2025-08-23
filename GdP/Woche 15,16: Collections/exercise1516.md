Aufgabe 2: Liste Sortieren

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◑
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: Sortieren, Comparable

In Woche 10 haben Sie eine rekursive Implementierung der Binären Suche geschrieben. Gegeben war dazu ein Array von Studierenden, in dem Studierende anhand ihrer Matrikelnummer gesucht werden können.
Die Aufgabe war an zwei Stellen unnötig einschränkend:

Sie mussten davon ausgehen, dass das Array sortiert ist – andernfalls funktioniert die Binäre Suche nicht.
Gearbeitet wurde mit einem Array. Neue Studierende im laufenden Programm hinzuzufügen, z.B. nach der ersten Suche, ist unnötig umständlich, da neue Arrays erstellt und Daten kopiert werden müssen.
In dieser Aufgabe soll das Programm so abgeändert werden, dass beide Probleme gelöst werden.

Aufgabe

Tauschen Sie den Datentyp Student[] durch eine Liste aus, die es ermöglicht, komfortabel Elemente hinzuzufügen und zu entfernen. Mit dem Anwendungsfall der Binären Suche im Hinterkopf: Ist die ArrayList oder die LinkedList besser geeignet?

Die Klasse Collections besteht ausschließlich aus Klassenmethoden und stellt viele praktische Funktionen für den Umgang mit Listen und Co. zur Verfügung. Eine dieser nützlichen Funktionen ist <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collections.html#sort(java.util.List)" target=_blank>sort(List<T> list)</a>, die eine Liste übergeben bekommt und diese aufsteigend sortiert. Voraussetzung dafür ist, dass die Elemente der Liste das Comparable-Interface implementieren: <T extends Comparable<? super T>>. Lies: "Der Listenelement-Datentyp T (in dieser Aufgabe Student) muss das Comparable-Interface implementieren (extends), welches Objekte vom Typ T oder Elternklassen davon (<? super T>) miteinander vergleichen kann."
Lassen Sie die Klasse Student das Comparable-Interface implementieren und sorgen Sie dafür, dass zwei Studierende anhand ihrer Matrikelnummer eine aufsteigende Ordnung haben.
Nutzen Sie die sort()-Methode in Ihrem Programm, sodass die Binäre Suche immer auf sortierten Daten arbeitet.
Aufgabe 3: Gleichheit

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◑
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: Generische Datentypen, Überschreiben, equals(), hashCode()

Sie haben die Klasse Student sowohl in Verbindung mit Arrays als auch mit Listen verwendet. Die Klasse Student ist eine typische Daten-Klasse, die Informationen kapselt (Matrikelnummer und Name), aber selbst keine oder nur wenig Funktionalität zur Verfügung stellt. Für solche Daten-Klassen bietet es sich an, ihre Gleichheit neu zu definieren.

Matrikelnummern identifizieren Studierende eindeutig. Zwei unterschiedliche Studierende können niemals die gleiche Matrikelnummer haben. Somit müssen zwei Student-Objekte, die die gleiche Matrikelnummer kapseln, zwangsläufig den gleichen Studenten oder die gleiche Studentin beschreiben.

Überschreiben Sie die <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object)" target=_blank>equals()</a>-Methode so, dass zwei Student-Instanzen mit der gleichen Matrikelnummer als gleich gelten.
Überprüfen Sie, dass folgende zwei Objekte somit als gleich gelten, obwohl die Referenzen anna und anna2 nicht auf das selbe Objekt verweisen:
Student anna = new Student(1, "Anna");
Student anna2 = new Student(1, "anna");
System.out.println("anna == anna2: " + (anna == anna2)); // false
System.out.println("anna.equals(anna2): " + anna.equals(anna2)); // true

"It is generally necessary to override the hashCode method whenever this method is overridden, so as to maintain the general contract for the hashCode method, which states that equal objects must have equal hash codes." (Dokumentation zur equals()-Methode)
Sobald die equals()-Methode überschrieben wird, muss immer auch die hashcode()-Methode überschrieben werden. Andernfalls kommt es zu schwerwiegenden Problemen, sobald die Klasse in einer Collection-Datenstruktur verwendet wird, die Hashcodes verwendet (z.B. HashMap und HashSet).
Überschreiben Sie die hashCode()-Methode so, dass...
... für das identische Objekt immer derselbe Hashcode zurückgegeben wird (es sei denn, der interne Zustand des Objekts, z.B. die Matrikelnummer, wird in der Zwischenzeit verändert). Verwenden Sie dazu die <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Objects.html#hash(java.lang.Object...)" target=_blank>Objects.hash()</a>-Funktion.
... für zwei gleicheStudent-Objekte der gleiche Hashcode zurückgegeben wird
... möglichst unterschiedliche Hashcodes zurückgegeben werden, wenn es sich um unterschiedliche Student-Objekte handelt.
Aufgabe 4: Einkaufszettel

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◕
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: Collections

1. Schreiben Sie ein Programm, das einen Einkaufszettel darstellt. Das Programm soll drei einfache Funktionen anbieten:

add(String item, int quantity): Fügt das Produkt item mit der Menge quantity hinzu.
remove(String item): Entfernt das Produkt vom Einkaufszettel.
print(): Gibt die Produkte und ihre Menge aus.
2. Erweitern Sie das Programm so, dass mehrere Geschäfte unterstützt werden. Die add()- und remove()-Methode sollen dazu als zusätzlichen ersten Parameter einen String shop übergeben bekommen. Die print()-Methode soll die Produkte dann nach Geschäft gruppiert ausgeben. Beachten Sie die Beispielausgabe unten.

Der englische Begriff shopping list ist in diesem Fall etwas irreführend. Es gibt eine geeignetere Datenstruktur als Listen, um die Aufgabe zu lösen. Lesen Sie sich die passende Dokumentation in Ruhe durch, um möglichst passende Methoden zu finden. Dies erspart Ihnen unter Umständen viel Programmierarbeit.

Beispiel:

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
ShoppingList list = new ShoppingList();
 
list.add("dm", "Hundefutter", 10);
list.add("Norma", "Limo", 6);
list.add("Edeka", "Schokolade", 2);
list.add("Edeka", "Schokolade", 3);
list.add("Edeka", "Tomaten", 10);
list.add("Edeka", "Dose Kidneybohnen", 1);
list.add("Edeka", "Dose Kidneybohnen", 2);
 
list.add("Norma", "Limo", -50);
list.remove("Edeka", "Tomaten");
list.print();
gibt aus:

===================================
||         Shopping list         ||
===================================
Shop 'Edeka':
Schokolade: 5
Dose Kidneybohnen: 3
 
Shop 'dm':
Hundefutter: 10
Aufgabe 5: Messenger

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◍
Code-Review-Service: nein, Sie können sich aber Feedback zu Ihrer Implementierung in den Rechnerübungen holen
Keywords: String Parsing, Collections

Schreiben Sie ein Programm, das Kurznachrichten verwaltet. Das Programm soll seinem User die Möglichkeit geben, Nachrichten zu schreiben und diese mit Tags zu versehen, wenn eine Raute (#) vorangestellt ist. Implementieren Sie die folgende Funktionalität:

Der User soll das Programm über die Kommandozeile steuern. Nutzen Sie dazu die Scanner-Klasse.
Mit einem Befehl post <message> soll die Nachricht message in das System aufgenommen und auf der Konsole ausgegeben werden. Ist die Nachricht länger als 150 Zeichen, soll eine Fehlermeldung angezeigt werden und die Nachricht nicht ins System aufgenommen und auch nicht ausgegeben werden.
Über den Befehl findTag <tag> sollen alle dem System bekannten Nachrichten ausgegeben werden, die eine Raute enthalten, hinter der direkt danach der gesuchte tag-Text steht. Ein Tag darf dabei keine Leerzeichen beeinhalten.
Da zu erwarten ist, dass die Tag-Suche sehr oft verwendet wird, soll beim Befehl findTag keine Suche durchgeführt werden, da diese sehr zeitaufwändig wäre. Stattdessen sollen die Nachrichten schon beim post-Befehl katalogisiert werden, sodass findTag nur noch die passenden Nachrichten abrufen muss.
Der Befehl exit soll das Programm beenden.
Beispiel:

post "This is my first #message!"
> This is my first #message!
post "This is my second #message"
> This is my second #message
post "Saw a #duck on a #skateboard today"
> Saw a #duck on a #skateboard today
post "This will be my last #message for today"
> This will be my last #message for today
findTag #message
> This is my second #message
> This will be my last #message for today
findTag #duck
> Saw a #duck on a #skateboard today
exit

<span style="color: rgb(0,0,255);">Challenge [optional]</span>

Im Beispiel oben ist zu sehen, dass die Nachricht "This is my first #message!" nicht ausgegeben wird, wenn nach dem Tag message gesucht wird, weil das Ausrufezeichen in der Nachricht als Teil des Tags interpretiert wird. Verbessern Sie die Tag-Suche, indem Sie Sonderzeichen nicht mehr als Teil des Tags identifizieren. Nutzen Sie dazu reguläre Ausdrücke (Regex): package java.util.regex