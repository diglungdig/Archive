
#include <stdio.h>
#include <stdlib.h>
#include "WordIterator.h"
#include <iostream>
#include <string>
#include <cstring>


using namespace std;

WordIterator::WordIterator()
{
	wordLength = 0;	
}

bool
WordIterator::readFile(const char * fileName)
{
	_fd = fopen(fileName,"r");
   
   	if(_fd!=NULL)
	{
		return true;
	}

  	return false;

}

const char * 
WordIterator::next()
{
	

	if(feof(_fd))
		return NULL;

	

	char c = fgetc(_fd);	

	

	while(c=='\n')
        {
               c=fgetc(_fd);
        }	


	if(c!=EOF)
	{
		char* ca = (char*)malloc(sizeof(char));
		
		memset(word,'\0',sizeof(word));
        	wordLength = 0;
		
		char* ptr =  ca;		
		

		while(c!=' ')
		{
							
			*ptr++ =c;
			c = fgetc(_fd);
			wordLength++;

			if(c =='\n'||c == EOF)
			{
				
				break;
			}

		}

			memcpy(word,ca,strlen(ca)+1);
			return ca;

	}	
		


	
	fclose(_fd);
	
	return NULL;	
	
	

}

WordIterator::~WordIterator(){
}

