package TreeRealization;

public class Main {

    public static void main(String[] args) throws Exception {
        SimpleTree<String> tree = new SimpleTree<>(s -> s.toString());
        tree.fromBracketNotation("1(1(1,2),2(1,2))");
        tree.task();
        //System.out.println(tree.toStrFunc);
    }
}
