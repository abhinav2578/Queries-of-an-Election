package col106.assignment3.Election;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Map;

import col106.assignment3.BST.BST;

class Candidate
{
    String name;
    String candID;
    String state;
    String district;
    String constituency;
    String party;
    String votes;
    

    public  Candidate(String name , String candID , String state , String district , String constituency , String party , String votes)
    {
        this.name=name;
        this.candID=candID;
        this.state=state;
        this.district=district;
        this.constituency=constituency;
        this.party=party;
        this.votes=votes;
    }

}


public class Election implements ElectionInterface {
	/* 
	 * Do not touch the code inside the upcoming block 
	 * If anything tempered your marks will be directly cut to zero
	*/
	public static void main() {
		ElectionDriverCode EDC = new ElectionDriverCode();
		System.setOut(EDC.fileout());
	}
	/*
	 * end code
	 */
	
	//write your code here
	private BST<String,Integer> book_keeper=new BST<String,Integer>();                           // candID - votes
	private HashMap<String,BST<String,Integer>> s_map=new HashMap<String,BST<String,Integer>>();  //bst of parties - votes.
	private HashMap<String,BST<String,Integer>> c_map=new HashMap<String,BST<String,Integer>>();  // bst of candID - votes.
	private HashMap<String,Integer> parties_votes=new HashMap<String,Integer>();                  // total votes per party.
	private HashMap<String,Candidate> candID_to_info=new HashMap<String,Candidate>();             // candID to all the information.
	private HashMap<String,Integer> state_totalvotes=new HashMap<String,Integer>();               // total votes in a state.

	public void insert(String name, String candID, String state, String district, String constituency, String party, String votes)
	{
		//write your code here 
		// for book-keeping ds
		this.book_keeper.insert(candID,Integer.valueOf(votes));

		// for s_map
		if (!(s_map.containsKey(state)))
		{
			BST<String,Integer> b= new BST<String,Integer>();
			b.insert(party, Integer.valueOf(votes));
			s_map.put(state, b);
		}
		else
		{
			BST<String,Integer> bst_s=s_map.get(state);
			if (!(bst_s.map.containsKey(party)))
			{
				bst_s.insert(party, Integer.valueOf(votes));	
			}
			else
			{
				Integer temp=bst_s.map.get(party);
				bst_s.update(party, temp+Integer.valueOf(votes));
				
			}
		}

		// for c_map
		if (!(c_map.containsKey(constituency)))
		{
			BST<String,Integer> c=new BST<String,Integer>();
			c.insert(candID, Integer.valueOf(votes));
			c_map.put(constituency, c);	
		}
		else
		{
			BST<String,Integer> bst_c=c_map.get(constituency);
			bst_c.insert(candID, Integer.valueOf(votes));
		}

		// total-votes per party map
		if (!(parties_votes.containsKey(party)))
		{
			parties_votes.put(party, Integer.valueOf(votes));	
		}
		else
		{
			Integer prev_votes=parties_votes.get(party);
			parties_votes.replace(party, prev_votes, prev_votes+Integer.valueOf(votes));
		}

		// candID-to-info
		Candidate C=new Candidate(name,candID,state,district,constituency,party,votes);
		candID_to_info.put(candID,C);

		// states - total votes map
		if (!(state_totalvotes.containsKey(state)))
		{
			state_totalvotes.put(state, Integer.valueOf(votes));	
		}
		else
		{
			Integer prev=state_totalvotes.get(state);
			state_totalvotes.replace(state, prev, prev+Integer.valueOf(votes));
		}
	}

	public void updateVote(String name, String candID, String votes)
	{
		//write your code here
		Integer old_votes=Integer.valueOf(candID_to_info.get(candID).votes);
		String district=candID_to_info.get(candID).district;
		String state=candID_to_info.get(candID).state;
		String party=candID_to_info.get(candID).party;
		String constituency=candID_to_info.get(candID).constituency;

		// book-keeping ds
		this.book_keeper.update(candID, Integer.valueOf(votes));

		// s_map
		HashMap<String,Integer> temp1=s_map.get(state).map;
		Integer party_prev_votes_in_state=temp1.get(party);
		this.s_map.get(state).update(party, party_prev_votes_in_state+Integer.valueOf(votes)-old_votes);

		// c_map
		HashMap<String,Integer> temp2=c_map.get(constituency).map;
		this.c_map.get(constituency).update(candID, Integer.valueOf(votes));

		// parties-votes map
		Integer party_prev_votes_overall=parties_votes.get(party);
		parties_votes.replace(party, party_prev_votes_overall, party_prev_votes_overall+Integer.valueOf(votes)-old_votes);

		// candId-info map
		Candidate C1=new Candidate(name,candID,state,district,constituency,party,votes);
		candID_to_info.replace(candID,C1);

		// state-total votes map
		Integer state_prev_votes=state_totalvotes.get(state);
		state_totalvotes.replace(state, state_prev_votes, state_prev_votes+Integer.valueOf(votes)-old_votes);

	}

	public void topkInConstituency(String constituency, String k)
	{
		//write your code here
		ArrayList<String> candId_list=this.c_map.get(constituency).get_inorder();
		if (Integer.valueOf(k)>=candId_list.size())
		{
			for (int i = candId_list.size()-1; i >= 0; i--)
			{
				String candid=candId_list.get(i);
				String name=this.candID_to_info.get(candid).name;
				String party=this.candID_to_info.get(candid).party;
				System.out.println(name+", "+candid+", "+party);	
			}	
		}
		else
		{
			int count=0;
			for (int i = candId_list.size()-1; i>=0; i--)
			{
				String candid=candId_list.get(i);
				String name=this.candID_to_info.get(candid).name;
				String party=this.candID_to_info.get(candid).party;
				System.out.println(name+", "+candid+", "+party);	
				count++;
				if (count==Integer.valueOf(k))
				{
					break;	
				}
			}
		}
	}

	public void leadingPartyInState(String state)
	{
		//write your code here
		Iterator it1=this.s_map.get(state).map.entrySet().iterator();
		Integer leader_votes_in_state=0;
		String leader_name_in_state="";

		while (it1.hasNext())
		{
			Map.Entry ele=(Map.Entry)it1.next();
			Integer curr_votes=Integer.valueOf((int)ele.getValue());
			String curr_name=String.valueOf(ele.getKey());
			if (curr_votes>leader_votes_in_state)
			{
				leader_votes_in_state=curr_votes;
				leader_name_in_state=curr_name;
			}
		}
		ArrayList<String> sorted_order=new ArrayList<String>();
		Iterator it2=this.s_map.get(state).map.entrySet().iterator();
		while (it2.hasNext())
		{
			Map.Entry ele=(Map.Entry)it2.next();
			if (ele.getValue().equals(leader_votes_in_state))
			{
				sorted_order.add(ele.getKey().toString());
			}
		}

		String[] sorted_order1=sorted_order.toArray(new String[sorted_order.size()]);
		Arrays.sort(sorted_order1);

		for (String st : sorted_order1)
		{
			System.out.println(st);	
		}

	}

	public void cancelVoteConstituency(String constituency)
	{
		//write your code here
		// generating Arraylist of candidates that have to be deleted and get it sorted in ascending order of candID.
		ArrayList<String> cand_to_del=this.c_map.get(constituency).get_level_order();
		String[]cand_to_del1=cand_to_del.toArray(new String[cand_to_del.size()]);
		Arrays.sort(cand_to_del1);
		
		// book-keeping ds
		for (String str : cand_to_del1)
		{
			this.book_keeper.delete(str);	
		}

		// s_map
		String first_candID=cand_to_del1[0];
		String state_to_be_changed=candID_to_info.get(first_candID).state;
		HashMap<String,Integer> map_state=this.s_map.get(state_to_be_changed).map;

		for (String str : cand_to_del1)
		{
			String party=candID_to_info.get(str).party;
			Integer cand_votes=Integer.valueOf(candID_to_info.get(str).votes);
			Integer party_votes=map_state.get(party);
			this.s_map.get(state_to_be_changed).update(party, party_votes-cand_votes);	
		}

		// c_map
		this.c_map.remove(constituency);

		// parties-votes
		for (String str : cand_to_del1)
		{
			String party=candID_to_info.get(str).party;
			Integer cand_votes=Integer.valueOf(candID_to_info.get(str).votes);
			Integer party_votes=parties_votes.get(party);
			parties_votes.replace(party, party_votes, party_votes-cand_votes);	
		}
		
		// state-total_votes
		for (String str : cand_to_del1)
		{
			Integer cand_votes=Integer.valueOf(candID_to_info.get(str).votes);
			Integer state_votes=state_totalvotes.get(state_to_be_changed);
			state_totalvotes.replace(state_to_be_changed, state_votes, state_votes-cand_votes);	
		}

		// candID-info
		for (String str : cand_to_del1)
		{
			candID_to_info.remove(str);	
		}

	}

	public void leadingPartyOverall()
	{
		//write your code here
		Iterator it1=this.parties_votes.entrySet().iterator();
		Integer leader_votes=0;
		String leader_name="";

		while (it1.hasNext())
		{
			Map.Entry ele=(Map.Entry)it1.next();
			Integer curr_votes=Integer.valueOf((int)ele.getValue());
			String curr_name=String.valueOf(ele.getKey());

			if (curr_votes>leader_votes)
			{
				leader_votes=curr_votes;
				leader_name=curr_name;
			}
		}

		ArrayList<String> sorted_order=new ArrayList<String>();
		Iterator it2=this.parties_votes.entrySet().iterator();
		while (it2.hasNext())
		{
			Map.Entry ele=(Map.Entry)it2.next();
			if (Integer.valueOf((int)ele.getValue()).equals(leader_votes))
			{
				sorted_order.add(String.valueOf(ele.getKey()));
			}
		}
		String[] sorted_order1=sorted_order.toArray(new String[sorted_order.size()]);
		Arrays.sort(sorted_order1);

		for (String st : sorted_order1)
		{
			System.out.println(st);	
		}
	}

	public void voteShareInState(String party,String state)
	{
		//write your code here
		Integer party_votes_in_state=this.s_map.get(state).map.get(party);
		Integer total_votes_in_state=this.state_totalvotes.get(state);

		Double ans=(double)party_votes_in_state/(double)total_votes_in_state;
		ans=ans*100;
		System.out.println(ans.intValue());
	}
	
	public void printElectionLevelOrder()
	{
		//write your code here
		ArrayList<String> arr=this.book_keeper.get_level_order();
		for (String str : arr)
		{
			String name=candID_to_info.get(str).name;
			String state=candID_to_info.get(str).state;
			String district=candID_to_info.get(str).district;
			String constituency=candID_to_info.get(str).constituency;
			String party=candID_to_info.get(str).party;
			String votes=candID_to_info.get(str).votes;

			System.out.println(name+", "+str+", "+state+", "+district+", "+constituency+", "+party+", "+votes);	
		}
	}
}











