package TreeRealization;

import java.util.ArrayList;
import java.util.function.Function;

public class SimpleTree<T> {

    protected class SimpleTreeNode<T> {
        public T value;
        public ArrayList<SimpleTreeNode<T>> children;

        public SimpleTreeNode(T value, ArrayList<SimpleTreeNode<T>> children) {
            this.value = value;
            this.children = children;
        }

        public SimpleTreeNode(T value) {
            this.value = value;
            this.children = new ArrayList<>();
        }

        public T getValue() {
            return value;
        }

        public ArrayList<SimpleTreeNode<T>> getChildren() {
            return children;
        }

        public void addChild(T value) {
            children.add(new SimpleTreeNode<>(value));
        }

        public void addChild(T value, int index) {
            children.add(index, new SimpleTreeNode<>(value));
        }

        public SimpleTreeNode<T> getChild(int index) {
            return children.get(index);
        }

        public int numberOfChildren() {
            return children.size();
        }

        private void reverseChildren() {
            ArrayList<SimpleTreeNode<T>> newList = new ArrayList<>();
            for (int i = children.size()-1; i>=0; i--){
                newList.add(children.get(i));
            }
            this.children=newList;
        }
    }

    protected SimpleTreeNode<T> root = null;

    protected Function<String, T> fromStrFunc;
    protected Function<T, String> toStrFunc;

    public SimpleTree(Function<String, T> fromStrFunc, Function<T, String> toStrFunc) {
        this.fromStrFunc = fromStrFunc;
        this.toStrFunc = toStrFunc;
    }

    public SimpleTree(Function<String, T> fromStrFunc) {
        this(fromStrFunc, x -> x.toString());
    }

    public SimpleTree() {
        this(null);
    }

    public SimpleTreeNode<T> getRoot() {
        return root;
    }

    public void clear() {
        root = null;
    }

    private T fromStr(String s) throws Exception {
        s = s.trim();
        if (s.length() > 0 && s.charAt(0) == '"') {
            s = s.substring(1);
        }
        if (s.length() > 0 && s.charAt(s.length() - 1) == '"') {
            s = s.substring(0, s.length() - 1);
        }
        if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации строки в T");
        }
        return fromStrFunc.apply(s);
    }

    private class IndexWrapper {
        public int index = 0;
    }

    private void skipSpaces(String bracketStr, IndexWrapper iw) {
        while (iw.index < bracketStr.length() && Character.isWhitespace(bracketStr.charAt(iw.index))) {
            iw.index++;
        }
    }

    private T readValue(String bracketStr, IndexWrapper iw) throws Exception {
        skipSpaces(bracketStr, iw);
        if (iw.index >= bracketStr.length()) {
            return null;
        }
        int from = iw.index;
        boolean quote = bracketStr.charAt(iw.index) == '"';
        if (quote) {
            iw.index++;
        }
        while (iw.index < bracketStr.length() && (
                quote && bracketStr.charAt(iw.index) != '"' ||
                        !quote && !Character.isWhitespace(bracketStr.charAt(iw.index)) && "(),".indexOf(bracketStr.charAt(iw.index)) < 0
        )) {
            iw.index++;
        }
        if (quote && bracketStr.charAt(iw.index) == '"') {
            iw.index++;
        }
        String valueStr = bracketStr.substring(from, iw.index);
        T value = fromStr(valueStr);
        skipSpaces(bracketStr, iw);
        return value;
    }

    private SimpleTreeNode<T> fromBracketStr(String bracketStr, IndexWrapper iw) throws Exception {
        T parentValue = readValue(bracketStr, iw);
        SimpleTreeNode<T> parentNode = new SimpleTreeNode<>(parentValue);
        if (bracketStr.charAt(iw.index) == '(') {
            iw.index++;
            skipSpaces(bracketStr, iw);
            while (bracketStr.charAt(iw.index) != ')') {
                if (bracketStr.charAt(iw.index) != ',') {
                    SimpleTreeNode<T> Node = fromBracketStr(bracketStr, iw);
                    parentNode.children.add(Node);
                    skipSpaces(bracketStr, iw);
                }
                if (bracketStr.charAt(iw.index) == ',') {
                    iw.index++;
                    skipSpaces(bracketStr, iw);
                }
                if (iw.index == bracketStr.length() - 1) {
                    throw new Exception(String.format("Ожидалось ')' [%d]", iw.index));
                }
                iw.index++;
            }
        }
        return parentNode;
    }

    public void fromBracketNotation(String bracketStr) throws Exception {
        IndexWrapper iw = new IndexWrapper();
        SimpleTreeNode<T> root = fromBracketStr(bracketStr, iw);
        if (iw.index < bracketStr.length()) {
            throw new Exception(String.format("Ожидался конец строки [%d]", iw.index));
        }
        this.root = root;
    }

    public void task() {
        reverseChildren(root);
    }

    private void reverseChildren(SimpleTreeNode<T> parentNode) {
        if(parentNode.numberOfChildren()!=0){
            for(int i = 0; i<parentNode.numberOfChildren(); i++){
                reverseChildren(parentNode.getChild(i));
            }
        } else {
            for(int i = 0; i<parentNode.numberOfChildren(); i++){
                reverseChildren(parentNode.getChild(i));
            }
            parentNode.reverseChildren();
        }
    }

    public String toString(){
        /*if (fromStrFunc == null) {
            throw new Exception("Не определена функция конвертации в строку");
        }*/
        StringBuilder str = new StringBuilder();
        str.append(toStrFunc.apply(root.value) + "(");
        return str.toString();
    }

    /*private String treeTraversal(SimpleTreeNode<T> parentNode){
        StringBuilder str = new StringBuilder();
        str.append(toStrFunc.apply(parentNode.value));
        if(parentNode.numberOfChildren()!=0){
            str.append('(');
            for(int i = 0; i<parentNode.numberOfChildren(); i++){
                treeTraversal(parentNode.getChild(i));
                str.append(',');
            }
            str.append(')');
        }
    }*/
}
