package file;

import task.Epic;
import task.SubTask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;


public class TaskCSVFormatHeader {

    public static String getHeader() {
        return "id,type,name,status,description,startTime,duration,epicId\n";
    }

    // Метод сохранения задачи в строку
    public static String toString(Task task) {
        String startTimeStr = task.getStartTime() != null ? TimeFormatter.formatForCsv(task.getStartTime()) : "";
        String durationStr = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";

        String[] toJoin = {Integer.toString(task.getId()), task.getType().toString(), task.getName(),
                task.getStatus().toString(), task.getDescription(), startTimeStr, durationStr, getParentEpicId(task)};
        return String.join(",", toJoin);

    }

    // Метод создания задачи из строки
    public static Task fromString(String value) {
        String[] params = value.split(",");
        LocalDateTime startTime = params[5].isEmpty() ? null : TimeFormatter.parseFromCsv(params[5]);
        Duration duration = params[6].isEmpty() ? null : Duration.ofMinutes(Long.parseLong(params[6]));

        if (params[1].equals("EPIC")) {
            Epic epic = new Epic(params[2], params[4], TaskStatus.valueOf(params[3].toUpperCase()), startTime, duration);
            epic.setId(Integer.parseInt(params[0]));
            epic.setStatus(TaskStatus.valueOf(params[3].toUpperCase()));
            return epic;
        } else if (params[1].equals("SUBTASK")) {
            SubTask subtask = new SubTask(params[2], params[4], TaskStatus.valueOf(params[3].toUpperCase()),
                    Integer.parseInt(params[7]), startTime, duration);
            subtask.setId(Integer.parseInt(params[0]));
            return subtask;
        } else {
            Task task = new Task(params[2], params[4], TaskStatus.valueOf(params[3].toUpperCase()), startTime, duration);
            task.setId(Integer.parseInt(params[0]));
            return task;
        }
    }

    private static String getParentEpicId(Task task) {
        return task instanceof SubTask ? Integer.toString(((SubTask) task).getEpicId()) : "";
    }
}
