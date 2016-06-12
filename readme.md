The challenge
============

Optimal Ghost

In the game of Ghost, two players take turns building up an English word from left to right. Each player adds one letter per turn. The goal is to not complete the spelling of a word: if you add a letter that completes a word (of 4+ letters), or if you add a letter that produces a string that cannot be extended into a word, you lose. (Bluffing plays and "challenges" may be ignored for the purpose of this puzzle.)
Write a program that allows a user to play Ghost against the computer.
The computer should play optimally given the attached dictionary. Allow the human to play first. If the computer thinks it will win, it should play randomly among all its winning moves; if the computer thinks it will lose, it should play so as to extend the game as long as possible (choosing randomly among choices that force the maximal game length).

Ghost solver
============

A Spring MVC application to solve the word game [Ghost](http://en.wikipedia.org/wiki/Ghost_(game)). 

Combinatorial game theory
-------------------------

Combinatorial game theory describes two player, turn-based, deterministic games where a player loses if he can't make a move. Chess, Go and Ghost are all combinatorial games. In particular, Ghost is an _impartial game_, meaning the same moves are avaliable to both players. Chess is _not_ impartial because Black cannot move the white pieces. [Nim](http://en.wikipedia.org/wiki/Nim) is a famous impartial game played with matchsticks. The Sprague-Grundy theorem (1) demonstrates that every impartial game is equivalent to a Nim-heap of some height.

This script determines the height of the Nim-heap equivalent to Ghost.

1. Conway, Berlekamp, Guy (1982). _Winning Ways for your Mathematical Plays_

Inspiration
---------

Randall Munroe (_xkcd_ artist) [solved Ghost](http://blog.xkcd.com/2007/12/31/ghost/) too.

