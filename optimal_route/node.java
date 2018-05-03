import java.util.*;
import java.io.*;

public class node
{
	String state;
	int depth;
	int totalcost;
	node parent;
	ArrayList<node>children = new ArrayList<node>();

	public node(String state,int depth, int totalcost,node parent)
	{
		this.state=state;
		this.depth=depth;
		this.totalcost=totalcost;
		this.parent=parent;
	}

	public node(String state,int depth, int totalcost,node parent,ArrayList<node>children)
	{
		this.state=state;
		this.depth=depth;
		this.totalcost=totalcost;
		this.parent=parent;
		this.children=children;
	}

	String getstate()
	{
		return this.state;
	}

	int getdepth()
	{
		return this.depth;
	}

	int gettotalcost()
	{
		return this.totalcost;
	}

	node getParent()
	{
		return this.parent;
	}

	ArrayList<node> getchildren()
	{
		return this.children;
	}

	void setchildren(ArrayList<node>children)
	{
		 this.children=children;

	}
}


