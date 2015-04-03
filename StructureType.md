# Introduction #
With access to the fast Alchemy memory it is desirable to have a higher level of abstraction for the data it contains. Imagine you store a two-component vector in the Alchemy memory. This is how we would access the vector and use it.

Writing to memory:
```
var pointer: int = 123
var x: Number = Math.random()
var y: Number = Math.random()
Memory.writeDouble(pointer, x)
Memory.writeDouble(pointer + 8, y)
```

Reading from memory:
```
var pointer: int = 123
var x: Number = Memory.readDouble(pointer)
var y: Number = Memory.readDouble(pointer + 8)
```

A compound action:
```
var pointer: int = 123
var x: Number = Memory.readDouble(pointer)
var y: Number = Memory.readDouble(pointer + 8)
x += 1.0
y += 1.0
Memory.writeDouble(pointer, x)
Memory.writeDouble(pointer + 8, y)
```

The amount of noise in your code increases since you are dealing with the raw memory access all the time. When not using Alchemy memory your code would look like this:

```
var point: Point = somePoint;
x += 1.0;
y += 1.0;
```

Apparat has the Structure type to get around this issue and let you concentrate more on the implementation instead of how things are laid out in memory.

The resulting code would look like this:

```
var pointer: int = 123;
var point: Point = map(pointer, Point);
point.x += 1.0;
point.y += 1.0;
```

Apparat will do all the Alchemy handling in the background for you. No object is allocated. `trace(point.x, point.y)` will translate to `trace(Memory.readDouble(pointer), Memory.readDouble(pointer+8))` behind the scenes.

# Usage #
## Defining a Structure type ##
In order to use the mappings you need to extend from apparat.memory.Structure.

```
import apparat.memory.Structure

public class PointStruct extends Structure {
  public var x: Number;
  public var y: Number;
}
```

The variables `x` and `y` are by default mapped to double types. They are placed in the same order into the memory as the compiler puts them into the bytecode. If you want to have control over the order you may use the `[Map]` annotation.

## Allocating Structures ##
There are some more helper methods like `sizeOf` that determine the size of a structure in bytes. `sizeOf(PointStruct)` would return `16` since two double values (each 8 bytes) are used.

Since no extra information is stored with a Structure you can map that value directly to Alchemy memory.

```
var ram: ByteArray = new ByteArray()

// Allocate enough space for 1000 PointStruct instances.
ram.length = sizeOf(PointStruct) * 1000 

Memory.select(ram)
```

## Using Structures ##
Let us assume you have allocated enough space for 1000 points in Alchemy memory. Now you want to do something with that information.

```
// In our case the offset in Alchemy memory is 0.
var point: PointStruct = map(0, PointStruct)
for(var i: int = 0; i < 1000; i++) {
  point.x = Math.random()
  point.y = Math.random()

  // Move to the next point
  point.next()
}
```

Structures come with even more helper methods like `next`, `prev`, `seekTo`, `seekBy`, etc.

**Note:** Since there is no object allocation for a structure you cannot pass references around. This means if you want to use the same Structure in two methods you have to pass the pointer and call `map` again in he second method.

## Type Mappings ##
`x` and `y` of the `PointStruct` are mapped to Double. This is because Apparat does some default mapping between ActionScript and Alchemy. The default for Number is double and the default for int is int(woah!).

Since there are some more types that can be stored in Alchemy here is a table that shows which type you have to use for which Alchemy type:

| **AS3** | **Alchemy** |
|:--------|:------------|
| int | int |
| int | byte |
| Number | double |
| Number | float |

### The Map Annotation ###
In order to tell Apparat which type you want to use you can use the Map annotation.

```
public class ExampleStruct extends Structure {
  [Map(type='float')] public var floatValue: Number;
  [Map(type='double')] public var doubleValue: Number;
  [Map(type='int')] public var intValue: int;
  [Map(type='byte')] public var byteValue: int;
}
```

**Note:** Do not forget to use `--keep-as3-metadata Map` when using the `[Map]` annotation.

## Union Types ##
You are maybe familiar with union types from C which allow you to use the same Memory location with a different view.

The `[Map]` annotation can be used to specify a position, relative from the offset of the class.

```
public class Union extends Structure {
  [Map(type='int', pos=0)] public var intValue: int;
  [Map(type='float', pos=0)] public var floatValue: Number;
}
```

So now `intValue` and `floatValue` would be mapped to the same memory location. The `pos` argument is always relative to the size of the largest type at the position.

Of course you could use this Union type now to implement your own Quake3 inverse square root calculation.