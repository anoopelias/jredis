package jredis;

public class RdfTestUtil {
    
    private RdfTestUtil(){
    }

    public static final byte[] NUMBERS = {
        0x10, //len 
        0x10, 0x00, 0x00, 0x00, //zlbytes
        0x0e, 0x00, 0x00, 0x00, //zltail
        0x02, 0x00, //zllen
        
        // e1
        0x00, // prev len
        0x03, // spl flag
        0x4f, 0x6e, 0x65, // raw 'One'
        
        // e2
        0x06, // prev len
        (byte) 0xf9, // special flag
        
        (byte) 0xff // end
     };

     public static final byte[] NUMBERS_STRING_SCORE = {
         0x1c, //len 
         0x1c, 0x00, 0x00, 0x00, //zlbytes 4
         0x14, 0x00, 0x00, 0x00, //zltail 4
         0x02, 0x00, //zllen 2
         
         // e1
         0x00, // prev len 1
         0x08, // spl flag 1
         0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x73,  // raw 'Fourties' 8
         
         // e2
         0x06, // prev len 1
         (byte) 0x05, // special flag 1
         0x34, 0x37, 0x2e, 0x33, 0x32, // raw '47.32' 5
         
         (byte) 0xff // end 1
      };

     public static final byte[] NUMBERS_INVALID_STRING_SCORE = {
         0x1c, //len 
         0x1c, 0x00, 0x00, 0x00, //zlbytes 4
         0x14, 0x00, 0x00, 0x00, //zltail 4
         0x02, 0x00, //zllen 2
         
         // e1
         0x00, // prev len 1
         0x08, // spl flag 1
         0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x73,  // raw 'Fourties' 8
         
         // e2
         0x06, // prev len 1
         (byte) 0x05, // special flag 1
         0x34, 0x37, 0x2e, 0x6d, 0x32, // raw '47.m2' 5
         
         (byte) 0xff // end 1
      };

     public static final byte[] THREE_ENTRIES = {
         0x26, //len 
         0x26, 0x00, 0x00, 0x00, //zlbytes 4
         0x14, 0x00, 0x00, 0x00, //zltail 4
         0x03, 0x00, //zllen 2
         
         // e1
         0x00, // prev len 1
         0x08, // spl flag 1
         0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x73,  // raw 'Fourties' 8
         
         // e2
         0x06, // prev len 1
         (byte) 0x05, // special flag 1
         0x34, 0x37, 0x2e, 0x6d, 0x32, // raw '47.m2' 5

         // e3
         0x00, // prev len 1
         0x08, // spl flag 1
         0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x46,  // raw 'FourtieF' 8

         (byte) 0xff // end 1
      };
     
     public static final byte[] NUMBERS_NO_END = {
         0x0f, //len 
         0x0f, 0x00, 0x00, 0x00, //zlbytes
         0x0e, 0x00, 0x00, 0x00, //zltail
         0x02, 0x00, //zllen
         
         // e1
         0x00, // prev len
         0x03, // spl flag
         0x4f, 0x6e, 0x65, // raw 'One'
         
         // e2
         0x06, // prev len
         (byte) 0xf9, // special flag
      };


     public static final byte[] NUMBERS_LARGE_KEY_START = {
         0x1c, //len 
         0x58, 0x06, 0x00, 0x00, //zlbytes 4
         0x14, 0x00, 0x00, 0x00, //zltail 4
         0x02, 0x00, //zllen 2
         
         // e1
         0x00, // prev len 1
         (byte) 0x46, 0x40, // special flag 2
     };
     
     public static final byte[] NUMBERS_LARGE_KEY = { 0x46, 0x6f, 0x75, 0x72, 0x74, 0x69, 0x65, 0x73 }; // raw 'Fourties' 1600
         
     public static final byte[] NUMBERS_LARGE_KEY_END = {
         // e2
         (byte) 0xfe, // prev len 1
         0x43, 0x06, 0x00, 0x00, // prev len 4
         (byte) 0xd0, // special flag 1
         ~0x0f, ~(byte) 0xa9, ~0x53, ~0x42, // -1112779024 signed integer 4
         
         (byte) 0xff // end 1
      };

}
