package task;

import file.TimeFormatter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, null, null);
        this.endTime = null;
    }

    public Epic(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.endTime = super.getEndTime();
    }

    public List<Integer> getSubtuskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public void clearSubtusks() {
        subtaskIds.clear();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void updateTimes(List<SubTask> subtasks) {
        if (subtasks == null || subtasks.isEmpty()) {
            this.startTime = null;
            this.duration = null;
            this.endTime = null;
            return;
        }

        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;
        Duration totalDuration = Duration.ZERO;

        for (SubTask subTask : subtasks) {
            if (subTask.getStartTime() != null) {
                if (earliestStart == null || subTask.getStartTime().isBefore(earliestStart)) {
                    earliestStart = subTask.getStartTime();
                }

                LocalDateTime subTaskEnd = subTask.getEndTime();
                if (latestEnd == null || subTaskEnd.isAfter(latestEnd)) {
                    latestEnd = subTaskEnd;
                }

                if (subTask.getDuration() != null) {
                    totalDuration = totalDuration.plus(subTask.getDuration());
                }
            }
        }

        this.startTime = earliestStart;
        this.duration = totalDuration;
        this.endTime = latestEnd;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtuskIds=" + subtaskIds +
                ", startTime='" + TimeFormatter.formatForOutput(startTime) +
                ", duration='" + (duration != null ? duration.toMinutes() + " минут" : "не указано") +
                ", endTime='" + TimeFormatter.formatForOutput(getEndTime()) +
                '}';
    }
}
