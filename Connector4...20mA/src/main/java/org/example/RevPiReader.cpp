#include "jni.h"
#include <stdio.h>
#include <time.h>
#include <stdint.h>
#include "org_example_RevPiReader.h"
#include "piControl.h"
#include "piControlIf.hpp"
#include "piControlIf.cpp"

//Some code is commented out. This is example code in case I need to access single bits so
//I know where to look for the right way to do it.
JNIEXPORT jint JNICALL Java_org_example_RevPiReader_readFromOffset
  (JNIEnv* env, jobject obj, jint offset)
  {
    piControl piC;
    uint8_t readData;
    uint32_t offsetUint = offset;
    SPIVariable spiVariableOut = {"InputValue_1", 0, 0, 0};

    piC.GetVariableInfo(&spiVariableOut);

    //SPIValue spiValue = {0, 0, 0};
    //spiValue.i16uAddress = spiVariableOut.i16uAddress;
    //spiValue.i8uBit = 8;
    //spiValue.i8uValue = 0;
         //offset; size of result (2 bytes); pointer where to store result
    piC.Read(offsetUint, 2, &readData);

    //piC.GetBitValue(&spiValue);
    return readData;
  }
