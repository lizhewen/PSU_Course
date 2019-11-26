#include "float_i2f.h"
float_bits float_i2f(int i) {
  unsigned sign = i >> 31;
  unsigned exp;
  unsigned frac;
  //Special Case: when i is -2^31
  if (i == 0x80000000) {
    return 0xCF000000;
  }
  if (i < 0) {
    i = -i;
  }
  else if (i == 0) {
    return 0;
  }
  //get position of leading 1
  int pos;
  for (pos = 31; pos >= 0; pos--) {
    if (i >> pos == 1) {
      exp = 127 + pos; //exp = bias + position of leading 1
      frac = i << (32-pos); //remove everything before including leading 1
      break;
    }
  }
  if (pos > 23) { //if frac part is greater than 23 bits
    unsigned last_8_bits = (frac << 23) >> 24;
    frac = frac >> 9;
    if (last_8_bits > 0x80) {
      frac++; //round-up
    }
    else if (last_8_bits == 0x80) { //round to even
      //unsigned frac_last_bit = frac & 0x001;
      if (frac&0x1) {
        frac++; //round-up to even
      }
    }

    //exp += 1 when frac overflows after round-up
    if(frac & 0x800000) {
      exp++;
      frac = 0;
    }
  }
  else { // no need to round when frac <= 23 bits
    frac = frac >> 9;
  }
  return (sign<<31) | (exp<<23) | frac;
}
