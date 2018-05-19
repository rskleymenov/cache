package cache.tree;

import java.util.*;

public class RadixTree<V> implements Map<String, V> {

    private static final String KEY_MUST_BE_PRESENT = "KEY_MUST_BE_PRESENT";
    private static final String ONLY_STRINGS_ALLOWED = "ONLY_STRINGS_ALLOWED";
    private static final String ROOT_PREFIX = "";

    private RadixTreeNode<V> root;

    public RadixTree() {
        this.root = new RadixTreeNode<V>(ROOT_PREFIX);
    }

    private void visitNode(RadixTreeNode<V> node, String applicablePrefix, String prefix, RadixTreeFinder<V, ?> finder) {
        if (node.hasValue() && prefix.startsWith(applicablePrefix)) {
            finder.find(prefix, node.getValue());
        }

        for (RadixTreeNode<V> child : node) {
            int prefixLength = prefix.length();
            String newPrefix = prefix + child.getPrefix();
            if (applicablePrefix.length() <= prefixLength || newPrefix.length() <= prefixLength
                    || newPrefix.charAt(prefixLength) == applicablePrefix.charAt(prefixLength)) {
                visitNode(child, applicablePrefix, newPrefix, finder);
            }
        }
    }

    private void visitNode(RadixTreeFinder<V, ?> finder) {
        visitNode(root, ROOT_PREFIX, ROOT_PREFIX, finder);
    }

    private void visitNode(RadixTreeFinder<V, ?> finder, String applicablePrefix) {
        visitNode(root, applicablePrefix, ROOT_PREFIX, finder);
    }

    @Override
    public void clear() {
        Collection<RadixTreeNode<V>> children = root.getChildren();
        children.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        if (key == null) {
            throw new NullPointerException(KEY_MUST_BE_PRESENT);
        }

        if (!(key instanceof String)) {
            throw new ClassCastException(ONLY_STRINGS_ALLOWED);
        }
        RadixTreeFinder<V, Boolean> finder = new RadixTreeFinder<V, Boolean>() {
            boolean found = false;

            @Override
            public void find(String findKey, V value) {
                if (findKey.equals(key)) {
                    found = true;
                }
            }

            @Override
            public Boolean getResult() {
                return found;
            }
        };
        visitNode(finder, (String) key);
        return finder.getResult();
    }

    @Override
    public boolean containsValue(final Object value) {
        RadixTreeFinder<V, Boolean> finder = new RadixTreeFinder<V, Boolean>() {
            boolean found = false;

            @Override
            public void find(String key, V val) {
                if (value == val || (val != null && val.equals(value))) {
                    found = true;
                }
            }

            @Override
            public Boolean getResult() {
                return found;
            }
        };
        visitNode(finder);
        return finder.getResult();
    }

    @Override
    public V get(final Object keyToCheck) {
        if (keyToCheck == null) {
            throw new NullPointerException(KEY_MUST_BE_PRESENT);
        }

        if (!(keyToCheck instanceof String)) {
            throw new ClassCastException(ONLY_STRINGS_ALLOWED);
        }
        RadixTreeFinder<V, V> finder = new RadixTreeFinder<V, V>() {
            V result = null;

            @Override
            public void find(String key, V value) {
                if (key.equals(keyToCheck)) {
                    result = value;
                }
            }

            @Override
            public V getResult() {
                return result;
            }
        };
        visitNode(finder, (String) keyToCheck);
        return finder.getResult();
    }

    @Override
    public boolean isEmpty() {
        Collection<RadixTreeNode<V>> children = root.getChildren();
        return children.isEmpty();
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> map) {
        for (Entry<? extends String, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public int size() {
        RadixTreeFinder<V, Integer> finder = new RadixTreeFinder<V, Integer>() {
            int count = 0;

            @Override
            public void find(String key, V value) {
                ++count;
            }

            @Override
            public Integer getResult() {
                return count;
            }
        };
        visitNode(finder);
        return finder.getResult();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        RadixTreeFinder<V, Set<Entry<String, V>>> finder = new RadixTreeFinder<V, Set<Entry<String, V>>>() {
            Set<Entry<String, V>> result = new HashSet<Entry<String, V>>();

            @Override
            public void find(String key, V value) {
                result.add(new AbstractMap.SimpleEntry<String, V>(key, value));
            }

            @Override
            public Set<Entry<String, V>> getResult() {
                return result;
            }
        };
        visitNode(finder);
        return finder.getResult();
    }

    @Override
    public Set<String> keySet() {
        RadixTreeFinder<V, Set<String>> finder = new RadixTreeFinder<V, Set<String>>() {
            Set<String> result = new TreeSet<String>();

            @Override
            public void find(String key, V value) {
                result.add(key);
            }

            @Override
            public Set<String> getResult() {
                return result;
            }
        };
        visitNode(finder);
        return finder.getResult();
    }

    @Override
    public Collection<V> values() {
        RadixTreeFinder<V, Collection<V>> finder = new RadixTreeFinder<V, Collection<V>>() {
            Collection<V> result = new ArrayList<V>();

            @Override
            public void find(String key, V value) {
                result.add(value);
            }

            @Override
            public Collection<V> getResult() {
                return result;
            }
        };
        visitNode(finder);
        return finder.getResult();
    }

    @Override
    public V put(String key, V value) {
        if (key == null) {
            throw new NullPointerException(KEY_MUST_BE_PRESENT);
        }
        return put(key, value, root);
    }

    private V put(String key, V value, RadixTreeNode<V> node) {
        V resultValue = null;
        int largestPrefix = RadixTreePrefixHelper.getMaxPrefixLength(key, node.getPrefix());
        if (largestPrefix == node.getPrefix().length() && largestPrefix == key.length()) {
            // full match node
            resultValue = node.getValue();
            node.setValue(value);
            node.setHasValue(true);
        } else if (largestPrefix == 0 || (largestPrefix < key.length() && largestPrefix >= node.getPrefix().length())) {
            // case when passed key is bigger than the prefix in the current node, if there child that can share
            // prefix node will be splitted, otherwise just add a new node to current note
            boolean found = false;
            String leftKey = key.substring(largestPrefix);
            for (RadixTreeNode<V> child : node) {
                if (child.getPrefix().charAt(0) == leftKey.charAt(0)) {
                    found = true;
                    resultValue = put(leftKey, value, child);
                    break;
                }
            }
            if (!found) {
                // child is not found, add a new one
                RadixTreeNode<V> newChild = new RadixTreeNode<V>(leftKey, value);
                node.getChildren().add(newChild);
            }
        } else if (largestPrefix < node.getPrefix().length()) {
            // split node
            String leftoverPrefix = node.getPrefix().substring(largestPrefix);
            RadixTreeNode<V> n = new RadixTreeNode<V>(leftoverPrefix, node.getValue());
            n.setHasValue(node.hasValue());
            n.getChildren().addAll(node.getChildren());

            node.setPrefix(node.getPrefix().substring(0, largestPrefix));
            node.getChildren().clear();
            node.getChildren().add(n);
            if (largestPrefix == key.length()) {
                resultValue = node.getValue();
                node.setValue(value);
                node.setHasValue(true);
            } else {
                // There's a leftKey suffix on the key, so add another child
                String leftKey = key.substring(largestPrefix);
                RadixTreeNode<V> keyNode = new RadixTreeNode<V>(leftKey, value);
                node.getChildren().add(keyNode);
                node.setHasValue(false);
            }
        } else {
            String leftoverKey = key.substring(largestPrefix);
            RadixTreeNode<V> n = new RadixTreeNode<V>(leftoverKey, value);
            node.getChildren().add(n);
        }
        return resultValue;
    }

    @Override
    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException(KEY_MUST_BE_PRESENT);
        }
        if (!(key instanceof String)) {
            throw new ClassCastException(ONLY_STRINGS_ALLOWED);
        }
        String sKey = (String) key;
        if (sKey.equals(ROOT_PREFIX)) {
            V value = root.getValue();
            root.setHasValue(false);
            return value;
        }
        return remove(sKey, root);
    }

    private V remove(String key, RadixTreeNode<V> node) {
        V resultValue = null;
        Iterator<RadixTreeNode<V>> iter = node.getChildren().iterator();
        while (iter.hasNext()) {
            RadixTreeNode<V> child = iter.next();
            int largestPrefix = RadixTreePrefixHelper.getMaxPrefixLength(key, child.getPrefix());
            if (largestPrefix == child.getPrefix().length() && largestPrefix == key.length()) {
                if (child.getChildren().isEmpty()) {
                    resultValue = child.getValue();
                    iter.remove();
                    break;
                } else if (child.hasValue()) {
                    resultValue = child.getValue();
                    child.setHasValue(false);
                    if (child.getChildren().size() == 1) {
                        RadixTreeNode<V> subchild = child.getChildren().iterator().next();
                        String newPrefix = child.getPrefix() + subchild.getPrefix();
                        // Merge child node
                        child.setValue(subchild.getValue());
                        child.setHasValue(subchild.hasValue());
                        child.setPrefix(newPrefix);
                        child.getChildren().clear();
                    }
                    break;
                }
            } else if (largestPrefix > 0 && largestPrefix < key.length()) {
                String leftoverKey = key.substring(largestPrefix);
                resultValue = remove(leftoverKey, child);
                break;
            }
        }
        return resultValue;
    }
}
