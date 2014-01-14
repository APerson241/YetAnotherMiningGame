
Version (Release Date)
Description
Alpha 0.1
“The Original”
+ A game
+ A friendly green neon square that we’ll call the robot
Alpha 0.2
“Class Explosion”
+ A little version number on the top
+ Elements
+ Random little holes in the ground
+ Element collection

- Digging up
Alpha 0.3
“A Bunch Of Stuffs”
+ Status bar with robot position and current program status
+ A fuel tank which does nothing
+ Infrastructure for upgrades, which is currently unused
+ A rectangle onscreen called the Shop, which just displays a random empty window
+ Fly up messages

* Nerfed element creation (in the world generator)
* Restructured the HUD
Alpha 0.4
“Le Intro Screen”
+ A completely boss intro screen, with instructions and a friendly greeting

* The shop now shows your current fuel and fuel tank status
* Behind-the-scenes class structure got changed
Alpha 0.5
“Mr. Shop”
+ Upgrades for the tank

* Complete overhaul of the shop - now with upgrades and refueling!
* You can’t move when you don’t have fuel...
* Fixed a bug in the main game screen that refused to display the correct version #

- Annoying text on the progress bar showing your fuel in the main screen
Alpha 0.6
“So I heard you like features...”
+ A toolbar in the main game screen, with 2 options: preferences and quit.
+ Options (so far only to turn on/off gravity)
+ A new key shortcut in the welcome screen
+ Epic fuel tank reserves, which cost $100 and fill up your entire fuel tank
+ Rather buggy scrolling
+ A skeleton for gravity

* An exponential formula for calculating how much money needed for fuel tank upgrade
* Starting fuel capacity is now 40
* Fixed when fuel capacity is over 100, the progress bars showing fuel blank out
* Fixed incorrect plural forms (i.e. reserve vs. reserves)
* Fixed the store window being squished
Alpha 0.7
“Dat scrolling...”
+ Added scroll position, gravity status, and autoscroll status to status bar
+ Autoscrolling - if you’re too close to the edge, the screen scrolls.
+ Extremely buggy gravity

* Changed height of game to 5000 pixels
* Moved all the flyups to the upper left corner of the screen
* Fixed scrolling
* Fixed incorrect plural forms (they were wrong last time)
* Fixed checkboxes not being selected in the options screen by default

- Annoying debug message to System.out saying the fuel percentage
Alpha 0.8
“And den boom!”
April 17, 2012
+ Added an “About” screen
+ Dynamite, which blows up rocks and dirt, but not elements
+ Rocks, which you can’t go through
+ Cheat code: Type in “daniel” and get infinite fuel!

* If the tank is full, you shouldn’t be able to buy fuel
* If you don’t have enough money, you shouldn’t be able to buy fuel
* If you don’t have any reserves, the shop tells you so
* MANY layout fixes in the shop
* Gravity now can scroll the screen down, too.
* Fixed welcome message not fitting in the window (made window bigger)
* Fixed fuel purchasing
* Fixed gravity

- The console, because we don’t use it
Alpha 0.8.5
“LOL YOU ARE DEAD”
June 18, 2012
+ When the robot runs out of fuel, it dies
+ When the robot dies, it turns red and a funny message appears on the screen
+ When the funny message appears on the screen, you can press ENTER to restart.
+ An actual about screen

* The welcome view now uses CardLayout
* You can sell fuel reserves in the shop
* Fixed the fuel combo box is disabled (in the shop) for no good reason
Alpha 0.9
“Yet. Another.  Mining.  Animation.”
June 21, 2012
+ Added some extremely epic animation in the welcome view
+ You can click to skip the animation
+ You can now save and load games, but it’s very buggy

* You can sell dynamite in the shop
* Code structure in the about screen
* Fixed The exit button in the welcome screen FINALLY works, after 6 versions!
Alpha 1.0
“AWW YEAH”
October 19, 2012
+ A way more awesome welcome view animation
+ When you open the shop, the game is locked
+ When the game is locked, you can’t move
+ When you close the shop, the game is unlocked
+ A tiny margin on the left of the flyups (aesthetic)
+ A bunch of debug options on the preferences screen
+ The “status” part of the status bar does something
+ You can now save and load games
+ You can now start a new game from the menu bar
+ The flyups change color dynamically (so you can read them)
+ There are rocks under the shop, so you can’t unintentionally dig out the shop
+ Another cheat code: “kaboom” will give you INFINITE dynamite.
+ Another cheat code: “lulz” will give you INFINITE fuel and INFINITE dynamite.

* Beautified the preferences screen
* The “You Died” message is more legible
* Fixed The shop fuel buttons are disabled for no good reason
* Fixed The Magical Creeping Fly-ups after 7 versions!
Alpha 1.1
“Eeew bugs”
November 11, 2012
+ You can now turn cheat codes off in the preferences
+ The gravity delay appears in the preferences screen
+ New upgrades system
+ You can upgrade the dynamite (doesn’t work yet)
+ The GamePanel uses alpha constants for epic-er graphics (ooooooh aaaaaah)

* The constants 25, 200, and 5000 have been replaced by UNIT, GROUND_LEVEL, and BOTTOM.
* Beautified the preferences screen even more
* Moved the version number inside the draw panel in the welcome view
* Fixed The robot can no longer exit on the bottom
* Fixed The robot can no longer exit on the right
Alpha 1.2
“Whimsicalness?”
November 23, 2012
+ The flyups fade away
+ A thread that calls gameView.update() every so often
+ The user can set whether or not the thread runs and the delay between each call in Preferences
+ The user can set whether mouse coordinates are printed to System.out when the mouse is clicked
+ We have FPS because of the update thread
+ Added a friendly message “Use the arrow keys to move the robot!” before the user takes a step
+ User can change fundamental unit size (with a warning)
+ You can purchase a portal (which does nothing)
+ New cheat code: “profit!” will give you $100,000

* The limit for fuel tank upgrades is tier 20
* Beautified the preferences screen more
* Beautified the shop
* Updated “About” screen
* Fixed Some bugginess when scrolling below ground level
* Fixed Gravity can move you offscreen on the bottom
* Fixed One element isn’t visible when superimposed on a hole
