package cache.tree;

class RadixTreePrefixHelper {
    static int getMaxPrefixLength(CharSequence a, CharSequence b) {
        int prefixLength = 0;
        int minimumWordsLength = Math.min(a.length(), b.length());
        for (int i = 0; i < minimumWordsLength; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return prefixLength;
            }
            prefixLength++;
        }
        return prefixLength;
    }
}
