import java.util.Scanner;

public class Subtask extends Task {

    public Subtask(String name, String description, int id, String status) {
        super(name, description, id, status);
    }

    public static Object createSubtask(int id, String status) {
        Scanner subScanner = new Scanner(System.in);
        System.out.println("Введите название подзадачи");
        String name = subScanner.next();
        System.out.println("Введите описание подзадачи");
        String description = subScanner.next();

        Subtask subtask = new Subtask(name, description, id, status);

        System.out.println("Подзадача добавлена!");
        return subtask;
    }


}
