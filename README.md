# Blackjack Game

A console-based Blackjack game with the objective of getting 21 points.

## Installation & Running

```bash
# Clone and build
git clone https://github.com/dennismuehlegger/blackjack-game.git
cd blackjack-game
mvn clean install
```

## Features

### Core Gameplay
- **Multi-deck support**: Configurable number of decks per round
- **Multi-player mode**: Support for 2-7 players (no dealer)
- **Initial deal**: Each player receives two cards to start
- **Player actions**:
  - `Hit`: Draw another card
  - `Stand`: Keep current hand until round is over
- **Ace handling**: Aces count as 11 or 1 to prevent busting
- **Automatic win detection**: Players reaching exactly 21 win immediately

### Win Conditions
- Player closest to 21 without busting wins
- Ties are handled (multiple players can reach 21)
- All players busting results in no winner

## Technologies
- **Java 21** - Core language
- **JUnit 4** - Unit and integration testing
- **Maven** - Build and dependency management

### Prerequisites
- Java 21 or higher
- Maven 3.6+

## Testing
The project includes **16 comprehensive tests** covering:
- Player limit validation
- Deck creation and shuffling
- Ace calculation (11 vs 1)
- Busting scenarios
- Standing behavior
- Win condition detection
- etc...

## How to Play

1. Specify number of decks (1-8)
2. Enter number of players (2-7)
3. Enter player names
4. Each player's turn:
   - View current hand and score
   - Choose to `hit` (draw) or `stand` (keep)
   - Bust if score exceeds 21
5. Winner(s) announced at end of round

## Example Round

```
--- First deal ---
player 1 gets: 4♠
player 2 gets: K♣
player 3 gets: J♦

--- Second deal ---
player 1 gets: 2♥
player 2 gets: 2♦
player 3 gets: 8♠

==============================

=== Current hand ===
player 1: 6
player 2: 12
player 3: 18

========================================
*** ROUND 1 ***
========================================

=== Current hand ===
player 1: 6
player 2: 12
player 3: 18

--- player 1 turn ---
Current hand: 6
player 1, do you want to hit? (yes/no): yes

player 1 draws: 7♥
New hand: 13

--- player 2 turn ---
Current hand: 12
player 2, do you want to hit? (yes/no): yes

player 2 draws: 3♠
New hand: 15

--- player 3 turn ---
Current hand: 18
player 3, do you want to hit? (yes/no): no
player 3 stands (hand stays 18)

========================================
*** ROUND 2 ***
========================================

=== Current hand ===
player 1: 13
player 2: 15
player 3: 18 (Standing)

--- player 1 turn ---
Current hand: 13
player 1, do you want to hit? (yes/no): yes

player 1 draws: 10♦
New hand: 23
player 1 is over 21 and busted!

--- player 2 turn ---
Current hand: 15
player 2, do you want to hit? (yes/no): yes

player 2 draws: 4♥
New hand: 19

========================================
*** ROUND 3 ***
========================================

=== Current hand ===
player 1: 23 (Busted)
player 2: 19
player 3: 18 (Standing)

--- player 2 turn ---
Current hand: 19
player 2, do you want to hit? (yes/no): no
player 2 stands (hand stays 19)

========================================
*** FINAL HANDS ***
========================================
player 1: 4♠, 2♥, 7♥, 10♦ = 23 (Busted)
player 2: K♣, 2♦, 3♠, 4♥ = 19 (Standing)
player 3: J♦, 8♠ = 18 (Standing)
========================================

========================================
*** player 2 wins with 19 points! ***
========================================
```
```
