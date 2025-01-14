import TaskManager.Task;
import TaskManager.TaskManager;
import TaskManager.TaskStatus;
import TaskManager.Epic;
import TaskManager.SubTask;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        Task writeCode = new Task("Написать программу", "На JAVA");
        writeCode = manager.createTask(writeCode);
        System.out.println("__________");
        System.out.println("Задача 'writeCode' была создана");
        System.out.println(writeCode);

        Task Review = new Task("Отправить на ревью", "Выгрузить код на GitHub");
        Review = manager.createTask(Review);
        System.out.println("Задача 'Review' была создана");
        System.out.println(Review);

        System.out.println("__________");

        Epic codeStructure = new Epic("Определить структуру кода", "Понять задачи");
        codeStructure = manager.createEpic(codeStructure);
        System.out.println("Список подзадач 'codeStructure' был создан");
        System.out.println(codeStructure);

        SubTask mainTusk = new SubTask("Выделить основные задачи", "Прочитать ТЗ несколько раз", codeStructure.getId());
        mainTusk = manager.createSubtusk(mainTusk);
        System.out.println("Подзадача 'mainTusk' была создана");
        System.out.println(mainTusk);

        SubTask createClass = new SubTask("Создать классы", "Создать классы опираясь на ТЗ", codeStructure.getId());
        createClass = manager.createSubtusk(createClass);
        System.out.println("Подзадача 'createClass' была создана");
        System.out.println(createClass);

        System.out.println("__________");

        Epic continueCode = new Epic("Наполнить код", "Дописать все необходимое для работы программы");
        continueCode = manager.createEpic(continueCode);
        System.out.println("Список подзадач 'continueCode' был создан");
        System.out.println(continueCode);

        SubTask createMetods = new SubTask("Написать конструкторы и методы", "Опираясь на ТЗ написать конструкторы и методы для корректной работы программы", continueCode.getId());
        createMetods = manager.createSubtusk(createMetods);
        System.out.println("Подзадача 'createMetods' была создана");
        System.out.println(createMetods);

        System.out.println("__________");
    }
}
