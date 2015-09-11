

#include <string.h>
#include <stdio.h>
#include <iostream>
#include <stdlib.h>



#include "TreeDictionary.h"

TreeDictionary::TreeDictionary()
{
	//build root
	_root = NULL;	
	_numberOfEntries = 0;
}

// Add a record to the dictionary. Overwrite data if key
// already exists. It returns false if key already exists
bool 
TreeDictionary::addRecord( const char * key, int data) {
		
	TreeNode * n = _root;
	TreeNode * prev = NULL;

	while(n != NULL){
		if(strcmp(key,n->_key)< 0){
			prev = n;
			n = n->_left;

		}
		else if(strcmp(key,n->_key)> 0){
                        prev = n;
                        n = n->_right;

                }
		else{
			n->_data = data;
			return false;

		}

	}

	//creat new node
	n = new TreeNode();
	n->_key = key;
	n->_data = data;
	n->_left = NULL;
	n->_right = NULL;	

	

	//attach
	if(prev == NULL){
		_root = n;
	}


	else{
		if(strcmp(key,prev->_key)< 0)
			prev->_left = n;
		else
			prev->_right = n;		

	}


	
	_numberOfEntries++;
	return true;
}

int
TreeDictionary::numberOfEntries() {
  
	return _numberOfEntries++;
	
}

// Find a key in the dictionary and return corresponding record.
bool 
TreeDictionary::findRecord( const char * key, int & data) {
	TreeNode *n = _root;
	while(n!=NULL){
		if(strcmp(key,n->_key)<0)
			n = n->_left;
		else if(strcmp(key,n->_key)>0) 
                        n = n->_right;
		else
		{
			data = n->_data;
			return true;
		}

	}


	return false;
}


void 
TreeDictionary::printIndented(TreeNode * node, int level) {
	if(node == NULL) 
	{
	printf("NULL\n");
	return;
	}
		
	//print tab
	int n = 0;
	for(;n<level;n++){
		printf("  ");
	}

        std::cout<<node->_key<<":"<<node->_data<<std::endl;	
	
	if(node->_left == NULL){
         	int j = 0;
		for(;j<level+1;j++){
                	printf("  ");
        	}
		printf("NULL\n");
				
        }
        else{
           
		printIndented(node->_left,level+1);
        }

        
       if(node->_right == NULL){
               int k = 0;
		for(;k<level+1;k++){
                        printf("  ");
                }	
		printf("NULL\n");	

        }
        else{
                printIndented(node->_right,level+1);
        }

}

void 
TreeDictionary::printIndented(){
	printIndented(_root,0);
}






void 
TreeDictionary::print(TreeNode *node) {
	if(node == NULL) return;
	std::cout<<node->_key<<":"<<node->_data<<std::endl;
	print(node->_left);
	print(node->_right);	
	

}

void TreeDictionary::print()
{
	print(_root);

}


// Max depth in tree
void
TreeDictionary::computeDepthHelper(TreeNode * node, int depth, int & currentMaxDepth) {
		
		if(node==NULL)
		{
			return;
		}
		
		if(depth>currentMaxDepth){
			currentMaxDepth++;
		}	

		if(node->_left!=NULL||node->_right!=NULL)
		{

		TreeNode * n = node;
			
		
		computeDepthHelper(n->_right,depth+1,currentMaxDepth);
		computeDepthHelper(n->_left,depth+1,currentMaxDepth);
		
		
		}
				

}

int 
TreeDictionary::maxDepth() {

	int cmd = -1;
		
 	computeDepthHelper(_root,0,cmd); 
	
	return cmd;
}


TreeDictionary::~TreeDictionary(){
	del(_root);
}

void
TreeDictionary::del(TreeNode *node){
	if(node!=NULL){
		del(node->_left);
		del(node->_right);	
		delete node;
		_numberOfEntries = 0;
	}
}


// Minimum key. The leftmost node
const char * 
TreeDictionary::minimumKey()
{
        TreeNode * n = _root;
	while(n->_left!=NULL){
		n = n->_left;
			}
   return n->_key;
}



// Maximum Key. The rightmost node
const char * 
TreeDictionary::maximumKey()
{
	TreeNode * n =_root;
	while(n->_right!=NULL){
		n = n->_right;
			}
 
   	return n->_key;
}


void
TreeDictionary::collectKeys(TreeNode * node, const char **keys, int & nkeys)
{



	
	if(node==NULL)
	{
		return;	
	}
	
	TreeNode * n = node;

	if(n->_left!=NULL)
	{	
		collectKeys(n->_left,keys,nkeys);
				
	}	
	
	keys[nkeys] = n->_key;
	nkeys++;	

	
	if(n->_right!=NULL)
	{
		collectKeys(n->_right,keys,nkeys);

	}
			

}

const char ** 
TreeDictionary::keys( int & nkeys)
{

	const char** keys = (const char**)malloc(sizeof(char*));	
	nkeys  = 0;
		
	collectKeys(_root,keys,nkeys);

	
	return keys;
	
}
