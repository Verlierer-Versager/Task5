package TreeRealization;

public class Main {

    public static void main(String[] args) throws Exception {
        SimpleTree<Integer> tree = new SimpleTree<>(s -> Integer.parseInt(s), s -> s.toString());
        tree.fromBracketNotation("1(1(1(1, 2),2,3), 2(1,2), 3(1, 2, 3, 4))");
        tree.task();
        System.out.println(tree.toBracketNotation());
    }
}
