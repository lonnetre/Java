Aufgabe 4: Hero

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◍◍◑
Code-Review-Service:  ja, geben Sie die Dateien Hero.java,Item.java, Test.javaund Arena.javaals Paket hero(Unterordner um zip) hier ab
Keywords: Getter, Setter, Sichtbarkeiten, null, Referenzen, Spiellogik

Diese Aufgabe benötigt mehrere Klassen. Falls Sie mit der Entwicklungsumgebung IntelliJ IDEA arbeiten, empfehlen wir weiterhin, für GdP insgesamt nur ein einziges Projekt zu verwenden, aber ab sofort jede Übungsaufgabe, die mehrere Klassen benötigt, in einem eigenen Paket (package) zu bearbeiten. Pakete sind dazu da, mehrere thematisch zusammenpassende Klassen zu gruppieren.
Sie können ein neues Paket erstellen, wenn Sie per Rechtsklick auf den src-Ordner die Optionen "New" → "Package" auswählen. Nennen Sie das Paket für diese Aufgabe z.B. hero. Neue Klassen werden dann per Rechtsklick auf das Paket mit den Optionen "New" → "Java Class" angelegt. 

In dieser Aufgabe sollen das Zusammenspiel von Klassen sowie der Umgang mit Referenzvariablen und Sichtbarkeiten geübt werden. Dazu programmieren Sie ein vereinfachtes Rollenspiel mit Heldinnen und Helden, die gegeneinander kämpfen. Modellieren Sie genannte Eigenschaften einer Klasse als Attribute und setzen Sie die Sichtbarkeiten (sofern nicht anders gefordert) so restriktiv wie möglich. Dies gilt für diese und alle kommenden Übungsaufgaben sowie in der Klausur.

Erstellen Sie zunächst ein Paket hero und darin die Heldenklasse Hero. Diese wird im Laufe der Aufgabe erweitert.
Ein:e Held:in hat drei wichtige Eigenschaften: einen Namen, Lebens- und Schadenspunkte. Modellieren Sie diese Eigenschaften und implementieren Sie die entsprechenden Getter.
Für den Namen soll auch ein Setter existieren, der aber nur Namen, die aus mindestens einem Zeichen bestehen, zulässt.
Implementieren Sie einen Parameterkonstruktor für die Eigenschaften.
Eine öffentliche Methode void printInfo() soll die Attribute eines Hero sinnvoll ausgeben, z.B.:
Hero 'Dagobert': H: 100, D: 23
Testen Sie Ihre Implementierung, indem Sie in einer Test-Klasse eine main-Methode anlegen, dort Heldinnen und Helden initialisieren und deren Informationen auf der Standardausgabe ausgeben.

Um mehr Schaden anzurichten, können Held:innen Gegenstände verwenden. Erstellen Sie dazu eine Klasse Item.
Ein Gegenstand hat einen Namen, Schadenspunkte und eine eindeutige und konstante Identifikationsnummer (ID). Implementieren Sie Getter für diese Eigenschaften.
Legen Sie einen Parameterkonstruktor an, in dem der Name und die Schadenspunkte mit den übergebenen Werten initialisiert werden. Die Schadenspunkte eines neuen Gegenstands sollen im Intervall [0,100] liegen. Liegt der übergebene Schaden außerhalb, soll stattdessen ein zufälliger Wert im Intervall gewürfelt werden.
Die Identifikationsnummer soll fortlaufend für alle jemals erstellten Gegenstände vergeben werden, d.h., die erste Item-Instanz bekommt die ID 0, die zweite Item-Instanz die ID 1 usw.
Eine öffentliche Methode void printInfo() soll die Attribute eines Gegenstandes ausgeben, z.B.:
Item 'Eisenschwert' (ID: 0): +7 D
Testen Sie Ihre Klasse Item, indem Sie die main-Methode sinnvoll erweitern.

Die Gegenstände eines Hero liegen in einer Ausrüstungstasche.
Erweitern Sie die Klasse Hero so, dass ein Hero standardmäßig max. sechs Gegenstände mitnehmen kann.
Überladen Sie den Konstruktor, sodass mit einem zusätzlichen Parameter eine individuelle Taschengröße im Intervall [1,15] bestimmt werden kann.
Mit einer Methode boolean insertItem(Item) soll der übergebene Gegenstand der Ausrüstungstasche hinzugefügt werden. Der Rückgabewert gibt an, ob dies erfolgreich möglich war.
Eine Methode void printEquipment() soll den Inhalt der Ausrüstungstasche eines Hero ausgeben, z.B.:

Equipment for Hero 'Dagobert':
* Item 'Eisenschwert' (ID: 0): +7 D
* Item 'Mathematik' (ID: 1): +16 D
* ...

Implementieren Sie eine Methode int getEquippedDamage(), die den gesamten Schaden zurückgibt, den ein Held oder eine Heldin anrichten kann (Schadenspunkte des Hero zuzüglich der Schadenspunkte der Gegenstände).

Mit einer Methode void attackEquipped(Hero) soll ein:e Held:in (aktuelle Instanz) einen übergebenen Hero angreifen. Dabei soll der angerichtete Schaden dem gesamten bewaffneten Schaden des Angreifers entsprechen.

Kämpfe sollen in einer Arena durchgeführt werden. Erstellen Sie dazu eine Klasse Arena.
Implementieren Sie die Klassenmethode Hero fight(Hero, Hero).
Die übergebenen Held:innen sollen sich über mehrere Runden hinweg bekämpfen, bis einer besiegt wurde. Der Ablauf einer Runde soll wie folgt modelliert werden:
Es soll per Zufall bestimmt werden, welche:r Held:in zuerst angreifen darf.
Nach jedem Angriff wird getestet, ob der angegriffene Hero besiegt wurde (Lebenspunkte <= 0).
Haben beide Held:innen überlebt, wird die nächste Runde begonnen.
Die Methode soll eine Referenz auf den Gewinner zurückgeben.
Ergänzen Sie sinnvolle Ausgaben, um einen Kampf nachzuvollziehen, z.B. für jeden Angriff:
Hero 'Dagobert': 100H - 31H -> 69H

Der Ausgang jedes Kampfes ist bisher leicht vorhersehbar. Erweitern Sie daher Ihre Methode(n) aus der vorherigen Teilaufgabe für mehr Spannung um folgende Fähigkeiten, die für alle Held:innen mit je einer bestimmten Wahrscheinlichkeit aktiviert werden. Pro Zug eines Hero können mehrere der Fähigkeiten aktiviert werden. Tipp: Verwenden Sie selbstgeschriebene Hilfsmethoden.
Dimensionssprung: Mit einer Wahrscheinlichkeit von 30% kann ein:e Held:in den Angriffen in dieser Runde in eine andere Dimension ausweichen.
Tollpatsch: Mit einer Wahrscheinlichkeit von 20% verliert ein:e Held:in einen zufälligen Gegenstand, direkt nachdem er oder sie den anderen Hero erfolgreich angegriffen hat.
Dieb: Mit einer Wahrscheinlichkeit von 40% kann ein:e Held:in direkt nach dem Angriff dem Gegner eines Gegenstand abnehmen. Falls in der eigenen Ausrüstungstasche noch Platz ist, wird dieser Gegenstand dann der Tasche hinzugefügt. Gegenstände können trotz Dimensionssprung des Gegners gestohlen werden.
Doppelangriff: Mit einer Wahrscheinlichkeit von 10% kann ein:e Held:in in einer Runde ein zweites Mal ohne die Gegenstände zuschlagen – es werden dem Gegner nur die Schadenspunkte des Angreifers abgezogen.

Lassen Sie zwei Held:innen gegeneinander antreten.
<span style="color: rgb(0,0,255);">[optional]</span>

Erweitern Sie das Programm so, dass beliebig viele Held:innen in einem großen Turnier gegeneinander kämpfen können. Finden Sie eine geeignete Strategie, wer gegen wen antreten soll, um in so wenig Kämpfen wie nötig den stärksten Hero als Gewinner des Turniers zu identifizieren.

Aufgabe 5: Matrix

Schwierigkeitsgrad: ★★★☆☆
Zeitaufwand: ◍◑
Code-Review-Service: ja, geben Sie die Datei Matrix.java hier ab
Keywords: 2D-Arrays, Matrizen 

In dieser Aufgabe arbeiten Sie mit Klassenmethoden und zweidimensionalen double-Arrays. Die erste Dimension zweidimensionaler Arrays soll in dieser Aufgabe die Zeile beschreiben, der zweite Dimension die Spalte innerhalb dieser Zeile. Ihre Methoden sollen für alle Arten von zweidimensionalen Arrays funktionieren – auch wenn deren Zeilen unterschiedlich lang sind!

Achten Sie darauf, dass Ihre Funktionen die übergebenen Arrays nicht verändern. Testen Sie jede Ihrer Methoden ausführlich mit unterschiedlichen Matrizen. Denken Sie dabei auch an mögliche Sonderfälle, wie z.B. die unterschiedlich langen Zeilen oder null-Einträge.

Legen Sie eine Klasse Matrix an. Schreiben Sie eine Klassenmethode print(), der ein zweidimensionales double-Array übergeben wird und die das Array auf der Konsole ausgibt. Beachten Sie dazu folgende Punkte:
Die Methode soll nur Text auf der Konsole ausgeben, aber kein Ergebnis zurückgeben.
Die Methode soll jede Zeile des übergebenen Arrays in einer Zeile auf der Konsole ausgeben, die Spalteneinträge jeweils durch ein Leerzeichen getrennt. Beispiel:
𝚙𝚛𝚒𝚗𝚝((6.21.05.2−2.0)); 
gibt aus:
<pre>6.2 5.2
1.0 -2.0</pre>
Schreiben und Verwenden Sie zum Lösen der Teilaufgabe eine überladene Hilfsmethode (d.h. eine private Methode), die ein eindimensionales Array übergeben bekommt und dieses in einer eigenen Zeile ausgibt.

Schreiben Sie eine Methode multiply(), die als ersten Parameter ein zweidimensionales double-Array und als zweiten einen einzelnen double-Wert übergeben bekommt. Die Methode soll ein neues zweidimensionales Array anlegen und zurückgeben. Dieses Ergebnis-Array soll die Werte des übergebenen Arrays, multipliziert mit dem übergebenen double-Wert, enthalten. Beispiel:
𝚖𝚞𝚕𝚝𝚒𝚙𝚕𝚢((6.21.05.2−2.0),10); 
gibt zurück:
(62.010.052.0−20.0) 

Schreiben Sie eine Methode computeRowMeans(), die als Parameter ein zweidimensionales double-Array übergeben bekommt und als Ergebnis ein eindimensionales zurückgibt. Die Methode soll für jede Zeile den Durchschnitt berechnen und in das Ergebnis-Array einfügen. Beispiel:
𝚌𝚘𝚖𝚙𝚞𝚝𝚎𝚁𝚘𝚠𝙼𝚎𝚊𝚗𝚜((6.21.05.2−2.0)); 

gibt zurück:
(5.7,−0.5) 

Schreiben Sie eine Methode computeColumnMeans(), die als Parameter ein zweidimensionales double-Array übergeben bekommt und als Ergebnis ein eindimensionales zurückgibt. Die Methode soll für jede Spalte den Durchschnitt berechnen und in das Ergebnis-Array einfügen. Beispiel:
𝚌𝚘𝚖𝚙𝚞𝚝𝚎𝙲𝚘𝚕𝚞𝚖𝚗𝙼𝚎𝚊𝚗𝚜((6.21.05.2−2.0)); 

gibt zurück:
(3.6,1.6) 

Schreiben Sie eine Methode linearize(), die als Parameter ein zweidimensionales double-Array und einen Wahrheitswert übergeben bekommt und als Ergebnis ein eindimensionales double-Array zurückgibt. Die Methode soll das übergebene zweidimensionale Array in ein eindimensionales umwandeln und zurückgeben. Ist der Wahrheitswert false, sollen alle Zeilen (bei true alle Spalten) des Arrays nacheinander in das eindimensionale Array geschrieben werden.

Beispiel 1:
𝚕𝚒𝚗𝚎𝚊𝚛𝚒𝚣𝚎((6.21.05.2−2.0),𝚏𝚊𝚕𝚜𝚎); 
gibt zurück:
(6.2,5.2,1.0,−2.0) 

Beispiel 2:
𝚕𝚒𝚗𝚎𝚊𝚛𝚒𝚣𝚎((6.21.05.2−2.0),𝚝𝚛𝚞𝚎); 
gibt zurück:
(6.2,1.0,5.2,−2.0) 