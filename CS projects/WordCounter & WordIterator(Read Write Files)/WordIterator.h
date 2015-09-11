
#include <stdio.h>

// Class that iterates over all the words in a text.

class WordIterator {
  enum {MAXWORD = 200};

  char word[MAXWORD];
  int wordLength;

  FILE * _fd;

 public:

  // Creates WordIterator
  WordIterator();

  bool readFile(const char * fileName);

  // Get the next word or null if there are no more words.
  const char * next();

  ~WordIterator();
};

