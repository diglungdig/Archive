#ifndef SOUND_H
#define SOUND_H

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

class Sound
{
	unsigned long numChannels;
	unsigned long sampleRate;
	unsigned long bitsPerSample;
	unsigned long bytesPerSample;
	unsigned int lastSample;
	unsigned int maxSamples;
	unsigned int fileSize;
	unsigned int dataSize;

	// Points to the wave file in memory
	WaveHeader * hdr;
public:
	// Create an empty wave file with these parameters.
	Sound( int numChannels=1, int sampleRate=44000, int bitsPerSample=2);

	// Destructor
	~Sound(void);

	// Read a wave file from file name. Samples and parameters are overwritten
	bool read(const char * fileName);

	// Write wave file in fileName
	void write(const char * fileName);

	// Add a new sample at the end of the wave file.
	void add_sample(int sample);

	// Get ith sample
	int get_sample(int i);
	
	//littleEndian and bigEndian Dealing
	void assignLittleEndian4(unsigned char* dest, unsigned int value);
	
	int getLittleEndian4(unsigned char* dest);

	int isLittleEndian();

	
	// Append a Sound src to the end of this wave file. 
	void append(Sound * src);
			

};

#endif

