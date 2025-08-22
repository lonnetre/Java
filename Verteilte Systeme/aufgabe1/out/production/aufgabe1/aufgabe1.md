Dieses soll es dann ermöglichen, dass ein Methodenaufruf nicht mehr lokal ausgeführt wird, sondern eben entfernt auf einem anderen Rechner. Dabei wird ein Methodenaufruf dann von einer Komponente namens Stub entgegengenommen, die am Client als Platzhalter für das Objekt am Server dient. Der Aufruf wird in eine Nachricht verwandelt und an den Zielrechner versendet. Dort führt dann das sogenannte Skeleton, als Stellvertreter für den Client, den eigentlichen Aufruf durch und schickt das Ergebnis zurück.

Unser Auktionsdienst bietet einem Client die Möglichkeit, eine neue Auktion zu registrieren, die aktuell laufenden Auktionen abzufragen sowie ein neues Gebot zu einer laufenden Auktion abzugeben. Die Klasse VSAuction repräsentiert eine Auktion und enthält den Auktionsnamen und das aktuelle Höchstgebot. Beim Registrieren und Bieten kann der Client einen Eventhandler mitgeben, um über neue Gebote, den Ablauf sowie den Gewinn der Auktion informiert zu werden.

Der Server stellt dabei die Anwendung als Remoteobjekt bereit, sodass diese per Fernaufruf nutzbar wird. Damit der Client später darauf zugreifen kann, muss der Auktionsdienst dann noch mithilfe einer Registry bekannt gemacht werden. Der Client dient dann dazu, um per Fernaufruf auf den Auktionsdienst zuzugreifen und ermöglicht die Nutzung des Dienstes per Kommandozeile.

--------------------

VSConnection + VSObjectConnection:

Für unsere Kommunikationsschicht sind zwei Klassen zu implementieren:
Die VSConnection dient dazu, Byte-Arrays beliebiger Länge zu senden und zu empfangen. 
Für die Übermittlung selbst soll dabei eine TCP-Verbindung verwendet werden. 

Darauf aufbauend gibt es dann die VSObjectConnection, die beliebige Objekte senden und empfangen können soll. 
Dazu muss sich diese um das Marshaling und Unmarshaling kümmern, sodass die Objekte als Nachrichten zwischen den Rechnern übertragen werden können. 
Hierzu gibt es mehr Details im entsprechenden Video.

Der eigentliche Versand der dabei durch das interne Serialisieren entstehenden Binärdaten bzw. Byte-Arrays soll dann mithilfe der VSConnection erfolgen. Java besitzt bereits eine eingebaute Möglichkeit, Objekte zu serialisieren und zu deserialisieren. Allerdings ist das dabei verwendete Datenformat nicht besonders kompakt.

--------------------

Als Teil der erweiterten Übungsaufgabe ist es hier auch das Ziel, die Menge der über das Netzwerk zu übertragenden Daten zu minimieren. Dazu sollen die mittels des von Java bereitgestellten ObjectOutputStreams erzeugten Daten für eine Beispielklasse analysiert und anschließend kompakter codiert werden. Die Beispielklasse besteht aus einem int, einer Zeichenkette und einem Array von Objekten.

Zum Beeinflussen der erzeugten Daten gibt es über die Schnittstelle Externalizable die Möglichkeit, selbst Serialisierungs- und Deserialisierungsmethoden zu implementieren, in denen ein Objekt kompakter codiert werden kann. Um die codierten Binärdaten zur Analyse in Eclipse ausgeben zu können, kann es notwendig sein, diese von Java als Compound Text interpretieren zu lassen.