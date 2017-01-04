# MonopolyMultiplayer
Java-based monopoly with multiplayer capabilities

The is a monopoly game that allows multiple players (2-5) to play together simultaneously. The game will allow each player to purchase the properties that they want when they move to the specific location of the game board after they had rolled the dice. The game will end after 4 rounds of play and the winner will be the one who has the highest amount of asset on hand. 

Upon successful connection between the server and clients, each player will start the game with $5000 in hand while the server (Banker) will start the game with $10000. Every player in the game will roll the dice to determine how many steps they can move in the game board. 

If they manage to arrive at a specific location that is available for sale, they can choose to either purchase the property or ignore the request. Also, each player will have to pay a certain amount of rental fees to the owner of the property if they happen to be on their property. 

The game will conclude after 4 rounds of play and the total amount of asset for each player will be tabulated and displayed accordingly.

1)	A deck of 8 community chest cards will be shuffled before the start of each game.
  A.	It's your birthday, everyone gives you $100
  B.	Pay School Fee $500
  C.	Life Insurance Matures, collect $120
  D.	Donate $50 to charity
  E.	Advance to GO
  F.	Receive for services $25
  G.	Go back 2 spaces
  H.	You are very generous today, give each other $20
2)	Players to collect $200 when they pass "GO".
3)	Collect 10% rental fees of the property value if the player is on your property.
4)	Allow the players to engage in a chat session with each other during the game.
5)	Allow the player to restart the game after a game is completed.
6)	Allow the player to quit the game and show results.

The server program (Banker) will be hosting the game which allows the user to enter the numbers of players connecting to the game. The client program will be the one prompting the user for their name and IP address of the server. 

Note: This is not a multi-threaded program and is NOT thread safe!
