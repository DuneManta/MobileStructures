# MobileStructures

A Battletech unit construction companion app
Design concept proposal

## Primer - 

For those already familiar with Battletech tabletop game, skip ahead to the next section for an overview of the app concept.

Battletech is a tabletop miniatures wargame which pits two or more players against each other using a variety of units including tanks, aircraft, and the icon of the setting: Battlemechs. Using dice, pens, paper, and an array of miniatures, players take turns moving and attacking with their units either until the opponent is defeated or a predetermined objective is achieved.

The nature of the different units allows for an extreme degree in customization, including the size/weight, weapons, speed, and more. While there is a large variety of pre-made units available to choose from, building your own is also encouraged and is considered an integral part of the game as a whole. To this end, multiple rulebooks have been published to aid in this process and several applications have been created to more easily facilitate and streamline unit construction. One of these apps, an open source project known as MegaMek, is what has served as the inspiration for this project.

## Purpose -

While MegaMek allows for the customization of nearly every unit type in the game, there are a couple notable exceptions. It is one of these units, known as ‘mobile structures’, that this project will focus on. Since there is currently no computerized construction assistant for mobile structures, it has presented the perfect niche for development.

Mobile structures are rather unique among the units of Battletech, in that they are composed of multiple separate map spaces, each space containing multiple levels of height. Whereas nearly all other units only occupy a single space and don’t have extra height levels. It is this very nature that makes them much more challenging to develop for, as each individual tile has even greater complexity than any other unit in the game. And multiple tiles must be tracked and combined in a single area to properly build the unit.

## Plan -

The initial plan is to get a single tile working first. That way, the editor will behave much like the existing MegaMek app. At this stage, a tile selection won’t even be necessary. It will simply be a case of ensuring the correct equipment and construction rules are followed.Once this is achieved, the next step will be to create a tile map for selecting different individual tiles, and for each tile to be built as separate entities. The final, and potentially most difficult phase, will be to integrate all the tiles together and follow the construction rules for the unit while doing so. This will require tracking each individual tile and its statistics globally, as the total sum of various factors across all tiles influences the requirements and capabilities of other aspects of the unit.

The task ahead will certainly be difficult. The chosen language of Java has been chosen as it is the same language in use by MegaMek, and I hope to be able to offer my app to their development team in the future if they are interested in integration with their project.

**Development stage 1 - (Complete)**\
	Design early draft of UI of core structure page. - [UI draft available here](https://drive.google.com/file/d/1-miPgra_FQU7BXWDseNzAxUhhfAG6j2m/view?usp=sharing)\
	Create a console app that allows manual entry of core fields and makes stat calculations based on those fields.)\
 	Refactor code to use a class model.

**Stage 2 - (in progress)**\
	Develop a UI that incorporates the fields and outputs the information (complete)\
	Create hex-grid display in non-functional/interactive form(in progress)

**Stage 3 - (ns)**\
	Integrate functionality of hex-grid display, will allow for the selected hexes to be highlighted\
	Create early draft of equipment page UI

**Stage 4 - (ns)**\
	Create ability to save and load structures

**Stage 5 - (ns)**\
	Create method of attaching equipment to the structure\
	Create test equipment items to make testing and troubleshooting easier

**Stage 6 - (ns)**\
	Create method of selecting and storing information about individual hexes
	Finish work on equipment and other needed modules\
	Prepare for initial build release

 **Stage 7 - (ns)**\
 	Prepare for initial build release
	Continue adding additional equipment items

 ***

The application has reached a functional state! Basic data verification has been added. It's nothing special, but it serves the purpose while I work on building and migrating to a UI. For now, the only way to run the app is to load it into an IDE for Java and use the IDE console. I have been using IntelliJ Idea Community Edition for development, and as such is my recommendation for running the application.

In order to run the application, you may need to ensure you version of Java is on par with what I have been using for development. This version is Java 17 - 17.0.11.9 provided by Adoptium. A guide for installing this version of Java can be found below this paragraph, created by the open source developers at the MegaMek project. This is the guide I followed to install my version of Java.

https://github.com/MegaMek/megamek/wiki/Updating-to-Adoptium

It is difficult to explain exactly what each different input choice means to those uninitiated with the Battletech tabletop game. Some fields are somewhat self explanitory, but others only make sense within the universe. In general, I have it set up so that the higher numbers mean bigger/stronger structures, while lower numbers will typically cooralate with smaller and lighter weight ones.
