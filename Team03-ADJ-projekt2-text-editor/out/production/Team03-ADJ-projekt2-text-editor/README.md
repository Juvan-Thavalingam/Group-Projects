# PM1-IT22a-WIN-dihl-fame-wahl-team03-ADJ-projekt2-text-editor
# Software Overview → Text Editor
### ZHAW Software Engineering Semester 1 Projekt 2

### Geschrieben von: Juvan, Dominique, Aleksandar


## Was?
Der Texteditor erlaubt das hinzufügen, löschen, ersetzen und formatieren von Text in einer Shell Umgebung.
Geschrieben komplett in Java ohne externe dependencies.
Durch Befehle wie ADD, DEL, REPLACE, DUMMY, FORMAT, etc. kann effektiv Text geschrieben und bearbeitet werden.
 
## Wie?

### Software Aufbau:
Das Programm besteht aus folgenden fünf Klassen. 
* Texteditor
* Textprocessor
* OutputManager
* Format
* Index

### Anwendung durch Folgende Befehle: 

* ADD [n]: Ruft nach der Eingabe dieses Kommandos zur Eingabe des Textes für den Absatz auf. Der Absatz wird an Position n eingefügt. Fehlt die Absatznummer, wird dieser am Ende angefügt 
* DEL [n]: Löscht einen Absatz. Fehlt die Absatznummer, wird der letzte Absatz gelöscht. 
* DUMMY [n]: Fügt einen fest einprogrammierten Blindtext ein. Wird keine Absatznummer n angegeben, wird der Absatz am Ende angefügt. 
* EXIT: Beendet das Programm 
* FORMAT RAW : Setzt das Ausgabeformat auf die Ausgabe der Absätze mit vorangestellter Absatznummern.

* FORMAT FIX b: Setzt das Ausgabeformat auf eine Ausgabe mit einer maximalen Spaltenbreite von b Zeichen ein. Das Umbruchverhalten ist wie folgt: 
    * Umgebrochen wird grundsätzlich nur nach einem Leerzeichen. 
    * Das Leerzeichen, nach welchem umgebrochen wird, zählt nicht zur Zeilenlänge dazu. Es wird noch auf der aktuellen Zeile ausgegeben, auch wenn es möglicherweise nicht mehr draufpasst. 
    * Findet sich nach einem Umbruch keine Umbruchstelle innerhalb der Spaltenbreite, wird nach der Spaltenbreite umgebrochen.  Beispiel für FORMAT FIX 20. 
* INDEX: Gibt einen Index (Wortverzeichnis) aller Begriffe aus, die über alle Absätze gesehen öfter als dreimal vorkommen. Ein Begriff beginnt mit einem Grossbuchstaben. Der Index listet die Absätze, wo der jeweilige Begriff vorkommt, als Komma getrennte Zahlenfolge auf. 
    * Beispiel: Tigurini 2,5,10,11 
* PRINT: Ausgabe des Textes gemäss dem aktuell eingestellten Ausgabeformat. 
* REPLACE [n]: Ruft nach der Eingabe dieses Kommandos zuerst zur Eingabe eines zu suchenden Wortes oder Textteils im Absatz n auf und anschliessend zur Eingabe des Textes, mit dem das Gesuchte ersetzt werden soll. Das Suchen und Ersetzten erfolgt pro Absatz, nicht über Absatzgrenzen hinweg. Wird keine Absatznummer n angegeben, wird der letzte Absatz geändert.

# Befehl Cheatsheet

| Befehl       | Beschreibung                                                                                                                                                                                                                                                                                                                                                        |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ADD [n]      | Ruft nach der Eingabe dieses Kommandos zur Eingabe des Textes für den Absatz auf. Der Absatz wird an Position n eingefügt. Fehlt die Absatznummer, wird dieser am Ende angefügt.                                                                                                                                                                                    |
| DEL [n]      | Löscht einen Absatz. Fehlt die Absatznummer, wird der letzte Absatz gelöscht.                                                                                                                                                                                                                                                                                       |
| DUMMY [n]    | Fügt einen fest einprogrammierten Blindtext ein. Wird keine Absatznummer n angegeben, wird der Absatz am Ende angefügt.                                                                                                                                                                                                                                             |
| EXIT         | Beendet das Programm                                                                                                                                                                                                                                                                                                                                                |
| FORMAT RAW   | Setzt das Ausgabeformat auf die Ausgabe der Absätze mit vorangestellter Absatznummern.                                                                                                                                                                                                                                                                              |
| FORMAT FIX b | Setzt das Ausgabeformat auf eine Ausgabe mit einer maximalen Spaltenbreite von b Zeichen ein.                                                                                                                                                                                                                                                                       |
| INDEX        | Gibt einen Index (Wortverzeichnis) aller Begriffe aus, die über alle Absätze gesehen öfter als dreimal vorkommen. Ein Begriff beginnt mit einem Grossbuchstaben. Der Index listet die Absätze, wo der jeweilige Begriff vorkommt, als Komma getrennte Zahlenfolge auf.                                                                                              |
| PRINT        | Ausgabe des Textes gemäss dem aktuell eingestellten Ausgabeformat.                                                                                                                                                                                                                                                                                                  |
| REPLACE [n]  | Ruft nach der Eingabe dieses Kommandos zuerst zur Eingabe eines zu suchenden Wortes oder Textteils im Absatz n auf und anschliessend zur Eingabe des Textes, mit dem das Gesuchte ersetzt werden soll. Das Suchen und Ersetzten erfolgt pro Absatz, nicht über Absatzgrenzen hinweg. Wird keine Absatznummer n angegeben, wird der letzte Absatz geändert. |




## Klassendiagramm:

![](https://res.cloudinary.com/dnk/image/upload/v1668217567/editor-klassendiagramm6_ypxcfq.png)
Klassendiagramm aktuell am 12. November 2022, 02:48 CES.
