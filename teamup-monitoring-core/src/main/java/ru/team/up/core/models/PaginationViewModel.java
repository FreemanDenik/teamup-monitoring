package ru.team.up.core.models;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.LinkedList;
import java.util.List;

/**
 * Класс для формирования пагинации, принимает в конструктор {@link Page}<br>
 * Получаем целевой тип данных когда к запросу db добавляем {@link PageRequest} <br>
 * Пример: <i>PageRequest.of(pageNumber, pageSize)</i>
 *
 * @author Деник
 * @version 1.1
 */
public class PaginationViewModel {
    /**
     * Переключатель, включена пагинация или нет
     */
    public boolean enabled = false;
    /**
     * Количество всех разбитых на пагинацию страниц
     */
    public int totalPages;
    /**
     * Коллекция номеров страниц
     */
    public List<Link> links = new LinkedList<>();
    /**
     * Номер страницы
     */
    public int pageNumber;
    /**
     * Количество записей на одной странице
     */
    public int pageSize;

    /**
     * Конструктор принимает {@link Page} <br>
     * Он уже имеет в себе начальные настройки низкоуровневой пагинации такие как pageNumber, pageSize и т.д.
     *
     * @param pageable класс org.springframework.data.domain.Page
     */
    public PaginationViewModel(Page pageable) {
        this.totalPages = pageable.getTotalPages();
        // Проверка, если количество страниц с записями <= 1, то пагинация отключается
        if (totalPages > 1)
            this.enabled = true;
        else return;

        // Нумерация страниц начинается с 0, для красоты добавляем +1 чтобы в view'шке был не 0, а 1
        this.pageNumber = pageable.getNumber() + 1;
        // Получаем количество записей одной страницы
        this.pageSize = pageable.getSize();
        // Запись первой страницы с многоточием при условии, что текущая страницы не соседняя
        if (pageNumber > 3) {
            links.add(new Link(1));
            links.add(new Link());
        }
        // Запись второй страницы, при условии, что текущая страница 3
        if (pageNumber == 3) {
            links.add(new Link(pageNumber - 2));
        }
        // Запись третей страницы
        if (pageNumber > 1) {
            links.add(new Link(pageNumber - 1));
        }
        // Запись текущей страницы
        links.add(new Link(pageNumber, true));
        // Запись третей страницы с конца
        if (pageNumber < totalPages) {
            links.add(new Link(pageNumber + 1));
        }
        // Запись второй страницы с конца при условии, что текущая страница с конца 2
        if (pageNumber == totalPages - 2) {
            links.add(new Link(pageNumber + 2));
        }
        // Запись последней страницы с многоточием при условии, что текущая страницы не соседняя
        if (pageNumber < totalPages - 2) {
            links.add(new Link());
            links.add(new Link(totalPages));
        }

    }

    /**
     * Класс Link является вложенным в класс {@link PaginationViewModel} <br>
     * И используется в этом же классе как коллекция номеров страниц
     */
    public class Link {
        /**
         * Номер страницы, Object потому что кроме номера еще передается и строка: <code>...</code>
         */
        public Object pageNumber;
        /**
         * текущая страница или нет,
         */
        public String isActive;
        /**
         * Рабочая ссылка или нет ("нет" для ссылок ...)
         */
        public String isDisable;

        public Link() {
            this("...", false, true);
        }

        public Link(Object pageNumber) {
            this(pageNumber, false, false);
        }

        public Link(Object pageNumber, boolean isActive) {
            this(pageNumber, isActive, false);
        }

        public Link(Object pageNumber, boolean isActive, boolean isDisable) {
            this.pageNumber = pageNumber;
            // Помещаем строчное значение 'active', используется как класс на стороне клиента, для выделения текущего номера страницы
            this.isActive = isActive ? "active" : "";
            // Помещаем строчное значение 'pointer-events: none;', которое помещается в 'style' тега 'a' и отключает работу ссылки '<a href=""></a>'
            this.isDisable = isDisable ? "pointer-events: none;" : "";
        }
    }
}
