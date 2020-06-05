package TreeRealization;

public class Main {

    public static void main(String[] args) throws Exception {
        SimpleTree<Integer> tree = new SimpleTree<>(s -> Integer.parseInt(s));
        tree.fromBracketNotation("1(1(1,2),2(1,2))");
        //tree.task();
        //System.out.println(tree.toStrFunc);
    }
}
