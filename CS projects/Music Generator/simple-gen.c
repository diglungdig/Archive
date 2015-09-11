
#include <stdio.h>
#include <math.h>
#include <stdlib.h>

// Generate tones
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
} WaveHeader;

void assignLittleEndian4(unsigned char * dest, unsigned int value) {
	dest[0] = value & 0xFF;
	dest[1] = (value >> 8) & 0xFF;
	dest[2] = (value >> 16) & 0xFF;
	dest[3] = (value >> 24) & 0xFF;
}

int main(int argc, char ** argv) 
{
	if (argc <3) {
		printf("generate freq secs\n");
		exit(1);
	}

	int freq;
	int secs;

	sscanf(argv[1], "%d", &freq);
	sscanf(argv[2], "%d", &secs);

	printf("Generating simple WAV file sample.wav\n");
	printf("freq=%d secs=%d\n", freq, secs);

	unsigned int numChannels = 1;
	unsigned int sampleRate = 44100;
	unsigned int bitsPerSample = 16;
	unsigned int numSamples = secs * sampleRate;

	unsigned int dataSize = numSamples * numChannels * (bitsPerSample/8) ;
	
	// Subtract 1 for the data[1] in the header
	unsigned int fileSize = sizeof(WaveHeader) - 1 + dataSize;  

	// Allocate memory for header and data
	WaveHeader * hdr = (WaveHeader *) malloc(fileSize);		

	// Fill up header
	hdr->chunkID[0]='R'; hdr->chunkID[1]='I'; hdr->chunkID[2]='F'; hdr->chunkID[3]='F';
	
	assignLittleEndian4(hdr->chunkSize, fileSize - 8);
	
	hdr->format[0] = 'W'; hdr->format[1] = 'A'; hdr->format[2] = 'V'; hdr->format[3] = 'E';	
	hdr->subchunk1ID[0]='f'; hdr->subchunk1ID[1]='m'; hdr->subchunk1ID[2]='t'; hdr->subchunk1ID[3]=' ';
	
	assignLittleEndian4( hdr->subchunk1Size,16);
	
	hdr->audioFormat[0] = 1; hdr->audioFormat[1]=0;
	hdr->numChannels[0] = numChannels; hdr->numChannels[1]=0;
	
	assignLittleEndian4(hdr->sampleRate, sampleRate);
	assignLittleEndian4(hdr->byteRate, numChannels * sampleRate * bitsPerSample/8);
	
	hdr->blockAlign[0] = numChannels * bitsPerSample/8; hdr->blockAlign[1]=0;
	hdr->bitsPerSample[0] = bitsPerSample; hdr->bitsPerSample[1]=0;
	hdr->subchunk2ID[0]='d'; hdr->subchunk2ID[1]='a'; hdr->subchunk2ID[2]='t'; hdr->subchunk2ID[3]='a';
	
	assignLittleEndian4(hdr->subchunk2Size, dataSize);

	// Generate data
	int i;

	// Check if machine is big endian or little endian
	int littleEndian = 0;
	int test=5;
	if (*(char*)&test==5) {
		littleEndian = 1;
	}

	for (i=0; i<numSamples; i++) {
		short value = 32767*sin(3.1415*freq*i/sampleRate);
		if (littleEndian) {
			hdr->data[2*i] = *((unsigned char *)&value);
			hdr->data[2*i+1] = *((unsigned char *)&value + 1);
		}
		else {
			hdr->data[2*i+1] = *((unsigned char *)&value);
			hdr->data[2*i] = *((unsigned char *)&value + 1);
		}
	}

	// Write file to disk
	FILE * f = fopen("sample.wav", "w+");
	if (f==NULL) {
		printf("Could not create file\n");
		perror("fopen");
		exit(1);
	}

	fwrite(hdr, fileSize, 1, f);

	fclose(f);
}
