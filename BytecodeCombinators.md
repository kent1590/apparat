# Introduction #

Apparat comes with a powerful feature to analyze or manipulate bytecode using a domain specific language.

The bytecode combinators are influenced by Scala's excellent parser combinators. If you are familiar with them you will have no problem with our implementation.

# Details #

A sequence of bytecode combinators is called bytecode chain and represented by the `BytecodeChain` class. The `Bytecode` class defines several methods that work with chains.

  * `contains[A](chain: BytecodeChain[A]): Boolean` Whether or not the bytecode contains an occurrence of the given chain.
  * `indexOf[A](chain: BytecodeChain[A]): Int` The index of the given chain in the bytecode. -1 if it could not be found.
  * `rewrite[A <: AbstractOp](rule: BytecodeChain[List[A]]): Boolean` Rewrites all occurrences of the given chain with its result. `true` if the bytecode has been modified; `false` otherwise.
  * `replace[A](chain: BytecodeChain[A])(body: A => List[AbstractOp]): Boolean` Replace all occurrences of the given chain with its mapped result. As you can see, rewrite is defined as `replace(chain){ x => x }`. Returns `true` if the bytecode has been modified; `false` otherwise.
  * `replaceFrom[A](fromIndex: Int, chain: BytecodeChain[A])(body: A => List[AbstractOp]): Boolean` Like replace but starting from a given index.
  * `replaceAll[A](chain: BytecodeChain[A])(rule: A => List[AbstractOp]): Boolean` Replaces all occurrences and emerging occurrences for a chain. Imagine you search for `A B`and replace it with `A` then the first iteration for `A B B` will result in `A B`. Only `replaceAll` will run a second iteration here and fold `A B` to `A`.

## Defining chains ##

You need the following two import statements to make use of all implicit conversions:

```
import apparat.bytecode.combinator.BytecodeChains._
import apparat.bytecode.combinator._
```

The general syntax for a bytecode chain is `(pattern) ^^ { result }`. A pattern is defined by a sequence of operations. A sequence is created using the `~` operator. For instance `PushInt(1) ~ Pop()` is the sequence that will search for an occurrence of `PushInt(1)` followed by a `Pop()` operation.
A disjunction is defined with the `|` operator. `(PushInt(1) | PushInt(2)) ~ Pop()` will search for PushInt(1) or PushInt(2) followed  by a Pop() operation.
Optional operations are marked with the `?` operator or the `opt` function. `opt(PushInt(1)) ~ Pop()` is equal to `PushInt(1)? ~ Pop()` and will search for the sequence `PushInt(1)` followed by `Pop()` or only `Pop()` since `PushInt(1)` is optional.
If you want to repeat an operation you can use the `*` operator or the `rep` function. Those operations occur zero or more times. `PushInt(1) ~ Nop()* ~ Pop()` is equal to `PushInt(1) ~ rep(Nop()) ~ Pop()` and searches for any `PushInt(1)` followed by zero or more `Nop()` operations and a finaly `Pop()` operation.

As you can see you do not want to search always for `PushInt(1)` but maybe for any `PushInt(x)` or only those with an `x` that fulfills a special condition. The `partial` combinator comes to rescue. It allows you to define a matcher for a specific case.

```
partial { case PushInt(x) if 0 == x % 2 => PushInt(x) } ~ Pop()
```

This rule would match all `Pop()` operations preceeded by a `PushInt(x)` for all x mod2 = 0. As you can see you can also map the result of a partial case with any result. This is also valid:

```
(partial { case PushInt(x) if 0 == x % 2 => x } ~ Pop()) ^^ {
  case x ~ Pop() => x
}
```

`x` is in this case an integer and so the result of this chain is typed `Int`.

Sometimes `partial` will not satisfy your needs. This can be the case if you require always the result of a partial case but match only for certain conditions. In that case, `filter` comes to the rescue and requires a boolean mapping that keeps the result.

```
filter { case pushInt: PushInt => true } ~ Pop()
```

This rule is of course equal to `partial { case PushInt(x) => PushInt(x) } ~ Pop()`. It is also a shortcut to write that you want to match on any parameterized operation.

Note that you can also combine your bytecode chains together. Just like you know it from the Scala combinators you can write `myRule1 ~ myRule2` which will match for `myRule1` first and then `myRule2`.

# Examples #

Here are some examples from the apparat code and their explanation.

```
lazy val ifFalse = (PushFalse() ~ partial {case ifFalse: IfFalse => ifFalse}) ^^ {
  case PushFalse() ~ IfFalse(marker) => Jump(marker) :: Nil
  case _ => error("Unreachable code.")
}
```

This example searches for a `PushFalse()` followed by any `IfFalse(marker)`. The result is transformed with a `Jump` to the marker. The chain's result type is therefore `List[Jump]` which satisfies `List[AbstractOp]`. Note the default `_` case which we insert only because of a problem in the current combinator system.

```
lazy val unnecessaryIntCast = ((AddInt() | SubtractInt() | MultiplyInt()) ~ ConvertInt()) ^^ {
  case x ~ ConvertInt() => x :: Nil
  case _ => error("Unreachable code.")
}
```

This chain matches `AddInt() or `SubtractInt()` or `MultiplyInt()` followed by a `ConvertInt()`. The result is only the mathematical operation without the cast.

## Stripper ##

The stripper tool is a more complex example for bytecode combinators. It defines two rules.

```
private lazy val qname = AbcQName(Symbol("trace"), AbcNamespace(AbcNamespaceKind.Package, Symbol("")))

private lazy val traceVoid = {
  partial { case CallProperty(name, args) if name == qname => args } ~ Pop() ^^ {
    case args ~ Pop() => CallPropVoid(qname, args) :: Nil
    case _ => error("Internal error.")
  }
}

private lazy val trace = {
  (FindPropStrict(qname) ~
    (filter {
      case CallPropVoid(name, args) if name == qname => false
      case _ => true
    }*) ~
    partial {
      case CallPropVoid(name, args) if name == qname => CallPropVoid(name, args)
    }
  ) ^^ {
    case findProp ~ ops ~ callProp if ops.length == callProp.numArguments => {
      ops ::: (List.fill(callProp.numArguments) { Pop() })
    }
    case findProp ~ ops ~ callProp => findProp :: ops ::: List(callProp)
    case _ => error("Internal error.")
  }
}
```

`qname` defines the qualified name of the trace method. `traceVoid`searches for any `CallProperty()` that matches the qualified name of trace followed by a Pop(). Such calls can be converted to a `CallPropVoid()` and we get rid of a `Pop()` operation.

The second rule searches for FindPropStrict that matches the trace name, followed by any operation that is not calling trace and then the call to trace. This rule gets replaced with the operations that we ignored in the repetition followed by that many `Pop()` operations. If that match did not succeed for any reason we simply insert the trace back in. This could happen maybe if a trace is nested since the stripper tool can not remove nested traces.