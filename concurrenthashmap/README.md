# HashTable, HashMap, ConcurrentHashMap
HashTable, HashMap, ConcurrentHashMap 의 차이점 위주로 알아봅니다.  
좀 더 자세한 설명은 또 따로 포스팅할게요.

## HashTable, HashMap, ConcurrentHashMap 의 자료구조

- HashTable, HashMap, ConcurrentHashMap 은 키를 값에 매핑할 수 있는 구조인 (Key, Value) 형태로 데이터를 저장하는 자료구조이다.
- HashTable, HashMap, ConcurrentHashMap 은 모두 자바 컬렉션 프레임워크인 **Map 인터페이스의 구현 객체다.**

<br>
<br>

## HashTable, HashMap, ConcurrentHashMap 의 차이점

## HashTable

```java
public synchronized V get(Object key) {
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    for (Entry<?,?> e = tab[index] ; e != null ; e = e.next) {
        if ((e.hash == hash) && e.key.equals(key)) {
            return (V)e.value;
        }
    }
    return null;
}
```

해쉬테이블은 `synchronized` 키워드가 선언 되어 있다. (동기화를 할 수 있다.)  
여러 쓰레드를 사용하는, 즉 멀티쓰레드 환경에서는 **Thread Safe**를 고려한 HashTable을 사용한다.  
해쉬테이블은 `null` 을 허용하지 않는다.

```java
import java.util.Hashtable;

public class HashTableEx {
    public static void main(String[] args) {
        Hashtable<String, String> hashtable = new Hashtable<>();

        hashtable.put("one", "hoho");
        hashtable.put(null, "haha"); //error 발생!
        hashtable.put("two", null); //error 발생!

        System.out.println(hashtable.get("one"));
        System.out.println(hashtable.get(null));
        System.out.println(hashtable.get("two"));
    }
}
```

## HashMap

해쉬맵은 `synchronized` 키워드가 선언되어 있지 않아서 **HashTable보다 Thread Safe 하지 않다**  
그래서 **동시성 문제**를 발생시킬 수 있기 때문에 이를 고려해야 한다.  
해쉬맵은 `null`이 허용된다.

```java
import java.util.HashMap;

public class HashMapEx {
    public static void main(String[] args) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();

        hashMap.put(1, 2);
        hashMap.put(null, 3);
        hashMap.put(2, null);

        System.out.println(hashMap.get(1));
        System.out.println(hashMap.get(null));
        System.out.println(hashMap.get(2));
    }
}
```

## ConcurrentHashMap

```java
public V put(K key, V value) {
    return putVal(key, value, false);
}

/** Implementation for put and putIfAbsent */
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh; K fk; V fv;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value)))
                break;                   // no lock when adding to empty bin
        }
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else if (onlyIfAbsent // check first node without acquiring lock
                 && fh == hash
                 && ((fk = f.key) == key || (fk != null && key.equals(fk)))
                 && (fv = f.val) != null)
            return fv;
        else {
            V oldVal = null;
            synchronized (f) {
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {
                        binCount = 1;
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key, value);
                                break;
                            }
                        }
                    }
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                       value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                    else if (f instanceof ReservationNode)
                        throw new IllegalStateException("Recursive update");
                }
            }
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}
```

위 코드를 자세히 보면 `synchronized` 키워드가 있다. 즉, HashMap의 동시성 문제를 해결해준다.  
물론 `null`도 허용된다.

```java
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapEx {
    public static void main(String[] args) {
        ConcurrentHashMap<Long, String> concurrentHashMap = new ConcurrentHashMap<>();

        concurrentHashMap.put(1L, "HI");
        concurrentHashMap.put(null, "null!!");
        concurrentHashMap.put(2L, null);

        System.out.println(concurrentHashMap.get(1L));
        System.out.println(concurrentHashMap.get(null));
        System.out.println(concurrentHashMap.get(2L));
    }
}
```

<br>
<br>

## 정리

Java에서 HashTable은 권장하지 않는다. 그래서 되도록이면 HashMap을 사용하자!  
HashMap의 동시성 문제를 해결하려면 ConcurrentHashMap을 사용하자!