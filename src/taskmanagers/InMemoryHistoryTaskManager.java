package taskmanagers;

import task.Task;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;


public class InMemoryHistoryTaskManager implements HistoryManager {

    /*
     *Класс CutomList и работа со списком для истории поиска
     */

    private static class CutomList {

        private final HashMap<Integer, Node> nodeMap = new HashMap<>();
        private Node head;
        private Node tail;

        private void linkLast(Task task) {
            Node node = new Node();
            node.setTask(task);

            if (nodeMap.containsKey(task.getId())) {
                removeNode(nodeMap.get(task.getId()));
            }

            if (head == null) {
                tail = node;
                head = node;
                node.setNext(null);
                node.setPrev(null);
            } else {
                node.setPrev(tail);
                node.setNext(null);
                tail.setNext(node);
                tail = node;
            }

            nodeMap.put(task.getId(), node);
        }

        private List<Task> getTasks() {
            return nodes()
                    .map(Node::getTask)
                    .collect(Collectors.toList());
        }

        private Stream<Node> nodes() {
            return Stream.iterate(head, Objects::nonNull, Node::getNext);
        }

        private void removeNode(Node node) {
            if (node != null) {
                nodeMap.remove(node.getTask().getId());
                Node prev = node.getPrev();
                Node next = node.getNext();

                if (head == node) {
                    head = node.getNext();
                }
                if (tail == node) {
                    tail = node.getPrev();
                }

                if (prev != null) {
                    prev.setNext(next);
                }

                if (next != null) {
                    next.setPrev(prev);
                }
            }
        }

        private Node getNode(int id) {
            return nodeMap.get(id);
        }
    }

    /*
     *Экземпляр класса CutomList
     */
    private final CutomList cutomList = new CutomList();

    /*
     *Метод добавления задачи после ее поиска
     */
    @Override
    public void addTask(Task task) {
        cutomList.linkLast(task);
    }

    /*
     *Метод удаления дубликата задачи
     */
    @Override
    public void remove(int id) {
        cutomList.removeNode(cutomList.getNode(id));
    }

    /*
     *Вызов истории поиска
     */
    @Override
    public List<Task> getHistory() {
        return cutomList.getTasks();

    }

    /*
     *Класс Node(узел), содержит в себе ссылки на следующий и предыдущий элементы,
     * а также само значение(task)
     */

    public static class Node {

        private Task task;
        private Node next;
        private Node prev;

        public Task getTask() {
            return task;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}
