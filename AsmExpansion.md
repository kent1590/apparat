# Introduction #

Apparat allows you to inline AVM2 bytecode directly into your ActionScript code. You can even mix both representations at the same time.

**Note:** Writing bytecode is an advanced topic and nothing for beginners. The intention of this Wiki page is not to explain what bytecode is and how it works. Bytecode optimization is really the last resort and not something you should rely on.

# AVM2 #

The AVM2 is a typical stack based virtual machine. You can read a lot more about it [at Adobe](http://learn.adobe.com/wiki/display/AVM2/ActionScript+Virtual+Machine+2). Especially part 5 on the instruction set is interesting for you.

## ASMify ##

To figure out how the AVM2 works and how certain operations are performed Apparat comes with an asmifier. Simply run the `asmifier` tool on a small test case SWF and you will get ASM code that you can simply copy and paste.

# `__asm` #

The asm keyword is a special ActionScript function that accepts other functions or constants from the `apparat.asm` package.

The operations in Apparat are not named 100% like Adobe describes them. `add_i` is for instance named `AddInt` and `subtract_i` is named `SubtractInt`. But if you have a decent IDE with autocompletion it will be very easy to figure out what the corresponding name is.

## Example ##

Push two integers onto the stack and add them. Store the result in a local register and trace it.

```
var result: int

__asm(
  PushInt(1),
  PushInt(2),
  AddInt
  SetLocal(result)
)

trace(result)
```

# Local variables #

You can simply use local variables in any operation that requires a local variable like GetLocal or SetLocal.

```
var one: int = 1
var two: int = 2
var three: int

__asm(
  GetLocal(one),
  GetLocal(two),
  AddInt,
  SetLocal(three)
)

trace(three)
```

# Control Flow #

You can mark a label via any String that is suffixed with a colon. "loop:" is for instance a valid label. You can jump to a label if you omit the colon. Statements like If`*`, Jump and LookupSwitch require a label as an argument.

```
__asm('loop:')

trace('Who wants to loop forever?')

__asm(Jump('loop'))
```

# `__as3` #

The `__as3` keyword allows you to mix ActionScript 3 code into your assembler code.

```
var one: int = 1
var two: int = 2
var three: int

__asm(
  __as3(one+two),
  SetLocal(three)
)

trace(three)
```

Note that you can also use as3 as a generic alternative to Push`*` operations. Usually you have to decide whether to use PushByte, PushShort or PushInt for example. But as3 will do that automatically for you.

```
var result: int

__asm(__as3(123), __as3(12345), AddInt, SetLocal(result))

trace(result)
```

# Name Lookup #

Name lookup is also explained in the Adobe Wiki and ASMify should show you how it works. All names are prefixed with `Abc`. This means a qualified name is called `AbcQName` for instance.

However Apparat comes with the `__as3` keyword and will infer the correct name when required.

```
__asm(
  FindPropStrict(__as3(trace)),
  PushString('Hello World!'),
  CallPropVoid(__as3(trace), 1)
)
```

# More Examples #

Apparat's Ersatz library is making heavy use of inline assembler.

  * [IntMath.as](http://code.google.com/p/apparat/source/browse/apparat-ersatz/src/main/as3/apparat/math/IntMath.as)
  * [SIMD.as](http://code.google.com/p/apparat/source/browse/apparat-ersatz/src/main/as3/apparat/math/SIMD.as)
  * [memcpy.as](http://code.google.com/p/apparat/source/browse/apparat-ersatz/src/main/as3/apparat/memory/memcpy.as)