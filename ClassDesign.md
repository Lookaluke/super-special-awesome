# Introduction #

OMG HALP SO MUCH DAMN STUFF


# Problems #

Right now, I'm kinda stuck. There's a lot of thinks to implement so please help me design the classes.

1) how should i take the next move. the move could be using an item or swapping pokemon. Swapping pokemon takes precedence over using an item which is over making a move, and I'm not sure about running.

2) where do we store information about how much experience is given after you kill a pokemon? do enemy pokemon "gain experience" when they kill your pokemon or do i have to make  it so that it actually recognizes the difference between your pokemon and someone else's pokemon?

3) how are we going to set up move-learning? is there going to be a method in pokemon that sees if the current move list is full and if user input is required and it displays stuff.

4) how do we define how moves reduce stats? Do we have to make an `enum` called `Stat` and define `ATTACK, DEFENSE, SPEED and SPECIAL`?

5) what methods does battle system have? how much of the functionality will be written in lower classes like `Pokemon` and `Move` and just called by `Battle`? How much will be controlled by `Battle`?

6) How do i differentiate between wild pokemon battles and trainer battles? Does that go into the constructor? if it does, how do i save it within the class? Should i just make two different Battle classes? `TrainerBattle` and `PokemonBattle`? would that make things easier?

alright...now that that's over, i suppose i can make room for suggesting design decisions and what i already have.

```

public class Battle{

  private Pokemon yours;
  private Pokemon[] theirs;
}

```