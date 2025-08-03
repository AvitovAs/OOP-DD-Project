package view;

public class Program {

    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Error: No level file specified");
            System.exit(0);
        }

        GameRunner runner = new GameRunner();
        runner.initialize(args[0]);
        runner.start();
    }

}
