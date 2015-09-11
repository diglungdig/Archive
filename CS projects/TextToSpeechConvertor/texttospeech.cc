#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <iostream>
#include "Sound.h"


using namespace std;

char* toLowAndRmpunc(char* & str)
{
	
	char* ptr = str;
	char* cpy = new char[strlen(str)+1];
	char* cptr = cpy;

	for(;*ptr!='\0';*ptr++)
	{
		if(!ispunct(*ptr)){
			*ptr = tolower(*ptr);
			*cptr = *ptr;
			 *cptr++;	}
	}

	*cptr = '\0';
	return cpy;
	
}


void addpause(Sound &s)
{
	Sound s1(1,44000,2);
	s1.read("pause.wav");
	s.append(&s1);

}


int main(int argc, char**argv)
{
	if(argc<2)
	{
		cout<<"No Words Entered :)"<<endl;
		exit(32);		
	}	

 	char* str = (char*)malloc(sizeof(char)*20);
	str = argv[1];

	str = toLowAndRmpunc(str);	

	//use strtok to seperate words, put the .wav at the end of every word and put them in to an 2d-array
		
	char *p;
	p = strtok(str," ");

	char**array = (char**)malloc(sizeof(char)*10000);
	int i = 0;
		
	while(p!=NULL)
	{
		array[i] = p;
		i++;
		p = strtok(NULL, " ");
	}

	//i = the total number of words
	
	int k = 0;
	char cha[100][100];
	for(;k<i;k++)
	{		
		strcpy(cha[k],array[k]);	
		strcat(cha[k],".wav");
	}



	//start to use sound class
	int y = 1;
	Sound totol(1,44000,2);

	if(totol.read(cha[0])==false){
		cout<<cha[0]<<":no such file name!"<<endl;
		exit(32);
					}	
	

	for(;y<i;y++)
	{
		addpause(totol);
		Sound s2(1,44000,2);
		if(s2.read(cha[y])==false){
			cout<<cha[y]<<":no such file name!"<<endl;
                	exit(32);
					}
				
		totol.append(&s2);
	}
	
	//write in
	
	char* name = (char*)malloc(sizeof(char));
	strcpy(name,argv[2]);
	strcat(name,".wav");

	totol.write(name);
	
	//free the memory
	free(array);	

}
