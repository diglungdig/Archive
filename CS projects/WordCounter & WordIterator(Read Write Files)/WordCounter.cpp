
#include <stdio.h>
#include <stdlib.h>

#include "WordIterator.h"
#include "TreeDictionary.h"

void printUsage() {
  printf("Usage: WordCounter file\n");
  exit(1);
}

int
main(int argc, char ** argv)
{
  if (argc < 2) {
    printUsage();
  }

  const char * fileName = argv[1];

  WordIterator wordIter;

  bool exists =  wordIter.readFile(fileName);
  if (!exists) {
    printf("File not found!\n");
    exit(1);
  }

  TreeDictionary dict;

  const char * word;
  int i = 0;
  while (word = wordIter.next()) {
    printf("%d:%s\n", i,word);
    int number = 0;

    // Find number of occurrences and add it by one.
    dict.findRecord(word,number);
    dict.addRecord(word,number+1);

    i++;
  }

  dict.print();
}

