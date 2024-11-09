Sure! Here's a **README.md** for your **HiFive Multiplayer Card Game** project. This README is designed to highlight the key aspects of the game, its features, and how you approached its development. It's concise, professional, and suitable for a resume project.

---

# **HiFive Multiplayer Card Game**

**HiFive** is a multiplayer card game developed using **Java**, utilizing the **JCardGame** and **JGameGrid** libraries. The game features both human and AI players, with distinct strategies for card discarding and score calculation. The game architecture follows the **SOLID principles**, **GoF design patterns**, and **UML modeling** to ensure flexibility, scalability, and maintainability.

---

## **Features**

- **Multiplayer Support**: Designed for 4 players, with the ability to play as both human and AI players.
- **Dynamic Player Strategies**: Players can have different strategies for selecting and discarding cards (e.g., **CleverPlayer**, **BasicPlayer**, **RandomPlayer**).
- **Game Flow Management**: The game handles the flow of rounds, score calculation, and card dealing with ease.
- **Modular Design**: The game is built using **SOLID** principles, ensuring extensibility and maintainability.
- **Design Patterns**: Uses **Factory**, **Strategy**, **Creator**, and **Information Expert** patterns for player creation and gameplay management.

---

## **Technologies Used**

- **Java** – Core programming language.
- **JCardGame** – Library for creating card games.
- **JGameGrid** – Used for creating a grid-based UI for the game.
- **UML** – For modeling the game architecture.
- **SOLID Principles** – To ensure the game is maintainable and extensible.
- **GoF Design Patterns** – Including **Strategy**, **Factory**, **Creator**, and **Information Expert**.

---

## **Installation & Setup**

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/HiFive-Card-Game.git
   ```
   
2. **Install dependencies**:
   Ensure that you have **Java** installed on your machine. You can download it from [Oracle’s Java page](https://www.oracle.com/java/technologies/javase-downloads.html).

3. **Build and run the game**:
   - Navigate to the project directory.
   - Compile and run the project using your preferred IDE (e.g., IntelliJ IDEA or Eclipse), or use the following command in the terminal:
     ```bash
     javac *.java
     java Driver
     ```

---

## **Gameplay Overview**

- **Game Rules**:  
  - The game is played with **4 players**: Human or AI.
  - Players are dealt **2 cards** each and play through **4 rounds**.
  - **Score Calculation** is based on card values, and specific scoring rules are in place for achieving "HiFive."
  
- **Player Types**:
  - **Human Player**: Makes decisions by selecting cards from their hand.
  - **AI Players**: Implement different strategies for selecting cards.
    - **CleverPlayer**: Chooses cards based on calculated potential scores.
    - **BasicPlayer**: Selects cards with a simpler strategy, focusing on card rank.
    - **RandomPlayer**: Chooses cards randomly.

---

## **Design and Architecture**

### **Design Patterns Used**
- **Strategy Pattern**: Allows dynamic change of player behavior (e.g., switching between **CleverPlayer**, **BasicPlayer**, and **RandomPlayer**).
- **Factory Pattern**: Used for creating player instances based on configuration, facilitating extensibility for new player types.
- **Creator Pattern**: Manages the creation of players, delegating responsibilities to the appropriate classes.
- **Information Expert Pattern**: Assigns responsibilities to classes that have the necessary information to perform actions, such as score calculation.

### **UML Diagrams**
- **Class Diagrams**: Represent the structure of the game and interactions between components such as **HiFive**, **Player**, and **Rank**.
- **System Sequence Diagrams**: Illustrate the flow of actions during each round of gameplay, including interactions between the **HiFive** class and the players.

---

## **Future Development**

The game is designed for easy extensibility. Future improvements could include:
- **New Player Types**: Implementing advanced AI players with machine learning or decision-making capabilities.
- **Networking Support**: Adding multiplayer support over a network for remote play.
- **Advanced Game Modes**: Introducing new game modes with different rules and scoring systems.
- **Mobile Version**: Porting the game to a mobile platform like Android or iOS.

---

## **Contributing**

Feel free to fork the repository and submit pull requests. Contributions are welcome!


### **Contact**

For any inquiries or feedback, please contact me at:  
- **Email**: mail@sushant.io 
- **GitHub**: [github.com/sushantg13](https://github.com/sushantg13)

---

