
#include "Sound.h"
#include "stdio.h"
#include "stdlib.h"
#include "sys/types.h"
#include "sys/stat.h"


//Constuctor for Sound object
Sound::Sound(int numChannels,int sampleRate,int bitsPerSample)
{

	
	hdr = (WaveHeader*)malloc(sizeof(WaveHeader)-1+maxSamples*bytesPerSample);
	this->numChannels = numChannels;
	this->sampleRate = sampleRate;
	this->bitsPerSample = bitsPerSample;
	this->bytesPerSample = bitsPerSample/8;
	this->lastSample = 0;	
	this->maxSamples = 20000;
	this->fileSize = 0;
	this->dataSize = 0;	


	
}

//Destructor
Sound::~Sound(void)
{
	delete hdr;
}


//read method
bool
Sound::read(const char * fileName)
{
        FILE * f = fopen(fileName, "r");

        if (f == NULL) {
                return false;
        }

        // Get size of file
        struct  stat  buf;
        stat(fileName, &buf);
        int fsize = buf.st_size;


        // Allocate memory for wave file
        hdr = (WaveHeader *)malloc(fsize+1);
        
	// Read file
        fread(hdr, fsize, 1, f);

        fclose(f);

        // Fill it up
        this->numChannels = hdr->numChannels[0];
        this->sampleRate = getLittleEndian4(hdr->sampleRate);
        this->bitsPerSample = hdr->bitsPerSample[0]+(hdr->bitsPerSample[1]<<8);
        this->bytesPerSample = (this->bitsPerSample/8)*this->numChannels;

        this->lastSample = getLittleEndian4(hdr->subchunk2Size)/this->bytesPerSample;
        this->maxSamples = this->lastSample;
	
	this->dataSize = lastSample*bytesPerSample;
	this->fileSize = sizeof(WaveHeader)-1+dataSize;
	

        return true;

}


//write method
void
Sound::write(const char* fileName)
{
	FILE * f = fopen(fileName,"w+");
	if(f==NULL){
		printf("No Such File.");
		return;	
		}


	
	this->dataSize = lastSample*bytesPerSample;
        this->fileSize = sizeof(WaveHeader)-1+dataSize;
	
	hdr->chunkID[0]='R'; hdr->chunkID[1]='I'; hdr->chunkID[2]='F'; hdr->chunkID[3]='F';
	assignLittleEndian4(hdr->chunkSize, this->fileSize - 8);
	hdr->format[0] = 'W'; hdr->format[1] = 'A'; hdr->format[2] = 'V'; hdr->format[3] = 'E';
	hdr->subchunk1ID[0]='f'; hdr->subchunk1ID[1]='m'; hdr->subchunk1ID[2]='t'; hdr->subchunk1ID[3]=' ';
	assignLittleEndian4( hdr->subchunk1Size,16);
	hdr->audioFormat[0] = 1; hdr->audioFormat[1]=0;
	hdr->numChannels[0] = numChannels; hdr->numChannels[1]=0;
	assignLittleEndian4(hdr->sampleRate, this->sampleRate);
	assignLittleEndian4(hdr->byteRate, this->numChannels * this->sampleRate * this->bitsPerSample/8);
	hdr->blockAlign[0] = this->numChannels * this->bitsPerSample/8; hdr->blockAlign[1]=0;
	hdr->bitsPerSample[0] = this->bitsPerSample; hdr->bitsPerSample[1]=0;
	hdr->subchunk2ID[0]='d'; hdr->subchunk2ID[1]='a'; hdr->subchunk2ID[2]='t'; 
	hdr->subchunk2ID[3]='a';
	assignLittleEndian4(hdr->subchunk2Size, this->dataSize);
	 

	fwrite(hdr,fileSize,1,f);
}



//add method
void
Sound::add_sample(int sample)
{
	//check if reach the maxSamples
	if(maxSamples == lastSample)
	{
		maxSamples += 2000;
		//reallocate mem
		hdr = (WaveHeader*)realloc(hdr,sizeof(WaveHeader)-1+maxSamples*bytesPerSample);	

	}
	
	int samVal = sample;
	
	if(isLittleEndian())
	{
		hdr->data[2*lastSample+1] = *((unsigned char*)&samVal+1);
		hdr->data[2*lastSample] = *((unsigned char*)&samVal);
	}
	else
	{
		hdr->data[2*lastSample+1] = *((unsigned char*)&samVal);
                hdr->data[2*lastSample] = *((unsigned char*)&samVal+1);
	}		 
	
	lastSample++;


}


//get the ith sample
int
Sound::get_sample(int i)
{	
	int sampleVal = 0;
	
	if(isLittleEndian())
	{
		*((unsigned char*)&sampleVal) = hdr->data[2*i];
		*((unsigned char*)&sampleVal+1) = hdr->data[2*i+1];

	}
	else
	{
		*((unsigned char*)&sampleVal) = hdr->data[2*i+1];
		*((unsigned char*)&sampleVal+1)  = hdr->data[2*i];

	}
	
		return sampleVal; 
}

//append method
void
Sound::append(Sound * src)
{
	int i = 0;
	for(;i<src->lastSample;i++)
	{
	add_sample(src->get_sample(i));
	}

}



//Endian Dealing
void
Sound::assignLittleEndian4(unsigned char * dest, unsigned int value) 
{
	dest[0] = value & 0xFF;
	dest[1] = (value >> 8) & 0xFF;
	dest[2] = (value >> 16) & 0xFF;
	dest[3] = (value >> 24) & 0xFF;
}
int
Sound::getLittleEndian4(unsigned char* dest)
{
	int le = 0;
	le = dest[0]+(dest[1]<<8)+(dest[2]<<16)+(dest[3]<<24);

	return le;
}
int 
Sound::isLittleEndian()
{
	int num = 6;
	
	if(*(char*)&num == 6)
	{
		return 1;
	}
	else 	
		return 0;

}


