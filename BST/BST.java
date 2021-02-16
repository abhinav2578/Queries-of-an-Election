package col106.assignment3.BST;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
class BSTNode<T extends Comparable, E extends Comparable>
{
	public T key;
	public E value;
	public BSTNode<T,E> left;
	public BSTNode<T,E> right;


	public BSTNode(T a,E b)
	{
		this.key=a;
		this.value=b;
		this.left=null;
		this.right=null;
	}

	public E getValue()
	{
        return this.value;
    }
}


public class BST<T extends Comparable, E extends Comparable> implements BSTInterface<T, E>  {
	/* 
	 * Do not touch the code inside the upcoming block 
	 * If anything tempered your marks will be directly cut to zero
	*/
	public static void main() {
		BSTDriverCode BDC = new BSTDriverCode();
		System.setOut(BDC.fileout());
	}
	/*
	 * end code
	 * start writing your code from here
	 */
	
	//write your code here
	public BSTNode<T,E> root=null;
	public HashMap<T,E> map=new HashMap<T,E>();

    private BSTNode<T,E> insert_helper(BSTNode<T,E> root,T key,E val)
	{
		if (root==null)
		{
			BSTNode<T,E> node=new BSTNode<T,E>(key,val);
			return node;
		}

		if (root.value.compareTo(val)>0)
		{
			root.left=insert_helper(root.left, key, val);
			return root;
		}
		else
		{
			root.right=insert_helper(root.right, key, val);
			return root;
		}

	}
	
	public void insert(T key, E value)
	{
		//write your code here
		this.root=insert_helper(this.root,key,value);
		this.map.put(key, value);
		
    }

	public void update(T key, E value)
	{
		//write your code here
		delete(key);
		insert(key, value);
    }

	private T min(BSTNode<T,E> root)
	{
		if (root==null)
		{
			return null;	
		}

		if (root.left==null)
		{
			return root.key;
		}
		return min(root.left);
	}

	private BSTNode<T,E> delete_helper(BSTNode<T,E> root,T key,E val)
	{
		if (root==null)
		{
			return null;	
		}

		if (root.value.compareTo(val)>0)
		{
			root.left=delete_helper(root.left, key, val);
			return root;
		}
		
		if (root.value.compareTo(val)<0)
		{
			root.right=delete_helper(root.right, key, val);
			return root;	
		}

		// if root is a leaf.
		if (root.left==null && root.right==null)
		{
			return null;	
		}

		// root has one child
		if (root.left==null)
		{
			return root.right;	
		}

		if (root.right==null)
		{
			return root.left;	
		}

		// root has two children
		T in_successor_key=min(root.right);
		root.key=in_successor_key;
		root.value=this.map.get(in_successor_key);
		T new_key=in_successor_key;
		E new_val=this.map.get(in_successor_key);
		root.right=delete_helper(root.right, new_key, new_val);
		return root;

	}

	public void delete(T key)
	{
		//write your code here
		E val=this.map.get(key);
		//System.out.println(val+".....");
		this.root=delete_helper(this.root,key,val);
		this.map.remove(key);
    }

	public void printBST ()
	{
		//write your code here
		Queue <BSTNode<T,E>> que=new LinkedList<BSTNode<T,E>>();
		que.add(this.root);

		while (!(que.isEmpty()))
		{
			BSTNode<T,E> curr=que.poll();
			System.out.println(curr.key.toString()+", "+curr.value.toString());
			if (curr.left!=null)
			{
				que.add(curr.left);	
			}
			if (curr.right!=null)
			{
				que.add(curr.right);	
			}
		}
    }

	public ArrayList<T> get_level_order()
	{
		ArrayList<T> ans=new ArrayList<T>();
		Queue <BSTNode<T,E>> que=new LinkedList<BSTNode<T,E>>();
		que.add(this.root);

		while (!(que.isEmpty()))
		{
			BSTNode<T,E> curr=que.poll();

			ans.add(curr.key);

			if (curr.left!=null)
			{
				que.add(curr.left);	
			}
			if (curr.right!=null)
			{
				que.add(curr.right);	
			}
		}
		return ans;
	}

	private void get_inorder_helper(BSTNode<T,E> root,ArrayList<T> ans)
	{
		if(root==null)
		{
			return;
		}
		if(root.left==null && root.right==null)
		{
			ans.add(root.key);
			return;
		}

		if (root.right==null && root.left.left==null && root.left.right==null)
		{
			get_inorder_helper(root.left, ans);
			ans.add(root.key);
			return;	
		}

		if (root.left==null && root.right.left==null && root.right.right==null)
		{
			ans.add(root.key);
			get_inorder_helper(root.right, ans);
			return;	
		}

		get_inorder_helper(root.left,ans);
		ans.add(root.key);
		get_inorder_helper(root.right, ans);
	}

	public ArrayList<T> get_inorder()
	{
		ArrayList<T> ans=new ArrayList<T>();
		get_inorder_helper(this.root,ans);
		return ans;
	}
}