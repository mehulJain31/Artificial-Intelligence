/* Mehul Jain 1001229017
   CSE 4308-002 Lab 1
 */

import java.util.*;
import java.io.*;


 public class java_1
 {
	private static String parsing; 

 	public static void childAddition(node origin,ArrayList<file> routes) //adding the child nodes to the parent nodes
 	{	
 		ArrayList<node> children=new ArrayList<node>();

 		for(file fileObject:routes)
 		{	
 			if(fileObject.getcity1().equals(origin.getstate()))
 			{
 				node child=new node(fileObject.getcity2(),origin.getdepth()+1,origin.gettotalcost()+fileObject.getdistance(),origin);
 				
 				children.add(child);
 			}
 			else if(fileObject.getcity2().equals(origin.getstate()))
 			{
 				node child=new node(fileObject.getcity1(),origin.getdepth()+1,origin.gettotalcost()+fileObject.getdistance(),origin);
 				
 				children.add(child);
 			}
 		}
 		origin.setchildren(children);
	}


	public static void printPath(node backtrace,ArrayList<file> routes) //printing the path
    {	
    	
    	ArrayList<String>parents= new ArrayList<String>();

    	file calculatedistance=new file();
    	
    	System.out.println("Distance: "+backtrace.gettotalcost());
    	
    	System.out.println("Route:");
		
		while(backtrace.getParent()!=null) // print the backtrace of the path
		{	
			
			System.out.println(backtrace.getstate()+" to "+ backtrace.getParent().getstate()+":"+ getBetweendistance(backtrace.getstate(),backtrace.getParent().getstate(),routes));
			
			parents.add(backtrace.getstate());
			
			parents.add(backtrace.getParent().getstate());

			backtrace=backtrace.getParent();
		}   	
    }


	public static int getBetweendistance(String source,String destination,ArrayList<file>routes) //getting the distance between the two cities on the route
    {	

    	int distance=0;
    	
    	for(int i=0;i<routes.size();i++)
    	{
    		if(source.equals(routes.get(i).city1)) //if the source is saved as the first city in the file
    		{
    			if(destination.equals(routes.get(i).city2))	
    			{
    				distance=routes.get(i).distance;
    			}
    		}

    		if(source.equals(routes.get(i).city2))//if the source is saved as the second city in the file
    		{
    			if(destination.equals(routes.get(i).city1))	
    			{
    				distance=routes.get(i).distance;
    			}
    		}
    		
    	}
    	return distance;
	}
 
 	public static void main(String[] args) throws IOException
	{	

		String file_name= args[0];

		String source=args[1];

		String destination=args[2];

		String route[]; //array for splitting

		ArrayList<String> closedset=new ArrayList<String>();

		String first,second;

		int distance;

		ArrayList<file> routes=new ArrayList<file>();

		node origin=new node(source,0,0,null);

		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file_name));
		
			while((parsing = br.readLine()) != null) 
			{
                if(parsing.equals("END OF INPUT"))
                    break;

                route= parsing.split(" ");
                
                file newData= new file();

                newData.city1=route[0]; //saving the txt file to a class with the routes as objects.

                newData.city2=route[1];

                newData.distance=Integer.parseInt(route[2]);

                routes.add(newData); // save the routes as an object of file in java

			}
            br.close();
		}

		catch(FileNotFoundException ex) 
           	{
            	System.out.println("File Not Found");                
        	}
        
        catch(IOException ex) 
        	{
            	System.out.println("File Unreadable");                  
        	}


       PriorityQueue<node> fringe=new PriorityQueue<node>(500, new Comparator<node>() //making a priority queue to find the optimal route
       {
            public int compare(node node1, node node2)
            {
                
                if (node1.gettotalcost()==node2.gettotalcost()) 
                	{
                		return 0;
                	}
                if (node1.gettotalcost()>node2.gettotalcost())
	                {
	                	return 1;
	                }

                return -1;
            }
        });			
            

	   fringe.add(origin); //adding the node to the fringe

	   while(!fringe.isEmpty())
	   {
			node temp=fringe.poll(); //pull the first node from the fringe

	   		if(temp.getstate().equals(destination))
	   		{
	   			printPath(temp,routes);// if the destination=source
	   			
	   			break;
	   		}

			if(!closedset.contains(temp.getstate()))  
			
			{	
				closedset.add(temp.getstate());  

				childAddition(temp,routes);
			
				for(int i=0;i<temp.getchildren().size();i++)
					{
						node child= temp.getchildren().get(i);
			
						fringe.add(child); // add the children to the fringe
					}
		    }
	   
	   		if(fringe.isEmpty())
	   		{
	   			System.out.println("Distance= infinity");// if a route does not exist
	   	
	   			System.out.println("Route:"+'\n'+" none");
	   		}
		}

	}
}

 