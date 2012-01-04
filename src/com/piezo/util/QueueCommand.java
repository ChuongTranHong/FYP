package com.piezo.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class QueueCommand  {
	public static List<Command> listCommand=new LinkedList<Command>();
	public static boolean enQueue(Command arg0) {
		try{
		listCommand.add(listCommand.size(), arg0);
		return true;
		// TODO Auto-generated method stub
		}catch(Exception e){
		return false;
		}
	}

	public static Command deQueue(){
		if(listCommand.size()>0) return listCommand.remove(0);
		else return null;
	}
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (listCommand.size()==0);
	}

	public Iterator<Command> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public static int size() {
		// TODO Auto-generated method stub
		return listCommand.size();
	}

}
