import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

void main() { // stand-alone 'main' v21
//  List<Integer> numbers = Arrays.asList(1, 2, 3); // java 8
//  And cloning means memory allocation, which means CPU to garbage collect, which means it's called memory churning
//  Keep allocating the allocating because the old list is going to have to be destroyed. If I want to add one more element, I'm going to have to clone it again.
  List<Integer> numbers = List.of(1, 2, 3); // java 11 cannot change the list
  //numbers.set(0, 77); // you can change the elements of the list when using Arrays.asList(1, 2, 3)
//  numbers.add(4); // you cannot add or remove elements from the list
  //numbers.remove(1); // v21 - you cannot remove elements from the list
  System.out.println(numbers);

  var odds = numbers.stream()
      .filter(i -> i % 2 == 1)
      //.collect(toList());//java 8
              .toList(); // java 17 - you cannot change the list, immutable list
//  odds.remove(0); // it works with collect(toList())
  odds.remove(0); // it fails on Stream#toList()
  //odds.removeFirst(); // v21
  System.out.println(odds);
}