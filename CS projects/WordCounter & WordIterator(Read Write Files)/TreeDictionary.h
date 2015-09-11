
#ifndef Tree_h
#define Tree_h

#include "Dictionary.h"

class TreeNode {

  friend class TreeDictionary;

  const char * _key;
  int  _data;

  TreeNode *_left;
  TreeNode *_right;

};

class TreeDictionary : public Dictionary {

  friend class TreeDictionaryIterator;

  int _numberOfEntries;

  TreeNode * _root;

  // Helper private functions. You may use them or not.
  void print(TreeNode * node);

  void printIndented(TreeNode * node, int level);

  // Help compute minDepth and maxDepth
  void computeDepthHelper(TreeNode * node, int depth, int & currentMaxDepth);

  // Helper to collect keys
  void collectKeys(TreeNode * node, const char **keys, int & nkeys);

public: 
        TreeDictionary();

	// Add a record to the dictionary. Overwrite data if key
	// already exists. It returns false if key already exists
   	bool addRecord( const char * key, int data);

    	// Find a key in the dictionary and return corresponding record.
	bool findRecord(const  char * key, int & data);

	// Number of items in dictionary
	int numberOfEntries();

	// Minimum key. The leftmost node
	const char * minimumKey();

	// Maximum Key. The rightmost node
	const char * maximumKey();

	// Print tree normal
	void print();

	// Print tree with indentation
	void printIndented();

	// Max depth in tree
	int maxDepth();

	// Returns an array with all the keys. nkeys is updated with the number of keys.
	const char **keys(int & nkeys);

	// Destructor
	~TreeDictionary();
	void del(TreeNode * n);
};

#endif

