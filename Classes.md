# Class design #

This is really important, so spend a lot of time perfecting this



## public class Pokemon ##

### Description ###

This class will store:
  * name
  * level (and experience)
  * attack
  * defense
  * special
  * speed
  * image
  * moves
  * element 1
  * element 2
  * status

Methods that return most of the data. Also a method to gain experience (which controls level-up stat increases), and a method to temporarily increase/reduce HP, and other stuff (used by Moves and Battle). Also, a method for status changing.

### Status ###

started, not sure how far along it is.



## public class Move ##

### Description ###

This class will store:
  * power
  * animation
  * element
  * name
  * accuracy

Methods that access these data values.

### Status ###

Non started yet.



## public class Character ##

### Description ###

This class will hold a character's pokemon as well as the images for the character. The character is special because the world moves around him, he is always in the center of the screen, therefore most of the movement stuff is handled in the window class. This class will be serialized and deserialized as the game saves and loads. The only other thing that may need to be serialized at load time is the location. Everything else will be loaded as normal, likely not serialized. Character also holds information about the PC. Most likely, it'll be an ArrayList of Pokemon sorted alphabetically. Character also holds information about the money available to the character.

### Status ###

Started, and basic movement functionality is present. Serialization still needs to be implemented, and all game-related functionality is yet to be coded.

## public class Trainer extends NPC ##

## public class NPC ##

## public class GymLeader extends Trainer ##

## Description ##

Like a trainer, but they talk more and give you a TM and a Badge

## Status ##

not started yet

## public enum Element ##

### Description ###

Defines the different elements of pokemon. Has methods like `double getMultiplierAgainst(Element other)` to get the attack multiplier

### Status ###

completed, unless we decide to update to gold/silver/more recent

## public class Battle ##

### Description ###

The Battle class does all the calculations based on the amazing guide at gamefaqs. Stores two Pokemon

### Status ###

Barely started. Waiting for Move and Pokemon to finish