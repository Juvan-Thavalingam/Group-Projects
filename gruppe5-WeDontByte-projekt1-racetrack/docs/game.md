# Game Tests
Zurück zur [Testübersicht](). <br />
Zurück zum [ReadMe]().
## Übersicht

1. [willCarCrash]()
2. [calculatePath]()

### willCarCrash
Die Beschreibung des 2. Testfalls...

**Code des Testfalls** 
```
@Test
public void willCarCrash() {
    
}
```

**Resultat des Codes** <br />
Das Resultat des Tests ist wahr, weil alle Anforderungen erfüllt sind.
Der Test ist erfolgreich.

### calculatePath
Die Beschreibung des 2. Testfalls...

**Code des Testfalls** 
```
@Test
public void calculatePath() {
    
}
```

**Resultat des Codes** <br />
Das Resultat des Tests ist wahr, weil alle Anforderungen erfüllt sind.
Der Test ist erfolgreich.

### Autos werden richtig gewechselt
Bei diesem Test wird überprüft, ob die Autos richtig gewechselt werden.
Der Test wird mit drei verschiedenen Strecken gemacht, um zu überprüfen, ob es auch mit unterschiedlich vielen Autos funktioniert.

**Code des Testfalls** 
```
@Test
void switchToNextCar() throws InvalidTrackFormatException, IOException {

	Game game1C = new Game(new Track(new File(SRC_TEST_TRACK_ONE_CAR)));
	Game game2C = new Game(new Track(new File(SRC_TEST_TRACK_TWO_CAR)));
	Game game3C = new Game(new Track(new File(SRC_TEST_TRACK_THREE_CAR)));


	// Test with one car
	game1C.switchToNextActiveCar();
	assertEquals(0, game1C.getCurrentCarIndex());

	game1C.switchToNextActiveCar();
	assertEquals(0, game1C.getCurrentCarIndex());


	// Test with two car
	game2C.switchToNextActiveCar();
	assertEquals(1, game2C.getCurrentCarIndex());

	game2C.switchToNextActiveCar();
	assertEquals(0, game2C.getCurrentCarIndex());


	// Test with three car
	game3C.switchToNextActiveCar();
	assertEquals(1, game3C.getCurrentCarIndex());

	game3C.switchToNextActiveCar();
	assertEquals(2, game3C.getCurrentCarIndex());

	game3C.switchToNextActiveCar();
	assertEquals(0, game3C.getCurrentCarIndex());
}
```

**Resultat des Codes** <br />
Alle Tests sind positiv, somit funktioniert der Spielerwechsel einwandfrei.
