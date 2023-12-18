# Multichat Uebung-hk2-thavajuv-ismlab01

# Ablauf
1: Um eine Verbindung von verschiedenen Clients aufzubauen, müssen sie den Server starten.

2: Danach können sie die Clients starten.

3: Sie können einen Namen eingeben und danach auf die Taste "Connect" drücken. Es wird eine Verbindung zum Server aufgebaut.

4: Unten können sie ihren Text eingeben und danach auf "Send" drücken. Die Nachricht wird abgeschickt und wird in der TextArea angezeigt.
   ACHTUNG: Wenn sie die Nachricht nur einem bestimmten Usen senden wollen, dann müssen sie vor ihrem Text "@username" eingeben!
   
5: Wenn sie die Verbindung abbrechen wollen, dann müssen oben rechts auf "Disconnect" drücken!



# Project Information

* Funktionale Fehler

1: Client funktioniert nicht! -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/28

2: Anonymous Problem! -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/6#issue-77732

3: Client Fix! -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/2#issue-77513

4: Client: MessageField behält den Text! -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/29

5: Nachricht wird nicht angezeigt, wenn an bestimmten Client gesendet wird. -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/30

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

* Strukturelle Fehler

1: Protokoll in ein Daten Objekt -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/15

2: Die Handle Methoden in ClientConnectionHandler und ServerConnectionHandler erweitern -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/21

3: Namen umschreiben einheitlich machen -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/23

4: ClientConnectionHandler und ChatWindowControlller wissen von einander -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/22

5: Server- und ClienentConnectionHandler haben die gleichen Methoden -> https://github.zhaw.ch/PM2-IT21aWIN-fame-rayi-wahl/Uebung-hk2-thavajuv-ismlab01/issues/16


# Klassendiagramm

Mit dieser Klassenstruktur wollten wir die folgenden Dinge erreichen

- Eine lose Kopplung zwischen den Klassen
- Gemeinsame Codebasen für die Verbindungshandler
- Eine gemeinsame Nachricht für die Kommunikation zwischen Clients und Server

 Klassendiagramm:
![Uebung-hk2-thavajuv-ismlab01](https://github.zhaw.ch/storage/user/4860/files/0ba0be48-9401-479a-9fbd-ff1fafb4a2f9)

# Client
Wichtig für uns war, dass der ChatWindowController nichts über den NetworkHandler und die Verbindung weiß. Er sollte nur den ClientConnectionHandler initialisieren und alle UI-bezogenen Dinge behandeln (Knopfdruck, Display-Meldungen, etc.).

Wir haben den ClientState verwendet, um vom ClientConnectionHandler mit dem ChatWindowController zu kommunizieren. Er enthält ein paar beobachtbare Felder (isConnectedProperty, userNameProperty, etc.), die sowohl vom ChatWindowController als auch vom ClientConnectionHandler geändert werden können. Auf diese Weise kann der ChatWindowController auf Änderungen des Zustands achten und entsprechend handeln.

Der ClientConnectionHandler sollte auf einem abstrakten ConnectionHandler basieren, der die Grundfunktionalität eines Verbindungshandlers implementiert (startReceiving, processData, etc.).

Die Nachricht wird für die Kommunikation zwischen den Clients und dem Server verwendet. Sie enthält die folgenden Informationen:

- Sender
- Receiver
- Type
- Payload
Außerdem haben wir ein neues Paket UI für einige der Klassen erstellt.

# Server
Der wichtigste Punkt, den wir hier erreichen wollten, war die Kopplung zwischen ConnectionRegistry und ServerConnectionHandler. Beide mussten sich gegenseitig kennen, da ConnectionRegistry einen Verbindungshandler trennen muss und ServerConnectionHandler über die anderen Verbindungshandler Bescheid wissen muss.

Um dieses Problem zu lösen, haben wir eine Schnittstelle DisposableConnectionHandler geschaffen, die zwei Methoden sendData und disconnect benötigt. Dann muss ConnectionRegistry nur den DisposableConnectionHandler kennen und muss nicht die genaue Implementierung des ServerConnectionHandlers kennen.


  
