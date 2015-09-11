
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>



// Convert freq to wave file. The freq file has a sequence of frequency and duration pairs that
// make the song. 
typedef struct WaveHeader {
	unsigned char chunkID[4];      // big endian 
	unsigned char chunkSize[4];    // little endian
	unsigned char format[4];       // big endian
	unsigned char subchunk1ID[4];  // big endian
	unsigned char subchunk1Size[4];// little endian
	unsigned char audioFormat[2]; // little endian
	unsigned char numChannels[2];  // little endian
	unsigned char sampleRate[4];   // little endian 
	unsigned char byteRate[4];     // little endian
	unsigned char blockAlign[2];   // little endian
	unsigned char bitsPerSample[2];// little endian
	unsigned char subchunk2ID[4];  // big endian
	unsigned char subchunk2Size[4];// little endian
	unsigned char data[1];         // little endian
}WaveHeader;

void assignLittleEndian4(unsigned char * dest, unsigned int value) {
	dest[0] = value & 0xFF;
	dest[1] = (value >> 8) & 0xFF;
	dest[2] = (value >> 16) & 0xFF;
	dest[3] = (value >> 24) & 0xFF;
}






int main(int argc, char ** argv)
{
        if (argc <2) {
                printf("freq2wav freq-file\n");
                exit(1);
        }


	//
	// 1. Read freq-file. The freq file contains pairs (frequency, msecs)
	//    that indicates the signal frequency and duration that will be
	//    added to the wave file.
	// 2. For every freq.msecs pair generate the tone and added it to the wave file.
	// 3. Store the wave file. If the input file was file.freq, then store the wav file
	//    as file.wav
	

//check file's name
	int i;
	char *name = (char*)malloc(sizeof(char));
	for(i = 0; argv[1][i]!='\0';i++)
	{
		
		name[i] = argv[1][i];
		if(argv[1][i] == '.')
		{
			int j = i;
		 	char *check = (char*)malloc(sizeof(char));
						
			
			strcat(check,&argv[1][j+1]);
			
		
			if(strcmp(check,"freq")!=0)
			{
				printf("Expected .freq file.\n");	
				exit(32);
			}

		break;
		}
	}

	if(argv[1][i]=='\0')
	{
		printf("Expected .freq file.\n");
		exit(32);
	}


//check the existance of the file
	printf("freqFile: %sfreq\n",name);
	printf("waveFile: %swav\n",name);
	char* name1 = (char*)malloc(sizeof(char));
	strcpy(name1,name); 
	FILE *freqF = fopen(strcat(name,"freq"),"r");
	FILE *freqW = fopen(strcat(name1,"wav"),"w+");
	if(!freqF||!freqW)
		{
		printf("File \"%s\" not found\n",argv[1]);
		exit(32);
		}
			
	printf("Frequency file: (Hz,ms)");

//obtain input
	int times =  0;
	unsigned int numSamples = 0;
	int structFT[200][2];
	
	for(;fscanf(freqF,"%d %d",&structFT[times][0],&structFT[times][1])&&!feof(freqF);times++)
	{
		if(times%10==0){
			printf("\n");
			printf("%d: ",times);
			}

		printf("(%d,%d) ",structFT[times][0],structFT[times][1]);
		numSamples = numSamples+ structFT[times][1]*44100/1000;
	}
	
	printf("\n");


//form wav header 
	unsigned int numChannels = 1;
	unsigned int sampleRate = 44100;
	unsigned int bitsPerSample = 16;
	
	unsigned int dataSize = numSamples*numChannels*(bitsPerSample/8);
	unsigned int fileSize = sizeof(WaveHeader)+dataSize-1;
	
	WaveHeader *hdr = (WaveHeader*)malloc(fileSize);
	hdr->chunkID[0] = 'R';hdr->chunkID[1] = 'I';hdr->chunkID[2] = 'F';hdr->chunkID[3] = 'F';
	
	assignLittleEndian4(hdr->chunkSize,fileSize-8);
	 
	hdr->format[0] = 'W';hdr->format[1] = 'A';hdr->format[2] = 'V';hdr->format[3] = 'E';
	hdr->subchunk1ID[0] = 'f';hdr->subchunk1ID[1] = 'm';hdr->subchunk1ID[2] = 't';hdr->subchunk1ID[3] = ' ';
	
	assignLittleEndian4(hdr->subchunk1Size,16);
	
	hdr->audioFormat[0] = 1;hdr->audioFormat[1] = 0;
	hdr->numChannels[0] = numChannels;hdr->numChannels[1] = 0;
	
	assignLittleEndian4(hdr->sampleRate,sampleRate);
	assignLittleEndian4(hdr->byteRate,numChannels*sampleRate*(bitsPerSample/8));

	hdr->blockAlign[0] = numChannels*bitsPerSample/8; hdr->blockAlign[1] = 0;
	hdr->bitsPerSample[0] = bitsPerSample; hdr->bitsPerSample[1] = 0;
	hdr->subchunk2ID[0] = 'd';hdr->subchunk2ID[1] = 'a';hdr->subchunk2ID[2] = 't';hdr->subchunk2ID[3] = 'a';
	
	assignLittleEndian4(hdr->subchunk2Size,dataSize);
	

//check endian
	int littleEndian = 0;
	int test = 3;
	if(*(char*)&test == 3)
	{
		littleEndian = 1;
	
	}



//input data
	int count1,count2,count3 = 0;
	for(count1 = 0; count1<times;count1++)
	{
			
		
		for(count2 = 0;count2<structFT[count1][1]*sampleRate/1000;count2++)
		{
			short value = 32767*sin(3.1415*structFT[count1][0]*count2/(sampleRate));
			if(littleEndian)
			{
				hdr->data[2*count3] = *((unsigned char *)&value);
				hdr->data[2*count3+1] = *((unsigned char *)&value + 1);
			}
			else 
			{
				hdr->data[2*count3+1] = *((unsigned char *)&value);
				hdr->data[2*count3] = *((unsigned char *)&value + 1); 
			}

			count3++;
		}

	}
	

	
//output file
	fwrite(hdr,fileSize,1,freqW);
//free and close	
	free(hdr);fclose(freqW);fclose(freqF);
		
}
