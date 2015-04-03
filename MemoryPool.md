# Description #

The MemoryPool class allows you to share a single ByteArray instance for multiple tasks without worring about organization.
It is used to allocate and free memory in that ByteArray.

# Technical details #

The first 1024 bytes of the ByteArray are unused space. Those bytes are considered temporary and may be used for calculations and tasks that do not store persistent information.

The MemoryPool will never try to defragment the ByteArray automatically and it will also never change the size of the Memory. It is the developers burden to choose a correct initial size. An IMemoryPoolStrategy is responsible for preventing defragmentation.

You can not expect that `MemoryPool.allocate()` will return you a MemoryBlock with the exact same size you have requested. Memory operations usually round to the next power of two. So something like `MemoryPool.allocate(1023)` will probably result in a MemoryBlock with a length of 1024 bytes.

# Strategies #

The MemoryPool can be extended with custom strategies.

## StaticChunksStrategy ##

The StaticChunksStrategy is the default strategy of the MemoryPool. It devides the ByteArray into a list of equal chunks.
The StaticChunksStrategy in its current implementation is not trying to prevent defragmentation.

# Example #

In this example a pixel buffer is created and used to set content of a BitmapData.

```
var bitmapData: BitmapData = new BitmapData( 320, 240, false, 0 );
addChild( new Bitmap( bitmapData ) );

// Initialize the MemoryPool with default settings.
MemoryPool.initialize();

// Allocate 320 * 240 * 4 bytes of memory.
// Color information like 0xRRGGBB is stored as an unsigned int which has a length of four bytes.
var pixelBuffer: MemoryBlock = MemoryPool.allocate( ( 320 * 240 ) << 2 );

var n: int = ( 320 * 240 ) << 2;
var i: int = 0;

for( ; i < n; i += 4 )
{
  // Fill the pixel buffer with some color
  Memory.writeInt( 0xff00ff, pixelBuffer.position + i );
}

// Fill the bitmapData with the pixelBuffer content
bitmapData.lock();
MemoryPool.buffer.position = pixelBuffer.position;
bitmapData.setPixels( bitmapData.rect, MemoryPool.buffer );
bitmapData.unlock( bitmapData.rect );

// Free the space used by the pixelBuffer if we do not need it any longer
MemoryPool.free( pixelBuffer );
```