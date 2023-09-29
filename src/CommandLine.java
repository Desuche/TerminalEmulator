import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class CommandLine {

    private String currentPath;
    private final Scanner scanner;

    public CommandLine() throws IOException {
        this.currentPath = new File("").getCanonicalPath();
        this.scanner = new Scanner(System.in);
    }

    public void run() throws IOException {
        printIntro();
        while (true){
            String[] input = scanner.nextLine().split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            processInput(input);
        }
    }

    private void printIntro(){
        System.out.println("Command Line Emulator");
        System.out.println("Type 'help' for a list of available commands.");
        System.out.println();
        System.out.print(currentPath + "> ");
    }

    private void processInput(String[] input) throws IOException{
        if (input[0].isEmpty()){
            System.out.print(currentPath + "> ");
            return;
        }

        try {

            switch (input[0]) {
                case "dir":
                    dir(resolvePath(input[1]));
                    break;
                case "md":
                    md(resolvePath(input[1]));
                    break;
                case "rd":
                    rd(resolvePath(input[1]));
                    break;
                case "del":
                    del(resolvePath(input[1]));
                    break;
                case "cd":
                    cd(resolvePath(input[1]));
                    break;
                case "help":
                    help();
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

    private String resolvePath(String pathname) throws IOException {
        String resolvedPath;
        int stringLength = pathname.length();

        if (stringLength > 0 && pathname.charAt(0) == '"'){

            if (pathname.charAt(stringLength - 1) == '"'){
                pathname = pathname.substring(1, stringLength - 1);

            } else {
                pathname = pathname.substring(1, stringLength);
            }

        }

        if (stringLength > 0 && pathname.charAt(0) == '~'){
            pathname = pathname.substring(1, stringLength);
            String root = Paths.get(currentPath).getRoot().toString();
            resolvedPath = ( new File(Paths.get(root, pathname).toAbsolutePath().toString()) ).getCanonicalPath();
            return resolvedPath;
        }

        resolvedPath = ( new File(Paths.get(currentPath, pathname).toAbsolutePath().toString()) ).getCanonicalPath();
        return resolvedPath;
    }

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

    private void cd(String pathname) throws IOException {
        File path = new File(pathname);
        if (!path.exists() || !path.isDirectory()){
            System.out.printf("Unable to switch to path: %s\n",pathname);
            return;
        }
        this.currentPath = pathname;
    }

    private void help(){
        String[] messages = {
                "Hello there ~",
                "Here are the available commands",
                "    dir <path> : list out a directory's contents",
                "    md <path to new directory> : make a single or multilevel directory",
                "    cd <path> : Switch to a new directory",
                "    del <path to file> : delete a FILE",
                "    rd <path to empty directory> : remove an EMPTY DIRECTORY",
                "    exit : quit this program",
                "",
                "When working with paths:",
                "- relative paths are accepted",
                "- use '~' at the start of a path to signify the root directory",
                "- if your paths contain spaces, wrap them in double quotation marks"
        };

        System.out.println();
        for (String m : messages){
            System.out.println(m);
        }
        System.out.println();
    }

    private void exit(){
        System.out.println();
        System.out.println("Thank you for using this tool");
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        CommandLine commandLine = new CommandLine();
        commandLine.run();

    }
}