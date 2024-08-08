# Sabin Rains Hexagonal Game

A hexagonal-grid game project with Java Swing GUI and Json persistence.

Guide: https://www.di.fc.ul.pt/~jpn/gv/sabinrains.htm 

## Game Rules

Sabin Rains is played on a 5x5 hexagonal board.

Hex Ring: The set of six cells surrounding any given cell on the board. A hex ring may contain fewer 
than six cells, especially on the edges of the board.

Drop:

- Red and blue sides take turns, with red going first. Each round, each player drops a stone on an 
empty space.

- Stones cannot be dropped on a cell without adjacent empty cells.

- After placing a stone, the newly dropped stone may appear on up to 6 hex rings, centered on the 
6 adjacent cells.
  
- On each hex ring, it will flip all enemy stones that are sandwiched between it and another 
friendly stone (a flip is triggered when all cells between two friendly stones are occupied by 
enemy stones, not empty cells).
- If there are five enemy stones on a hex ring, the newly placed stone will flip all five.
- The flipping rule applies independently to each of the 6 hex rings affected by the new stone, 
meaning that flipping in one ring does not trigger a chain reaction in other rings.

Objective: The game ends when there are no more valid moves left on the board. The player with the 
most pieces on the board wins.

![image](https://github.com/user-attachments/assets/b9a995aa-0029-494d-a6ab-f20ec70ca5a6)

Example:

1. In the image at the top, assume it is red's turn and red places a stone in cell [1]. Cell [1] is part of 4 hex 
rings, corresponding to 4 adjacent cells. Among them, the flipping rule is triggered only in the hex ring 
centered on the red stone to the left of [1], and the blue stone is flipped to red.
2. It's blue's turn next, and there are only three cells left on the board where stones can be placed, 
marked with green dots. Blue can place a stones on any of these without triggering a flipping rule.
3. When it's red's turn again, red can place in the remaining two green-dotted cells. In this situation, red 
can always choose a placement that flips five blue stones in a hex ring and finally wins.

### GUI
****
<ins>Start Menu</ins>

![image](https://github.com/user-attachments/assets/ffab112e-fdf3-4faa-9187-29fb39b8f58b)


<ins>Game Menu</ins>

![image](https://github.com/user-attachments/assets/baf3ad40-c688-4ae2-a8f4-c151fedbcbf9)

![image](https://github.com/user-attachments/assets/7f2e4bdd-dbeb-4a03-ae07-13a59852f2ce)

![image](https://github.com/user-attachments/assets/f89f4363-b8ba-41cf-a87d-8bcf1189d5f3)

<ins>Result Menu</ins>

![image](https://github.com/user-attachments/assets/3c79a0ba-e202-440b-bcfa-d831f3ac0a85)

### Robot Design
****

Robot 1 uses a greedy algorithm approach as it makes a short-sighted, optimal move each step that loops
through each available move and chooses the one that results in the most flips or the most (blue) stones
for the robot. This often covers one of the author’s considerations, which is to win the battle for individual
hex rings by placing the six piece of the hex ring.

Robot 2 implements a simplified version of the considerations given by the author. The consideration
looked at is “to attempt to play the last piece”. This means that the robot should try to not play a move
that puts the opponent in a position to complete a hex ring. Thus, this robot is implemented by storing all
hex rings on the board and not allowing the move if it puts the hex ring in a state to be completed (unless
forced to).

Testing both methods showed generally random results, robot 1 was not able to really take control of
areas as it often one by one places in the top area of the board. Robot 2, dependent on the opponents
actions, can perform similarly to Robot 1, but attempts to deny opponents the ability to complete hex
rings. This appears to result in higher successes in Robot 2.
