# Introduction #

Since the release of [Adobe Alchemy](http://labs.adobe.com/technologies/alchemy/) there is a new feature in the Flash Player which can be referred to as Alchemy memory or domain memory.

Basically this describes a `ByteArray` that has been set as `ApplicationDomain.currentDomain.domainMemory`.

```
var alchemyMemory: ByteArray = new ByteArray();
ApplicationDomain.currentDomain.domainMemory = alchemyMemory;
```

The only special thing about this `ByteArray` is that it may be accessed global in the ApplicationDomain context with very fast operations.

Those operations are not part of the ActionScript 3 language. Apparat offers a convinient way to use them via a [special API](http://code.google.com/p/apparat/source/browse/apparat-ersatz/src/main/as3/apparat/memory/Memory.as) that gets replaced with the corresponding operation.

## Overview ##
| **Operation** | **Apparat Version** | **Description** | **Size** |
|:--------------|:--------------------|:----------------|:---------|
| 0x35 | Memory.readByte | Read unsigned byte | 1b  |
| 0x36 | Memory.readShort | Read unsigned short | 2b |
| 0x37 | Memory.readInt | Read usigned int | 4b |
| 0x38 | Memory.readFloat | Read float | 4b |
| 0x39 | Memory.readDouble | Read double | 8b |
| 0x3a | Memory.writeByte | Write unsigned byte | 1b |
| 0x3b | Memory.writeShort | Write unsigned short | 2b |
| 0x3c | Memory.writeInt | Write unsigned int | 4b |
| 0x3d | Memory.writeFloat | Write float | 4b |
| 0x3e | Memory.writeDouble | Write double | 8b |
| 0x50 | Memory.sign1 | 1bit sign extension | 1b |
| 0x51 | Memory.sign8 | 8bit sign extension | 1b |
| 0x52 | Memory.sign16 | 16bit sign extension | 2b |

The `Memory.select` operation is special since no counter part exists but it basically just sets the `ApplicationDomain.currentDomain.domainMemory` to the provided `ByteArray`.

This also means that `Memory.select` is a more expensive operation that should not be used too often.

# Details #

Although the memory may be accessed very fast there are a couple of things to note. Especially the way the `ByteArray` is handled internally by the Flash Player.

## Alchemy Implementation Details ##
There are two important things to note that might cause you trouble.

  1. Alchemy memory is always considered `Endian.LITTLE_ENDIAN`
  1. Once a ByteArray has been passed to `Memory.select` its length is fixed. If you change the length of the `ByteArray` you need to call `Memory.select` again.

The `apparat.memory.ImmutableByteArray` class prevents changes to its `length` and `endian` properties.

## Float vs. Double ##
You should never use `Memory.writeFloat` if you have severe memory problems in terms of available memory.

The Flash Player's `Number` type is backed by a `double` so each operation on a `float` involves at least two casts.

```
var x: Number = Memory.readFloat(0) //cast from float to double
x += 1.0
Memory.writeFloat(x, 0) //cast from double to float
```

If you have an issue and need to drop 50% of the memory usage you can go from double to float since a float is only 4b vs. 8b for a double. Note that this also means you will loose a lot of precision.

## Working With Memory ##
When working with a huge `ByteArray` you might run into trouble since you need to allocate some pieces of the `ByteArray` for certain operations and independent parts of your code want to access it.

Since this is a global `ByteArray` there are two options. You can either load completely independent parts of your application in a different `ApplicationDomain`. There is always one unique `domainMemory` per `ApplicationDomain`. This is rather complicated and not always possible.

The other option is to use the MemoryPool library by Apparat which performs the task of allocating and releasing memory blocks for you. Note however that fragmentation can occurr and you might run out of space in the Alchemy memory.

## Using Alchemy Without Apparat ##
You might not want to integrate Apparat into your build process for several reasons. For instance your IDE could not allow a post-compile task before launching your SWF.

The `apparat-ersatz.swc` has been designed to use the Alchemy operations internally even if you do not run Apparat. This means you can access the memory without Apparat but might want to use it on your continuous integration server for instance.