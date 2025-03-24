package file;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;


public class TaskCSVFormatHeader {

    public static String getHeader() {
        return "id,type,name,status,description,epic\n";
    }

    // Метод сохранения задачи в строку
    public static String toString(Task task) {
        String[] toJoin = {Integer.toString(task.getId()), task.getType().toString(), task.getName(),
                task.getStatus().toString(), task.getDescription(), getParentEpicId(task)};
        return String.join(",", toJoin);

    }

    // Метод создания задачи из строки
    public static Task fromString(String value) {
        String[] params = value.split(",");
        if (params[1].equals("EPIC")) {
            Epic epic = new Epic(params[2], params[4], TaskStatus.valueOf(params[3].toUpperCase()));
            epic.setId(Integer.parseInt(params[0]));
            epic.setStatus(TaskStatus.valueOf(params[3].toUpperCase()));
            return epic;
        } else if (params[1].equals("SUBTASK")) {
            SubTask subtask = new SubTask(params[2], params[4], TaskStatus.valueOf(params[3].toUpperCase()),
                    Integer.parseInt(params[5]));
            subtask.setId(Integer.parseInt(params[0]));
            return subtask;
        } else {
            Task task = new Task(params[2], params[4], TaskStatus.valueOf(params[3].toUpperCase()));
            task.setId(Integer.parseInt(params[0]));
            return task;
        }
    }

    private static String getParentEpicId(Task task) {
        if (task instanceof SubTask) {
            return Integer.toString(((SubTask) task).getEpicId());
        }
        return "";
    }
}
