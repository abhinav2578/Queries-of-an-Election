package col106.assignment3.Heap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class Heap<T extends Comparable, E extends Comparable> implements HeapInterface <T, E> {
	/* 
	 * Do not touch the code inside the upcoming block 
	 * If anything tempered your marks will be directly cut to zero
	*/
	public static void main() {
		HeapDriverCode HDC = new HeapDriverCode();
		System.setOut(HDC.fileout());
	}
	/*
	 * end code
	 */
	
	// write your code here
	private ArrayList<E> arr=new ArrayList<E>();
	private HashMap<T,E> k_v=new HashMap<T,E>();
	private HashMap<E,T> v_k=new HashMap<E,T>();
	
	private void perlocate_up(int childIndex)
	{
		while (childIndex>0)
		{
			int parentIndex=(childIndex-1)/2;
			
			if (this.arr.get(childIndex).compareTo(this.arr.get(parentIndex))>0)
			{
				E temp=this.arr.get(childIndex);
				this.arr.set(childIndex, this.arr.get(parentIndex));
				this.arr.set(parentIndex, temp);
				childIndex=parentIndex;
			}
			else
			{
				break;
			}
		}
	}

	public void insert(T key, E value)
	{
		//write your code here
		this.k_v.put(key, value);
		this.v_k.put(value, key);
		this.arr.add(value);
		perlocate_up(this.arr.size()-1);
	}

	private void perlocate_down(int parentIndex)
	{
		int leftchildIndex=2*parentIndex+1;
		int rightchildIndex=2*parentIndex+2;

		while (leftchildIndex < this.arr.size())
		{
			int minIndex=parentIndex;

			if (this.arr.get(minIndex).compareTo(this.arr.get(leftchildIndex))<0)
			{
				minIndex=leftchildIndex;	
			}

			if (rightchildIndex < this.arr.size() && this.arr.get(minIndex).compareTo(this.arr.get(rightchildIndex))<0)
			{
				minIndex=rightchildIndex;	
			}

			if (parentIndex==minIndex)
			{
				break;	
			}

			E temp=this.arr.get(parentIndex);
			this.arr.set(parentIndex, this.arr.get(minIndex));
			this.arr.set(minIndex, temp);

			parentIndex=minIndex;
			leftchildIndex=2*parentIndex+1;
			rightchildIndex=2*parentIndex+2;
		}
	}

	public E extractMax()
	{
		//write your code here
		E max_val=this.arr.get(0);
		this.arr.set(0, this.arr.get(this.arr.size()-1));
		this.arr.remove(this.arr.size()-1);
		perlocate_down(0);
		T key_to_del=v_k.get(max_val);
		v_k.remove(max_val);
		k_v.remove(key_to_del);
		return max_val;
	}

	public void delete(T key)
	{
		//write your code here
		E to_del_val=k_v.get(key);
		int req_index_first_time=0;
		
		for (; req_index_first_time < this.arr.size(); req_index_first_time++)
		{
			if (this.arr.get(req_index_first_time).compareTo(to_del_val)==0)
			{
				break;	
			}	
		}

		k_v.remove(key);
		v_k.remove(to_del_val);
		
		// check whether the node which is required to be deleted is the last node or not.
		if (req_index_first_time==this.arr.size()-1)
		{
			this.arr.remove(req_index_first_time);
		}
		else
		{
			E substi_val=this.arr.get(this.arr.size()-1);
			this.arr.set(req_index_first_time, substi_val);
			this.arr.remove(this.arr.size()-1);
			perlocate_up(req_index_first_time);

			int req_index_second_time=0;
			for (; req_index_second_time < this.arr.size(); req_index_second_time++)
			{
				if (this.arr.get(req_index_second_time).compareTo(substi_val)==0)
				{
					break;	
				}
			}

			perlocate_down(req_index_second_time);
		}
	}

	public void increaseKey(T key, E value)
	{
		//write your code here
		E prev_val=k_v.get(key);
		int req_index=0;

		for (; req_index < this.arr.size(); req_index++)
		{
			if (this.arr.get(req_index).compareTo(prev_val)==0)
			{
				break;	
			}	
		}

		k_v.replace(key, value);
		v_k.remove(prev_val);
		v_k.put(value, key);

		this.arr.set(req_index, value);
		perlocate_up(req_index);
	}

	public void printHeap()
	{
		//write your code here
		if (this.arr.size()>0)
		{
			Queue<Integer> que=new LinkedList<Integer>();
			que.add(0);

			while (!(que.isEmpty()))
			{
				Integer currIndex=que.poll();
				E val_to_print=this.arr.get(currIndex);
				System.out.println(v_k.get(val_to_print).toString()+", "+val_to_print.toString());
				Integer leftchildIndex=2*currIndex+1;
				Integer rightchildIndex=2*currIndex+2;

				if (leftchildIndex < this.arr.size())
				{
					que.add(leftchildIndex);	
				}

				if (rightchildIndex < this.arr.size())
				{
					que.add(rightchildIndex);	
				}
			}
		}
	}	


}
