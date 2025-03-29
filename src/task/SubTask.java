package task;

import file.TimeFormatter;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;


    public SubTask(String name, String description, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, TaskStatus status, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }


    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", epicId=" + epicId +
                ", startTime='" + TimeFormatter.formatForOutput(startTime) +
                ", duration='" + (duration != null ? duration.toMinutes() + " минут" : "не указано") +
                ", endTime='" + TimeFormatter.formatForOutput(getEndTime()) +
                '}';
    }
}
