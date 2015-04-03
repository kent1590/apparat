# Introduction #

SIMD means single instruction for multiple data. Thisn is in our case of course not hardware accelerated. However we have a very fast Macro available that fulfills this purpose.

# Details #

The `apparat.math.SIMD` class is a [Macro](MacroExpansion.md) which contains methods to work with Alchemy memory. All methods are prefixed with a type. `i` means integer, `f` float and `d` double.

Every operation takes an address in Alchemy memory space. It will act on the four values next to that address corresponding to the given size.

Imagine the following case `SIMD.iset(x, 0)`:

```
Memory.writeInt(x     , 0)
Memory.writeInt(x +  4, 0)
Memory.writeInt(x +  8, 0)
Memory.writeInt(x + 12, 0)
```

As you can see one instruction changed four values in Memory. A more advanced example:

```
SIMD.iset(x, 1)
SIMD.iset(y, 2)
SIMD.iadd(x, y)
```

In this case the Alchemy add address `x`, `x + 4`, `x + 8` and `x + 12` will hold the value `3`.