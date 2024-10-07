## The Java Revolution: Best new Language Features since Java 8
- 👋 Hi! I'm https://victorrentea.ro
- Java Language - background and position today
- var [11]
- Verbosity and Project Lombok (https://projectlombok.org/) [8] 
- records ⭐️ [17]
- immutability: List|Map.of [11], .toList [17]
- text blocks """ [17] and interpolation \{var} [24]
- switch statement: problems -> clean switch rules
- switch expressions ⭐️ [17]
- return switch(enum) [17] over polymorphism with types or enums
- switch over sealed classes [21] over Visitor pattern
- Virtual threads - Project Loom & friends [21]

The features above are supported since Java LTS version [ver] (expected)

Java since 8 HATES checked exceptions; loves unchecked exceptions( aka RuntimeExceptions) 
- checked exceptions = you have to handle them or declare them in the method signature,
eg: IOException, SQLException, ClassNotFoundException
- runtime exceptions = you can handle them, but you don't have to, 
eg: NullPointerException, ArrayIndexOutOfBoundsException, ClassCastException
__________________________________________________________
Immutable collections is the way to go in Java 
- List.of(), Map.of(), Set.of() ,Map.ofEntries() 
- stream() allows us to derive a new collection from an existing 
ones without modifying the original one. 
But there is a trick if you need to modify an immutable collection, 
you can only do that by cloning it and adding or removing the elements you don't like.
if you do that in the loop you will have a performance issue.
for example:
List<String> list = immutableList
for(e:otherList){ // > 1000=> you will have a performance issue
     list = new ImmutableList(list,e)// malloc more and more memory in a loop
    myImmutable = new MyImmutable(myImmutable.x + 1,y,z)
}
__________________________________________________________________
AOP - Aspect Oriented Programming - come to resolve the cross-cutting concerns, mean the concerns that are spread across the application,
Does this cost-cutting concern is something that you want to do over and over and over again in many, many places.
If the answer is yes, then you should consider using AOP aka Proxy.

AOP is a programming paradigm that aims to increase modularity by allowing the separation of cross-cutting concerns.