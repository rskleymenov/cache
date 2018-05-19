package cache.tree;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

class RadixTreeNode<V> implements Iterable<RadixTreeNode<V>>, Comparable<RadixTreeNode<V>> {
    private String prefix;
    private V value;
    private boolean hasValue;
    private Collection<RadixTreeNode<V>> children;

    RadixTreeNode(String prefix) {
        this(prefix, null);
        this.hasValue = false;
    }

    RadixTreeNode(String prefix, V value) {
        this.prefix = prefix;
        this.value = value;
        this.hasValue = true;
    }

    V getValue() {
        return value;
    }

    void setValue(V value) {
        this.value = value;
    }

    String getPrefix() {
        return prefix;
    }

    void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    Collection<RadixTreeNode<V>> getChildren() {
        if (children == null)
            children = new TreeSet<RadixTreeNode<V>>();
        return children;
    }

    boolean hasValue() {
        return hasValue;
    }

    void setHasValue(boolean hasValue) {
        this.hasValue = hasValue;
        if (!hasValue)
            this.value = null;
    }

    @Override
    public Iterator<RadixTreeNode<V>> iterator() {
        if (children == null) {
            return new Iterator<RadixTreeNode<V>>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public RadixTreeNode<V> next() {
                    return null;
                }

                @Override
                public void remove() {
                }
            };
        }

        return children.iterator();
    }

    @Override
    public int compareTo(RadixTreeNode<V> node) {
        return prefix.compareTo(node.getPrefix());
    }
}
