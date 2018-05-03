import java.io.*;
import java.util.*;


public class maxconnect4
{
      public static void main(String[] args) 
      {
        // check for the correct number of arguments
        if( args.length != 4 ) 
        {
          System.out.println("Four command-line arguments are needed:\n"+ "Usage: java [program name] interactive [input_file] [computer-next / human-next] [depth]\n"+ " or:  java [program name] one-move [input_file] [output_file] [depth]\n");

          exit_function( 0 );
         }
        
        // parse the input arguments
        String game_mode = args[0].toString();        // the game mode
        
        String input = args[1].toString();          // the input game file
        
        int depthLevel = Integer.parseInt( args[3] );     // the depth level of the ai search
        
        // create and initialize the game board
        GameBoard currentGame = new GameBoard( input );

        Scanner sc=new Scanner(System.in);
        
        // create the Ai Player
        AiPlayer calculon = new AiPlayer();
        
        //  variables to keep up with the game
        int playColumn = 99;        //  the players choice of column to play
        
        boolean playMade = false;     //  set to true once a play has been made

        if( game_mode.equalsIgnoreCase( "interactive" ) )  // interactive mode with alpha beta search
        {
          
          String next_player= args[2].toString(); // human next or computer next

          int human_turn, computer_turn;

          if(next_player.equalsIgnoreCase("human-next"))
          {
            human_turn= currentGame.getCurrentTurn();

            computer_turn=(3- currentGame.getCurrentTurn());

          }

          else // if it is computer-next
          {
            computer_turn=currentGame.getCurrentTurn();

            human_turn=3- currentGame.getCurrentTurn();

          }

          while(currentGame.getPieceCount() < 42 )// check until the board is full 
          {
            if(human_turn == currentGame.getCurrentTurn())
            {
                //user input
                System.out.print("\nMaxConnect-4 game\n");  // copied from one move mode
            
                System.out.print("game state before move:\n");
            //print the current game board
                currentGame.printGameBoard();
            // print the current scores
                System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +", Player2 = " + currentGame.getScore( 2 ) + "\n " );

                System.out.println("Enter Your Move[1-7]:");

                playColumn = sc.nextInt();
                // ****************** this chunk of code makes the computer play 
                currentGame.playPiece(playColumn-1);
                
                System.out.println("move " + currentGame.getPieceCount()+ ": Player " + currentGame.getCurrentTurn()+ ", column " + playColumn);
                
                System.out.print("game state after move:\n");
                
                currentGame.printGameBoard();
                
                currentGame.printGameBoardToFile( "human.txt" );
            
            }
            
            else
            {
               
                System.out.print("\nMaxConnect-4 game\n"); // copied from one move mode
            
                System.out.print("game state before move:\n");
            //print the current game board
                currentGame.printGameBoard();
            // print the current scores
                System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +", Player2 = " + currentGame.getScore( 2 ) + "\n " );
                
                playColumn = calculon.findBestPlay(currentGame,depthLevel);
               // ****************** this chunk of code makes the computer play 
                currentGame.playPiece(playColumn);
                
                System.out.println("move " + currentGame.getPieceCount() + ": Player " + currentGame.getCurrentTurn()+ ", column " + (int)(playColumn+1));
                
                System.out.print("Game State after Move:\n");
                
                currentGame.printGameBoardToFile( "computer.txt" );
          }


        } 

        System.out.println("Game Over!");
        
        System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) + ", Player2 = " + currentGame.getScore( 2 ) + "\n " );
  
        return;
    } 
          
        else if( !game_mode.equalsIgnoreCase( "one-move" ) ) 
        {
          System.out.println( "\n" + game_mode + " is an unrecognized game mode \n try again. \n" );
          
          return;
        }

        /////////////   one-move mode ///////////
        // get the output file name
        String output = args[2].toString();       // the output game file
        
        System.out.print("\nMaxConnect-4 game\n");
        
        System.out.print("game state before move:\n");
        
        //print the current game board
        currentGame.printGameBoard();
        
        // print the current scores
        System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +", Player2 = " + currentGame.getScore( 2 ) + "\n " );
        
        // ****************** this chunk of code makes the computer play
        if( currentGame.getPieceCount() < 42 ) 
        {
            int current_player = currentGame.getCurrentTurn();
      
            // AI play - random play
            playColumn = calculon.findBestPlay( currentGame,depthLevel );
      
            // play the piece
            currentGame.playPiece( playColumn );
              
            // display the current game board
            System.out.println("move " + currentGame.getPieceCount() 
                               + ": Player " + current_player
                               + ", column " + playColumn);
            System.out.print("game state after move:\n");
            
            currentGame.printGameBoard();
        
            // print the current scores
            System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +", Player2 = " + currentGame.getScore( 2 ) + "\n " );
            
            currentGame.printGameBoardToFile( output );
        } 
        else 
        {
            System.out.println("\nI can't play.\nThe Board is Full\n\nGame Over");
        }
      
        //************************** end computer play
          
        
        return;
        
    } // end of main()
      
      private static void exit_function( int value )
      {
          System.out.println("exiting from MaxConnectFour.java!\n\n");
          
          System.exit( value );
      }
} // end of class connectFour






