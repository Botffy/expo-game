# Kitchen Expo Game

A small proof-of-concept game about being the **expeditor** in a busy restaurant.

The player coordinates the flow of food from the kitchen to the dining room during service, ensuring timely delivery, correctness of orders, and keeping dishes synchronised so that plates get to the table together.

In case you're wondering, all this is inspired by the TV series "The Bear".

## Core idea

The player acts as the **expeditor (expo)**: the person standing at the _pass_ who coordinates communication between the **front of house** (FoH; the waitstaff in the dining room) and the **back of house** (BoH; the kitchen staff).

When ordering food, the customers expect the courses to come separately, but synchronised, and as soon as possible.
That is, when ordering two soups, a steak and a pasta dish, they expect to have the soups together; then, once they finish, they expect the steak and the pasta to arrive at the same time.
The steak and the pasta dish, however, take different amounts of time to prepare.
This is the main tension here: like an expo in real life, the player must:
- know how long each dish takes to prepare
- have an idea about the state of the tables
- tell the kitchen to prepare the dishes ("fire" them) at the right time. If they call it too early, the dishes will stay "in the window" and get cold; if they call it too late, well, the dishes will be late.

There is a FoH and BoH simulation running in the background, representing the truth, the state of the restaurant.
But the player knows about the state only through information they receive from the staff through little notes and speech bubbles.
The player must use this information to keep a mental model about the state of the restaurant.
The player can then affect the staff (and by extension the world) by issuing kitchen commands (e.g. "fire two burgers, that's six burgers all day" or "can I have hands for table 2, please").

## Prototype goals

This is a prototype, a very early proof-of-concept intended to see if the core idea is fun.

The prototype focuses on:
- incoming orders and waiter notes
- the UX of issuing kitchen commands
- mental tracking of table state
- dish timing and synchronisation

The prototype will not have:
- full restaurant simulation: while there will be an underlying simulation of FoH and BoH, these can be basic, with simple rules and behaviours.
- progression or end conditions, long-term game loop.
- heavy graphics

While the prototype will not have graphics as such, it must not be ugly, it must not look like a spreadsheet or a Compose tutorial.

Sound would be pretty important to the game: the shouted commands and questions, the frantic noise of the kitchen (scaled by the busyness of the kitchen) would add a lot to the atmosphere.
But it's technically tricky, and not a priority.

## Tech stack and architecture

Kotlin, Compose for Desktop, focusing on Windows for now.

- The simulation must always be kept separate from the UI.
- There will be a main game loop with ticks that trigger the updates in the FoH, BoH and window simulations.
- There will be a central event bus where the simulations post all their events. Some of these events are just logged, others are picked up by the UI to show speech, play sound, create new notes.
- The commands issued by the player can be posted to the same event bus.

## Simulations

We mentioned the simulations should be basic, but it's worth it to actually model the following:

- state of tables (number of guests, what they ordered, how long they've been waiting for the food.)
- kitchen stations and their state (what dishes are being prepared, how long until they're ready.)

It would be really fun to have:

- waiters getting angry and desperate if their order is late or wrong
- cooks asking for "all day" (total counts) and getting confused and angry if the counts the player gives them are wrong.
- cooks sometimes asking for confirmation.

Generally, we should model the mood of guests and waiters, and the "understanding" of the kitchen staff.

## Main UI elements

### Note rail

Features incoming orders and waiter notes.
These are draggable and purely informative.

### Speech bubbles

The staff (waiters and cooks) can also communicate with the player through speech bubbles.
The commands issued by the player are also shown as speech bubbles.
Even though the commands are basic and algorithmic, the speech displayed should be randomised into a more natural language with filler words and variations.

### Main work area

Features a schematic representation of the FoH.
The player is expected to model the state of the tables by dragging the orders and notes from the note rail on the tables.

### Command panel

Features UI for issuing kitchen commands.

This is a key element of the game, so the exact method of issuing commands is not set in stone yet.

An idea is to have a tree-based command builder: the player first selects a command (e.g. "fire"), then the dish to fire, then optional additions (like adding an "all day"). This would take the form of buttons, maybe, with a heavy emphasis on keyboard shortcuts.

### The "window"

The area where dishes appear once they are ready.
The player must use the "hands" command to move dishes from the window to the tables.
The dishes are marked with the type of the dish and its hotness, but not the table numbers or other "metadata". Again, the player has to keep track of this.
