package avl-tree-methods;

public class AVLTree {
    private TreeNode<T> root;
	public int size;

    private TreeNode<T> insertHelper(T value, TreeNode<T> root) {
		if(root == null) { //current root node is null, we can insert the new value here
			TreeNode<T> newNode = new TreeNode<T>(value); //create a new node with the given value
			updateHeight(newNode); //update the new node's height
			size++; //update the size of the tree
			return newNode;
		} else {
			//compare given value to current root value
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) {
	    		// duplicate element -- return existing node
	    		return root;
	    	} else if (comparison < 0) {
	    		// given value is less than current root value -- go left
	    		root.setLeft(insertHelper(value, root.left));	    		
	    	} else {
	    		// given value is more than current root value -- go right
	    		root.setRight(insertHelper(value, root.right));
	    	}
	    	updateHeight(root); //update height of the current root on the way back up the call stack because the
								//new node was inserted below it
	    	//rebalance the subtree if needed
			return rebalance(root);
	    }
	}

    private TreeNode<T> removeHelper(T value, TreeNode<T> root) {
	    if (root == null) { // did not find element, return null
	    	return null;
	    } else {
			//calculate whether the value we are looking for is greater than, less than, or equal to the curent root
	    	int comparison = value.compareTo(root.value);
		
	    	if (comparison == 0) { //value is equal to current root, found element to remove
	    		if (root.left == null || root.right == null) {
	    			// value we want to remove has at most one subtree --
	    			// return whichever one is not null (or return null
	    			// if both are null)
	    			size--; //update size
	    			return (root.left == null ? root.right : root.left);
	    		} else {
	    			// value we want to remove has two subtrees -- replace current node
	    			// with successor and recursively remove the successor.
	    			T minValue = minValueInSubtree(root.right); //find successor
	    			root.value = minValue; //replace current node with successor

					// recursively travel to the successor and remove it by travelling to the right
					//update the right child of the current root since there could be rebalancing of the right subtree
	    			root.setRight(removeHelper(minValue, root.right)); 
																		
	    		}
	    	} else if (comparison < 0) {
	    		// value we want to remove is less than current root -- go left
	    		root.setLeft(removeHelper(value, root.left));  		
	    	} else {
	    		// value we want to remove is more than current root -- go right
	    		root.setRight(removeHelper(value, root.right));
	    	}
			//update height of the subtree as we travel back up the call stack
	    	updateHeight(root);
			//update the root of the subtree after rebalancing
	    	TreeNode<T> newRoot = rebalance(root); 
    		return newRoot;
	    }
	}

    private void updateHeight(TreeNode<T> root) {
	    if(root.right == null && root.left == null) { //node is leaf & has height 0
	    	root.height = 0;
	    } else if(root.right == null) { //node does not have a right child, so the height must come from the left child
	    	root.height = root.left.height + 1;
	    } else if(root.left == null) { //node does not have a left child, so the height must come from the right child
	    	root.height = root.right.height + 1;
	    } else { //node has both children, take the child with the larger height
	    	root.height = Math.max(root.right.height, root.left.height) + 1;
	    }
	    
	}

    private int getBalance(TreeNode<T> root) {
	    //calculate balance factor using left subtree height - right subtree height
		//a null child has a height of -1
		if(root.left == null && root.right == null) { //both are null
			return 0;
		} else if (root.left == null) { //only left child is null
			return -1 - root.right.height;
		} else if (root.right == null) { //only right child is null
			return root.left.height + 1;
		} else { //both are not null
			return root.left.height - root.right.height;
		}

	}

	//assume that the balance factor can only ever be off by one
	//maintained by rebalancing immeidately after every insertion and removal
    private TreeNode<T> rebalance(TreeNode<T> root) {
		if(getBalance(root) == 2) { //left subtree is unbalanced
			if(getBalance(root.left) == -1) { //right subtree of left subtree is unbalanced, solve by doing a left rotation before doing the right rotation
				root.setLeft(leftRotate(root.left));
			}
			return rightRotate (root);	//left subtree of left subtree is unbalanced, solve using one right rotation	
		} else if (getBalance(root) == -2) { //right subtree is unbalanced
			if(getBalance(root.right) == 1) { //left subtree of right substree is unbalanced, solve by doing a right rotation first
				root.setRight(rightRotate(root.right));
			}
			return leftRotate(root); //right subtree of right subtree is unbalanced, solve by doing one left rotation
		}
		//return the new root after rebalancing
	    return root;
	}

    //Tree's root is assumed to have a left child
    private TreeNode<T> rightRotate(TreeNode<T> root) {
		//set new root as current root's left child
		TreeNode<T> newRoot = root.left;
		//detach the new root's right child
		TreeNode<T> orphan = newRoot.right;
		//set current root as new root's right child
		newRoot.setRight(root);
		//set detached node as current root's left child
		root.setLeft(orphan);
		
		//update the height of the new and old roots
		updateHeight(root);
		updateHeight(newRoot);

		return newRoot;

	}

    //Tree's root is assumed to have a right child
    private TreeNode<T> leftRotate(TreeNode<T> root) {
		//set new root as current root's right child
		TreeNode<T> newRoot = root.right;
		//detach the new root's left child
		TreeNode<T> orphan = newRoot.left;
		//set current root as new root's left child
		newRoot.setLeft(root);
		//set detached node as current root's right child
		root.setRight(orphan);
		
		//update the height of the new and old roots
		updateHeight(root);
		updateHeight(newRoot);

		return newRoot;
	}
}
