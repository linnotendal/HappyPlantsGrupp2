# Happy Plants - Web Application Improvement 

This repository contains a legacy web application originally developed by a 
previous student group. The purpose of this project is to remake, improve, test, 
and extend the application as part of a system development course.

## Project Scope
- Remake from a desktop app to a web app
- Improve code quality and structure
- Implement selected new requirements
- Implement unit tests 

## Contributors
- Linn Otendal (linnotendal)
- Lana Maher (lana2521)
- Christoffer Björnheimer (BJ0RNHEIMER)
- Lilas Beirakdar (LilasBe)
- Ellen-Mae Lantz (MagiicalMae)
- Erik Sjöberg (Er7k)

## Instructions
- Clone or download the project
- Open the project from the folder containing the pom.xml file.
- Ensure Maven dependencies are loaded: right‑click pom.xml and select Add as Maven Project if needed.
- Run the application from PlantappApplication.java (PlantApp2/backend/plantapp/src/main/java/com/happyplants2/plantapp/PlantappApplication.java).
- Open the web app in your browser at http://localhost:8080.


------------------------------------------------------
# Original documentation
The following documentation was included in the original project and has been kept as a reference. Some parts may be outdated or subject to change during the project.

## MyHappyPlants

### Länk till repot på github
https://github.com/antonholmCO/MyHappyPlants

### Produktbeskrivning
My Happy Plants är en applikation tänkt att hjälpa en användare att ta hand om sina växter i hemmet samt ge användaren information om växterna. My Happy Plants använder sig av information hämtad från Trefle.io, som var ett öppet och gratis API som erbjöd information om en miljon växtarter och hybrider. Applikationen omfattar ett färgglatt grafiskt användargränssnitt utvecklat i JavaFX med bilder av illustrerade växter, och ger möjlighet för användaren att söka bland tiotusentals växter, döpa dem och lägga till dem i sitt personliga bibliotek.
Applikationen påminner även användaren när det är tid att vattna, enligt appens beräkning.

### Instruktioner för att köra programmet
1. Se till att alla maven dependencies har laddats in
2. Execute maven goal "mvn javafx:compile"
3. Execute maven goal "mvn javafx:run" för att starta klienten
4. Kör main-metoden i se/myhappyplants/server/StartServer.java för att starta servern


Bilden nedan visar hur man exekverar ett maven goal.

![bild](https://user-images.githubusercontent.com/77005138/114137664-cd6c0d80-990c-11eb-8350-bdc3172e48d7.png)
