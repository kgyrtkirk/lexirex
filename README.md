
# lexirex 
## lexicographical regexp match generator

this small lib can be used to create a regexp for an arbitrary range:

```java
		Pattern p = RegexRangeBuilder
				.fromExclusive("bar")
				.toInclusive("foo")
				.toPattern();
```

for the generate pattern by the above code:
- match: `bar1`,`cat`,`cairo`,`foo`
- not match: `bar`,`a`,`zillion` 

### why is it useful?

I will use it to create regexps which will allow me to use a maven build system to test the code using multiple machines.


license: MIT 
