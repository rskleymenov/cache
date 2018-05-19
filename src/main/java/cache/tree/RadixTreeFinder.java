
package cache.tree;

public interface RadixTreeFinder<VALUE, RESULT> {
    void find(String key, VALUE value);

    RESULT getResult();
}
