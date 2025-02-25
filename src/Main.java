import TaskManagers.HistoryManager;
import TaskManagers.Managers;
import TaskManagers.TaskManager;
import task.Task;
import TaskManagers.InMemoryTaskManager;
import task.TaskStatus;
import task.Epic;
import task.SubTask;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefaultTaskManager();
       // createTasks(manager);
       // printAllTasks(manager);



        Epic epic1 = new Epic("epic1", "epic1 description");
        manager.createEpic(epic1);
        SubTask subtask3 = new SubTask("subtask1", "subtask1 description",
                epic1.getId());
        manager.createSubtusk(subtask3);
        System.out.println(manager.findSubtaskByID(subtask3.getId()));
        subtask3.setName("subtask2");
        subtask3.setDescription("subtask2 description");
        subtask3.setStatus(TaskStatus.IN_PROGRESS);
        System.out.println(manager.updateSubtask(subtask3));
        System.out.println(manager.getHistory());
    }

    private static void createTasks(TaskManager manager) {
            Task writeCode = new Task("Написать программу", "На JAVA");
            manager.createTask(writeCode);


            Task Review = new Task("Отправить на ревью", "Выгрузить код на GitHub");
            manager.createTask(Review);


            Epic codeStructure = new Epic("Определить структуру кода", "Понять задачи");
             manager.createEpic(codeStructure);


            SubTask mainTusk = new SubTask("Выделить основные задачи", "Прочитать ТЗ несколько раз", codeStructure.getId());
            manager.createSubtusk(mainTusk);


            SubTask createClass = new SubTask("Создать классы", "Создать классы опираясь на ТЗ", codeStructure.getId());
            manager.createSubtusk(createClass);


            Epic continueCode = new Epic("Наполнить код", "Дописать все необходимое для работы программы");
            manager.createEpic(continueCode);


            SubTask createMetods = new SubTask("Написать конструкторы и методы", "Опираясь на ТЗ написать конструкторы и методы для корректной работы программы", continueCode.getId());
            manager.createSubtusk(createMetods);
        }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

}
