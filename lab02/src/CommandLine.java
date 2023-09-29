import java.io.File;
import java.util.Scanner;

public class CommandLine {

    private String currentPath = new File("").getAbsolutePath();


    private void dir(String pathname){
        File path = new File(pathname);


        if (!path.exists() || !path.isDirectory()) return;

        File[] fileList = path.listFiles();

        if (fileList == null) return;

        for (File f : fileList){
            if (f.isDirectory()){
                System.out.printf("Directory: %s\n", f.getName());
            } else {
                System.out.printf("File: %s  Size: %s\n", f.getName(), f.length());
            }
        }
    }

    private void md(String pathname){
        File path = new File(pathname);
        path.mkdirs();
    }

    private void del(String pathname){
        File path = new File(pathname);

        if (path.isDirectory()){
            System.out.println("To delete a directory, you should use RD command.");
            return;
        }

        boolean success = path.delete();

        if (!success){
            System.out.println("Deletion failed: this file is in use by another application or process.");
        }
    }

    private void rd(String pathname){
        File path = new File(pathname);

        if (!path.isDirectory()){
            System.out.println("To delete a file, you should use DEL command.");
            return;
        }

        if (path.list().length > 0){
            System.out.printf("The directory %s is not empty!\n", pathname);
            return;
        }

        path.delete();

    }

    private void exit(){
        System.exit(0);
    }

    private void processInput(String[] input){
        if (input[0].isEmpty()){
            System.out.print(currentPath + "> ");
            return;
        }

        try {

            switch (input[0]) {
                case "dir":
                    dir(input[1]);
                    break;
                case "md":
                    md(input[1]);
                    break;
                case "rd":
                    rd(input[1]);
                    break;
                case "del":
                    del(input[1]);
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    System.out.printf("%s is not a recognised command\n", input[0]);
            }

        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Your input is incomplete");
        }

        System.out.print(currentPath + "> ");
    }

    public static void main(String[] args){
        CommandLine commandLine = new CommandLine();
        Scanner scanner = new Scanner(System.in);

        System.out.print(commandLine.currentPath + "> ");
        while (true){
            String[] input = scanner.nextLine().split("\\s+");
            commandLine.processInput(input);
        }
    }
}