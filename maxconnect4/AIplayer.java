import java.util.*;

public class AiPlayer 
{
    public int maximum(int i, int j) //max comparison
    {
        if( i >= j)
        {
            return i;
        }

        else
        {
            return j;
        }
    }
    
    public int minimum(int i, int j)//min comparison
    {
        if( i <= j)
        {
            return i;
        }
        else
        {
            return j;
        }
    }

    public AiPlayer() 
    { }
    
    public int utility(GameBoard currentGame, int curr_player) // the utility calculator
    {
        int utilityvalue=0; // utility value to be returned after using the eval_utility function

        int points= (currentGame.getScore(curr_player)- currentGame.getScore(3-curr_player));
       
        utilityvalue=100*points; // for easier calculation

        utilityvalue+= 4;// get the final value with the eval function

        return utilityvalue; // return the calculated utility value
    }

      public int MaxCalculator(GameBoard currentGame, int a, int b, int curr_depth, int depth, int curr_player) // calculating the maximum value
    {
        curr_depth++;

        if(!(curr_depth==depth||currentGame.getPieceCount()<42 ))
        {    
            return utility(currentGame,curr_player); // if you reach the end of the tree
        }

        int value = -10; // value for calculating the min/max value

        for(int i = 0;i < 7; i++) // traverse through one depth level
        {
            if(currentGame.isValidPlay(i))
            {
                GameBoard newboard= new GameBoard(currentGame.getGameBoard());

                newboard.playPiece(i); // play the next move in connect4
                
                value= Math.max(value, MinCalculator(newboard,a,b,curr_depth,depth,curr_player)); // calculate the max value with the min node

                if(value>=b) // evaluate the value if it is greater than beta 
                {
                    return value;
                }

                a=maximum(a,value); // find alpha

            }
        }
        return value;
    }// end of MaxCalculator

    public int MinCalculator(GameBoard currentGame, int a, int b, int curr_depth, int depth, int curr_player) // calculating the minimum value; same as maximum just with < signs inverted
    {
        curr_depth++;

        if(!(curr_depth==depth||currentGame.getPieceCount()<42 ))
        {

            return utility(currentGame,curr_player); // if you reach the end of the tree

        }

        int value=-10; // value for calculating the min/max value

        for(int i = 0 ;i < 7; i++) // traverse through one depth level
        {
            if(currentGame.isValidPlay(i))
            {
                GameBoard newboard= new GameBoard(currentGame.getGameBoard());

                newboard.playPiece(i); // play the next move in connect4
                
                value= Math.min(value, MinCalculator(newboard,a,b,curr_depth,depth,curr_player)); // calculate the min value with the max node

                if(value<=a) // evaluate the value if it is smaller than alpha 
                {
                    return value;
                }

                b=minimum(b,value); // find beta

            }
        }

        return value;
    } // end of MinCalculator

    public int findBestPlay( GameBoard currentGame, int depth )  // add the alpha beta search and the DFS
    {
    
        int curr_player= currentGame.getCurrentTurn(); // get the current player situation

        int action=0; // user action: to be returned
        
        int utility=0; // the utility values for evaluation
        
        int maxutility=-10; // the max utility value
        
        int curr_depth=0; // the current depth

        for(int i=0;i<7;i++) // evaluating the possible moves
        {
            if(currentGame.isValidPlay(i)) // if the move is valid
            {
                GameBoard newboard= new GameBoard(currentGame.getGameBoard());
                
                newboard.playPiece(i);

                utility= MinCalculator(newboard,-10,10,curr_depth,depth,curr_player); // find the minimax value with alphabeta

                if(utility>=maxutility) // getting the maximum utility value
                {
                    maxutility=utility;
                    
                    action=i;
                }
            }

        }
        return action; // return the value calculated by minimax alpha beta search
    
    }// end of findBestPlay
}
