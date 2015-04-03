# Introduction #

Macro expansion is inlining for the poor. Apparat has a very spartanic API that allows you to write efficient and maintainable code. It is easy to implement. So here it is. In order to use macro expansion for your SWF you would use TDSI to do the job like "tdsi -i input.swf -o output.swf"

The drawback is that you will have to follow some rules. And the syntax is not always very pleasing at this stage.


# About Macros #

A macro is usually a snippet you define somewhere in your code. Tiny little monkeys inside of a compiler copy and replace all macro occurrences with the code defined for it.

Apparat brings those tiny little monkeys back to ActionScript.

## Defining A Macro ##

If you want to define a macro you have to extend from `apparat.inline.Macro`. Then you write some static methods in your class and you are all set. Note that a Macro may never ever return a value.

```
package com.acme {
  import apparat.inline.Macro

  public final class Foo extends Macro {
    public static function increment(x: int): void {
      x++
    }
  }
}
```

Et voilà. Now whenever you write `Foo.increment(x)` it will be replaced with `x++`. Without any drawback in terms of speed.
As you can see, usually `Foo.increment(x)` would never alter the value of `x`. With macro expansion it does. This is useful since it is the only way to return parameters.

## Golden Rules ##

The current state of the macro expansion is bare bones, dead simple. Apparat will not optimize your code or do anything clever with it, it will just replace those macros. In order to do that you have to follow some rules:

  * You may pass only(!) local variables as parameters to a macro.
  * A macro class may contain only static methods that return void.
  * A macro may not contain exception handlers.

There are some more rules -- like a Macro may not be a dynamic class. But those are more like silver rules, not golden.

### Local variables only ###
Because of simplicity, speed and filesize the macro expansion does not allow you to pass anything else than local variables to a macro. This sucks but keeps the code really simple.

Assume that we have a method `Foo.add(x, y)` which does nothing else than `x += y`. This could would be illegal:

```
var a: int = 0
Foo.add(a, 2 + 2)
```

`2 + 2` is not a local variable so Apparat will tell you that it found some weird illegal operation. Probably `PushByte(2)` in this case. So how would you do this? You will have to create a temporary parameter for `2 + 2`. This is legal again:

```
var a: int = 0
var b: int = 2 + 2
Foo.add(a, b)//note, only local variables!
```

### Return type must be void ###
This leads to an error. A method may not return a value.

```
package com.acme {
  import apparat.inline.Macro

  public final class Foo extends Macro {
    public static function add(x: int, y: int): int {
      return x + y
    }
  }
}
```

This is how you would return a value using macro expansion.

```
package com.acme {
  import apparat.inline.Macro

  public final class Foo extends Macro {
    public static function add(x: int, y: int, result: int): void {
      result = x + y
    }
  }
}
```

### A macro may not contain exception handlers ###

There is no nice solution to this problem. This is illegal:

```
package com.acme {
  import apparat.inline.Macro

  public final class Foo extends Macro {
    public static function bar(): void {
      try { /* ... */ } catch(e: Error) { trace(e) }
    }
  }
}
```

The only way to handle an exception is like this. You wrap the handler around the macro.

```
try { Foo.bar() } catch(e: Error) { trace(e) }
```