# Blackjack Game

A console-based Blackjack game with the objective of getting 21 points.

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
- **JUnit 5** - Unit and integration testing
- **Maven** - Build and dependency management

### Prerequisites
- Java 21 or higher
- Maven 3.6+

## Testing
The project includes **15 comprehensive tests** covering:
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
