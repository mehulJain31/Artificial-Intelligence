


import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * @author james spargo
 *
 */
public class CheckTrueFalse 
{	

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		
		if( args.length != 3)
		{
			//takes three arguments
			System.out.println("Usage: " + args[0] +  " [wumpus-rules-file] [additional-knowledge-file] [input_file]\n");
			exit_function(0);
		}
		
		//create some buffered IO streams
		String buffer;
		BufferedReader inputStream;
		BufferedWriter outputStream;
		
		//create the knowledge base and the statement
		LogicalExpression knowledge_base = new LogicalExpression();
		LogicalExpression statement = new LogicalExpression();

		//open the wumpus_rules.txt
		try {
			
			inputStream = new BufferedReader( new FileReader( args[0] ) );
			
			//load the wumpus rules
			System.out.println("loading the wumpus rules...");
			
			knowledge_base.setConnective("and");
			

			
			while(  ( buffer = inputStream.readLine() ) != null ) 
                        {
							if( !(buffer.startsWith("#") || (buffer.equals( "" )) )) 
                                {
									//the line is not a comment
									LogicalExpression subExpression = readExpression( buffer );// sub gets every expression and stores in a vector
									knowledge_base.setSubexpression( subExpression );
								} 
                                else 
                                {
					//the line is a comment. do nothing and read the next line
                                }
						}		
			
			//close the input file
			inputStream.close();

		} catch(Exception e) 
                {
					System.out.println("failed to open " + args[0] );
					e.printStackTrace();
					exit_function(0);
				}
			//end reading wumpus rules
		String notalpha="";
		HashMap<String, Boolean> additional_file = new HashMap<String, Boolean>();
		
		//read the additional knowledge file
		try {
			inputStream = new BufferedReader( new FileReader( args[1] ) );
			
			//load the additional knowledge
			System.out.println("loading the additional knowledge...");
			
			// the connective for knowledge_base is already set.  no need to set it again.
			// i might want the LogicalExpression.setConnective() method to check for that
			//knowledge_base.setConnective("and");
			
			while(  ( buffer = inputStream.readLine() ) != null) 
                        {
                                if( !(buffer.startsWith("#") || (buffer.equals("") ))) 
                                {
									LogicalExpression subExpression = readExpression( buffer );
									knowledge_base.setSubexpression( subExpression );

									String sentence=buffer; // saving the additional_knowledge.txt

									

									if(sentence.startsWith("("))
									{	
										sentence=sentence.replace("(not ", "");
                                  		
                                        sentence=sentence.replace(")", "");
										
										additional_file.put(sentence,false);
									}
									else
									{
										additional_file.put(sentence,true);
									}
                                } 
                                else 
                                {
				//the line is a comment. do nothing and read the next line
                                }
                        }
			
			//close the input file
			inputStream.close();

		} catch(Exception e) 
			{
			System.out.println("failed to open " + args[1] );
			e.printStackTrace();
			exit_function(0);
			}
		//end reading additional knowledge
		
		
		
		// check for a valid knowledge_base
		if( !valid_expression( knowledge_base ) ) 
		{
			System.out.println("invalid knowledge base");
			exit_function(0);
		}
		
		// print the knowledge_base
		knowledge_base.print_expression("\n");
		
		
		// read the statement file
		try {
			inputStream = new BufferedReader( new FileReader( args[2] ) );
			
			System.out.println("\n\nLoading the statement file...");
			//buffer = inputStream.readLine();
			
			// actually read the statement file
			// assuming that the statement file is only one line long
			while( ( buffer = inputStream.readLine() ) != null ) 
			{
				if( !buffer.startsWith("#") ) 
				{
                                //the line is not a comment
                                    statement = readExpression( buffer );
                                    notalpha=buffer;
                                    String a="(not ";
                                    String b=" )";
                                    notalpha=a+notalpha+b;
                                    break;
				} 
				else 
				{
					//the line is a commend. no nothing and read the next line
				}
			}
			
			//close the input file
			inputStream.close();

		} catch(Exception e) 
			{
				System.out.println("failed to open " + args[2] );
				e.printStackTrace();
				exit_function(0);
			}
		// end reading the statement file
		
		// check for a valid statement
		if( !valid_expression( statement ) ) 
		{
			System.out.println("invalid statement");
			exit_function(0);
		}
		
		//print the statement
		statement.print_expression( "" );
                
                System.out.print("\n\nNegated LE: ");
                LogicalExpression alpha_negation = readExpression( notalpha );
                alpha_negation.print_expression(""); 
                        
		//print a new line
		System.out.println("\n");
						
		//testing (implement tt entails) : tt check_all, 
		boolean one=tt_entails(knowledge_base,statement,additional_file); // calling alpha
		
		boolean two= tt_entails(knowledge_base,alpha_negation,additional_file);// calling not alpha

		if(one==true && two==false)
		{
			System.out.println("Defintitely True");
		}

		if(one==false && two==true)
		{
			System.out.println("Defintitely False");
		}

		if(one==false && two==false)
		{
			System.out.println("Possibly True Possibly False");
		}

		if(one==true && two==true)
		{
			System.out.println("Both True and False");
		}




	} //end of main


	public static boolean tt_entails(LogicalExpression kb, LogicalExpression alpha, HashMap<String,Boolean> additional_file )
	{
		List<String> symbols= new ArrayList<String>(); // getting just the symbols from the knowledge file

		alpha.unique_symbols(symbols); // get alpha symbols
		kb.unique_symbols(symbols); // knowledge base is complete

		return tt_check_all(kb,alpha,symbols,new HashMap<String,Boolean>(),new HashMap<String,Boolean>());
		
	}


	public static boolean tt_check_all(LogicalExpression kb, LogicalExpression alpha, List<String>symbols,HashMap<String,Boolean>model,HashMap<String,Boolean> additional_file)
	{
		if(symbols.isEmpty())
		{
			if(pl_true(kb,model))
				return pl_true(alpha,model);
			else
				return true;
		}
		else
		{
			String p;

			p=symbols.get(0);
			symbols.remove(p);
                        
            List<String> copy1 = new ArrayList<String>(symbols);
            List<String> copy2 = new ArrayList<String>(symbols);

            HashMap<String, Boolean> mCopy1,mCopy2;
            mCopy1= new HashMap();
            mCopy2= new HashMap();
            
            if(model!=null)
            {
                mCopy1= new HashMap(model);
                mCopy2= new HashMap(model);
            }

           if(additional_file.containsKey(p))
    			return tt_check_all(kb, alpha, symbols, extend(p, additional_file.get(p), mCopy1),additional_file);


        	return tt_check_all(kb,alpha,copy1,extend(p,true,mCopy1),additional_file)&&tt_check_all(kb,alpha,copy2,extend(p,false,mCopy2),additional_file);
        }
    }

	public static boolean pl_true(LogicalExpression alpha, HashMap<String,Boolean>model)
	{
		if(alpha.getUniqueSymbol()!=null) // just a symbol
		{
			return model.get(alpha.getUniqueSymbol());
		
		}

                else if(alpha.getConnective().equalsIgnoreCase("and")) //and
		{
			for(Object temp: alpha.getSubexpressions())
            {
                if(pl_true((LogicalExpression)temp, model)==false)
                    return false;
             }
			return true;
		}

                else if(alpha.getConnective().equalsIgnoreCase("or")) //or
		{
			for(Object temp: alpha.getSubexpressions())
            {
                if(pl_true((LogicalExpression)temp, model)==false)
                    return true;
             }
			return false;
		}

                else if(alpha.getConnective().equalsIgnoreCase("if")) //if
		{
			boolean a = pl_true((LogicalExpression) alpha.getSubexpressions().get(0),model);
			boolean b= pl_true((LogicalExpression) alpha.getSubexpressions().get(1),model);
			
			
			if(a && !b)
			{
				return false;
			}
			else
			{
				return true;
			}
		}


                else if(alpha.getConnective().equalsIgnoreCase("iff")) //iff
		{
			boolean a = pl_true((LogicalExpression) alpha.getSubexpressions().get(0),model);
			boolean b= pl_true((LogicalExpression) alpha.getSubexpressions().get(1),model);
			
			
			if(a==b)
			{
				return true;
			}
			else
			{
				return false;
			}
		}

                else if(alpha.getConnective().equalsIgnoreCase("not")) //not
		{
			boolean a = pl_true((LogicalExpression) alpha.getSubexpressions().get(0),model);
			

			return !a;
		}


                else if(alpha.getConnective().equalsIgnoreCase("xor")) //xor
		{	
			int counter=0;
			for(Object temp: alpha.getSubexpressions())
                        {
                            if(pl_true((LogicalExpression)temp, model)==false)
                               counter++;
                        }

			if(counter>1)
			{
				return false;
			}
			return true;
		}
                
                return false;
	}

	public static HashMap<String,Boolean> extend(String p, Boolean trueFalse, HashMap<String,Boolean> model)
	{
		if(model==null)
                {
                    HashMap<String,Boolean> map=new HashMap<String,Boolean>();
                    map.put(p,trueFalse);
                    return (map);
                }

		else
                {
                    HashMap<String,Boolean> map=new HashMap<String,Boolean>(model);
                    map.put(p,trueFalse);
                    return (map);
                }
			

	}

	public static LogicalExpression readExpression( String input_string ) 
        {
          LogicalExpression result = new LogicalExpression();
          
          
          
          input_string = input_string.trim();
          
          if( input_string.startsWith("(") ) 
          {
          
            String symbolString = "";
            
            
            symbolString = input_string.substring( 1 );
				  
            if( !symbolString.endsWith(")" ) ) 
            {
              // missing the closing paren - invalid expression
              System.out.println("missing ')' !!! - invalid expression! - readExpression():-" + symbolString );
              exit_function(0);
              
            }
            else 
            {
              
              symbolString = symbolString.substring( 0 , ( symbolString.length() - 1 ) );
              symbolString.trim();
              
              
              // read the connective into the result LogicalExpression object					  
              symbolString = result.setConnective( symbolString );
              
              
            }
            
            //read the subexpressions into a vector and call setSubExpressions( Vector );
            result.setSubexpressions( read_subexpressions( symbolString ) );
            
          } 
          else 
          {   	
            
            result.setUniqueSymbol( input_string );
          
            
          }
          
          return result;
        }

	/* this method reads in all of the unique symbols of a subexpression
	 * the only place it is called is by read_expression(String, long)(( the only read_expression that actually does something ));
	 * 
	 * each string is EITHER:
	 * - a unique Symbol
	 * - a subexpression
	 * - Delineated by spaces, and paren pairs
	 * 
	 * it returns a vector of logicalExpressions
	 * 
	 * 
	 */
	
	public static Vector<LogicalExpression> read_subexpressions( String input_string ) 
	{

	Vector<LogicalExpression> symbolList = new Vector<LogicalExpression>();
	LogicalExpression newExpression;// = new LogicalExpression();
	String newSymbol = new String();
	
        input_string.trim();

	while( input_string.length() > 0 ) 
	{
		
		newExpression = new LogicalExpression();
		
		if( input_string.startsWith( "(" ) ) 
		{
			
			
			int parenCounter = 1;
			int matchingIndex = 1;
			while( ( parenCounter > 0 ) && ( matchingIndex < input_string.length() ) ) 
			{
					if( input_string.charAt( matchingIndex ) == '(') 
					{
						parenCounter++;
					} 
					else if( input_string.charAt( matchingIndex ) == ')') 
					{
						parenCounter--;
					}
				matchingIndex++;
			}
			
			
			newSymbol = input_string.substring( 0, matchingIndex );
			
			
			// pass that string to readExpression,
			newExpression = readExpression( newSymbol );

			// add the LogicalExpression that it returns to the vector symbolList
			symbolList.add( newExpression );

			// trim the logicalExpression from the input_string for further processing
			input_string = input_string.substring( newSymbol.length(), input_string.length() );

		} 
		else 
		{
			//its a unique symbol ( if its not, setUniqueSymbol() will tell us )

			// I only want the first symbol, so, create a LogicalExpression object and
			// add the object to the vector
			
			if( input_string.contains( " " ) ) 
			{
				//remove the first string from the string
				newSymbol = input_string.substring( 0, input_string.indexOf( " " ) );
				input_string = input_string.substring( (newSymbol.length() + 1), input_string.length() );
				
				//testing
				//System.out.println( "read_subExpression: i just read ->" + newSymbol + "<- and i have left ->" + input_string +"<-" );
			} 
			else 
			{
				newSymbol = input_string;
				input_string = "";
			}
			
			
			newExpression.setUniqueSymbol( newSymbol );
			
	    	
			symbolList.add( newExpression );
			
			
			
		}
		
		//testing
		//System.out.println("read_subExpression() - left to parse ->" + input_string + "<-beforeTrim end of while");
		
		input_string.trim();
		
		if( input_string.startsWith( " " )) 
		{
			//remove the leading whitespace
			input_string = input_string.substring(1);
		}
		
		//testing
		//System.out.println("read_subExpression() - left to parse ->" + input_string + "<-afterTrim with string length-" + input_string.length() + "<- end of while");
	}
	return symbolList;
}


	/* this method checks to see if a logical expression is valid or not 
	 * a valid expression either:
	 * ( this is an XOR )
	 * - is a unique_symbol
	 * - has:
	 *  -- a connective
	 *  -- a vector of logical expressions
	 *  
	 * */
	public static boolean valid_expression(LogicalExpression expression)
	{
		
		// checks for an empty symbol
		// if symbol is not empty, check the symbol and
		// return the truthiness of the validity of that symbol

		if ( !(expression.getUniqueSymbol() == null) && ( expression.getConnective() == null ) ) 
		{
			// we have a unique symbol, check to see if its valid
			return valid_symbol( expression.getUniqueSymbol() );

			//testing
			//System.out.println("valid_expression method: symbol is not empty!\n");
		}

		// symbol is empty, so
		// check to make sure the connective is valid
	  
		// check for 'if / iff'
		if ( ( expression.getConnective().equalsIgnoreCase("if") )  ||
		      ( expression.getConnective().equalsIgnoreCase("iff") ) ) 
		{
			
			// the connective is either 'if' or 'iff' - so check the number of connectives
			if (expression.getSubexpressions().size() != 2) 
			{
				System.out.println("error: connective \"" + expression.getConnective() +"\" with " + expression.getSubexpressions().size() + " arguments\n" );
				return false;
				}
			}
		// end 'if / iff' check
	  
		// check for 'not'
		else   if ( expression.getConnective().equalsIgnoreCase("not") ) 
		{
			// the connective is NOT - there can be only one symbol / subexpression
			if ( expression.getSubexpressions().size() != 1)
			{
				System.out.println("error: connective \""+ expression.getConnective() + "\" with "+ expression.getSubexpressions().size() +" arguments\n" ); 
				return false;
			}
		}
		// end check for 'not'
		
		// check for 'and / or / xor'
		else if ( ( !expression.getConnective().equalsIgnoreCase("and") )  &&( !expression.getConnective().equalsIgnoreCase( "or" ) )  &&( !expression.getConnective().equalsIgnoreCase("xor" ) ) ) 
		{
			System.out.println("error: unknown connective " + expression.getConnective() + "\n" );
			return false;
		}
		// end check for 'and / or / not'
		// end connective check

	  
		// checks for validity of the logical_expression 'symbols' that go with the connective
		for( Enumeration e = expression.getSubexpressions().elements(); e.hasMoreElements(); ) 
		{
			LogicalExpression testExpression = (LogicalExpression)e.nextElement();
			
			// for each subExpression in expression,
			//check to see if the subexpression is valid
			if( !valid_expression( testExpression ) ) 
			{
				return false;
			}
		}

		//testing
		//System.out.println("The expression is valid");
		
		// if the method made it here, the expression must be valid
		return true;
	}
	



	/** this function checks to see if a unique symbol is valid */
	//////////////////// this function should be done and complete
	// originally returned a data type of long.
	// I think this needs to return true /false
	//public long valid_symbol( String symbol ) {
	public static boolean valid_symbol( String symbol ) 
	{
		if (  symbol == null || ( symbol.length() == 0 )) 
		{  
			
			//testing
			//System.out.println("String: " + symbol + " is invalid! Symbol is either Null or the length is zero!\n");
			
			return false;
		}

		for ( int counter = 0; counter < symbol.length(); counter++ ) 
		{
			if ( (symbol.charAt( counter ) != '_') &&( !Character.isLetterOrDigit( symbol.charAt( counter ) ) ) ) 
			{
				System.out.println("String: " + symbol + " is invalid! Offending character:---" + symbol.charAt( counter ) + "---\n");
				
				return false;
			}
		}
		
		// the characters of the symbol string are either a letter or a digit or an underscore,
		//return true
		return true;
	}

        private static void exit_function(int value) 
        {
            System.out.println("exiting from checkTrueFalse");
            
            System.exit(value);
        }	
}
