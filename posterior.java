import java.io.*;
import java.util.*;


class hypothesis // hypothesis class
{
	double prior;
	double cherry;
	double lime;
	
	
	public hypothesis(double prior,double cherry,double lime) 
	{
		this.prior=prior;
		this.cherry=cherry;
		this.lime=lime;
		
	}
		
}



public class task1 
{
	
	private static void print_function(int i,BufferedWriter writer,hypothesis h1,hypothesis h2,hypothesis h3,hypothesis h4,hypothesis h5, double cp, double lm, String observation) throws IOException
	{
		
		writer.write("\nObservation Sequence Q: "+ observation+"\n");
		writer.write("Length of Q: "+observation.length()+"\n");
		writer.write("After observation : "+observation.charAt(i)+"\n");
		
		writer.write("P(h1 | Q) ="+h1.prior+"\n");
		writer.write("P(h2 | Q) ="+h2.prior+"\n");
		writer.write("P(h3 | Q) ="+h3.prior+"\n");
		writer.write("P(h4 | Q) ="+h4.prior+"\n");
		writer.write("P(h5 | Q) ="+h5.prior+"\n");
	
		writer.write("Probability that the next candy we pick will be Cherry, given Q:"+cp+"\n");
		writer.write("Probability that the next candy we pick will be Lime, given Q: "+lm+"\n\n");
		
	}


	public static void main(String[] args) throws IOException
	{	
			if(args.length>2)
				System.out.println("[usage] java posterior <string>");
			
			BufferedWriter writer= new BufferedWriter(new FileWriter("results.txt"));
				
			hypothesis h1= new hypothesis(0.1,1.0,0.0); // declare all the hypothesis
			hypothesis h2= new hypothesis(0.2,0.75,0.25);
			hypothesis h3= new hypothesis(0.4,0.5,0.5);
			hypothesis h4= new hypothesis(0.2,0.25,0.75);
			hypothesis h5= new hypothesis(0.1,0.0,1.0);
			
			if(args.length!=2) // if observation is not given.
			{
				writer.write("\nObservation Sequence Q: \n");
				writer.write("Length of Q: 0\n\n");
				
				writer.write("P(h1 | Q) ="+h1.prior+"\n");
				writer.write("P(h2 | Q) ="+h2.prior+"\n");
				writer.write("P(h3 | Q) ="+h3.prior+"\n");
				writer.write("P(h4 | Q) ="+h4.prior+"\n");
				writer.write("P(h5 | Q) ="+h5.prior+"\n");
			
				writer.write("Probability that the next candy we pick will be Cherry, given Q: 0.50000\n");
				writer.write("Probability that the next candy we pick will be Lime, given Q: 0.50000\n\n");
				
					
			}
			
			String observation="";
			
			try // if no argument given 
			{
			observation= args[0] ; // get the observation
			}
			catch(Exception e)
			{
				
			}
			double nprior=0.0; // for calculation new prior values
			
			double cp=0.0; // for calculation of new cherry probability 
			double lm=0.0;// for calculation of new lime probability 
			
			for(int i=0;i<observation.length();i++) // loop for calculating new probabilities
			{
				cp = (h1.prior*h1.cherry) + (h2.prior*h2.cherry) + (h3.prior*h3.cherry) + (h4.prior*h4.cherry) + (h5.prior*h5.cherry); // new probabilities
				lm = (h1.prior*h1.lime) + (h2.prior*h2.lime) + (h3.prior*h3.lime) + (h4.prior*h4.lime) + (h5.prior*h5.lime); //new probabilities
				
				if(observation.charAt(i)=='c'||observation.charAt(i)=='C') // if observation is a cherry
				{
					nprior = ( (h1.cherry * h1.prior) / cp);
					h1.prior = nprior;
					nprior = ( (h2.cherry * h2.prior) / cp);
					h2.prior = nprior;
					nprior = ( (h3.cherry * h3.prior) / cp);
					h3.prior = nprior;
					nprior = ( (h4.cherry * h4.prior) / cp);
					h4.prior = nprior;
					nprior = ( (h5.cherry * h5.prior) / cp);
					h5.prior = nprior;
					
				
				}
				
				else if(observation.charAt(i)=='l'||observation.charAt(i)=='L') // if observation is a lime
				{
					nprior = ( (h1.lime * h1.prior) / lm);
					h1.prior = nprior;
					nprior = ( (h2.lime * h2.prior) / lm);
					h2.prior = nprior;
					nprior = ( (h3.lime * h3.prior) / lm);
					h3.prior = nprior;
					nprior = ( (h4.lime * h4.prior) / lm);
					h4.prior = nprior;
					nprior = ( (h5.lime * h5.prior) / lm);
					h5.prior = nprior;
			
					
					
					
				}
				cp = (h1.prior*h1.cherry) + (h2.prior*h2.cherry) + (h3.prior*h3.cherry) + (h4.prior*h4.cherry) + (h5.prior*h5.cherry); // write the final calculated probabilities
				lm = (h1.prior*h1.lime) + (h2.prior*h2.lime) + (h3.prior*h3.lime) + (h4.prior*h4.lime) + (h5.prior*h5.lime);
				print_function(i,writer,h1,h2,h3,h4,h5,cp,lm,observation);
			}
			writer.close();// end of for
			
			cp = (h1.prior*h1.cherry) + (h2.prior*h2.cherry) + (h3.prior*h3.cherry) + (h4.prior*h4.cherry) + (h5.prior*h5.cherry); // write the final calculated probabilities
			lm = (h1.prior*h1.lime) + (h2.prior*h2.lime) + (h3.prior*h3.lime) + (h4.prior*h4.lime) + (h5.prior*h5.lime);
			
			
	}// end of main
	
	
		
}// end of class
