package ru.devguru.springms.planner.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devguru.springms.planner.todo.search.CategorySearchValues;
import ru.devguru.springms.planner.todo.service.CategoryService;
import ru.devguru.springms.planner.entity.Category;
import ru.devguru.springms.planner.utils.webclient.UserWebClientBuilder;

import java.util.List;
import java.util.Optional;

/**
 * КОНТРОЛЛЕР, ЭТО СЛОЙ, НА КОТОРОГО ПРИХОДИТ ЗАПРОС ОТ КЛИЕНТА, А САМ КОНТРОЛЛЕР ОБРАЩАЕТСЯ В СЕРВИС
 */

@RestController
@RequestMapping("/category") // базовый URI
@RequiredArgsConstructor
public class CategoryController {
    // Через DI нужно создать ссылку на сервис - доступ к БД
    private final CategoryService categoryService;
    private final UserWebClientBuilder userWebClientBuilder; // как для синхронного так и для асинхронного вызова мс

    /**
     * Метод POST
     */

    @PostMapping("/all")
    // POST - НЕиденпотентный, то есть повторный запрос меняет состояние сервера. (Повторный тот же запрос в банк спишет повторно деньги)
    public List<Category> findAll(@RequestBody Long id) { //  в параметры email также передается в формате json, указывается аннотация для считывания этого файла
        return categoryService.findAll(id);
    }

    /**
     * Добавление категории методом POST
     */
    @PostMapping("/add") //https://www.guru99.com/put-vs-post.html
    public ResponseEntity<Category> add(@RequestBody Category category) {
        // ResponseEntity - специальный объект, содержащий статус ответа(ок, ошибка и т.д., и объекты, указанные с помощью женерикс(как в данном случае))

        // проверка на обязательные параметры
        if (category.getId() != null && category.getId() != 0) { // Значит такая категория уже существует в БД
            // id создается автоматически в БД (autoincrement), поэтому его передавать не нужно

            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение title
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }

        // так как БД разделены, foreign key на user нет, то может случиться добавление записи(задачи, категории) для несуществующего user
        // проверка на наличие user: через WebClient

        if (userWebClientBuilder.userExistSync(category.getUserId())) {
            return ResponseEntity.ok(categoryService.add(category));
        }
        return new ResponseEntity("user id = " + category.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }


    /**
     * Обновление категории методом PUT
     */
    @PutMapping("/update") // Метод идемпотентный - повторная отправка запроса не влияет на сервер
    public ResponseEntity update(@RequestBody Category category) { // Будет возвращать только статус, а не объект entity, как в POST
        // проверка на обязательные параметры
        if (category.getId() == null && category.getId() == 0) { // Значит такой категории нет в БД для его обновления

            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        // если передать пустое значение title
        if (category.getTitle() == null || category.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title MUST be NOT NULL", HttpStatus.NOT_ACCEPTABLE);
        }

        categoryService.update(category);
        return new ResponseEntity(HttpStatus.OK); // возвращает только статус 200 - ОК
    }

    /**
     * Удаление категории методом DELETE
     */

    // DELETE - идемпотентный метод. Удаление можно также производить через POST, причем id категории для удаления передается в body
    @DeleteMapping("/delete/{id}") // id категории, которую надо удалить, предается в адресной строке.
    public ResponseEntity delete(@PathVariable("id") Long id) {

        // Применим исключение ошибки stacktrace. Через try-catch можно обработать исключение в статус
        try {
            categoryService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity(HttpStatus.OK); // возвращает статус сервера

    }

    /**
     * Поиск категории методом POST по названию и email пользователя
     */
    @PostMapping("/search")
    public ResponseEntity<List<Category>> search(@RequestBody CategorySearchValues categorySearchValues) { // Создали отдельный класс с параметрами title и email. См пакет search

        if ((categorySearchValues.getUserId() == null || categorySearchValues.getUserId() == 0)) {
            return new ResponseEntity("missed param: userId ", HttpStatus.NOT_ACCEPTABLE);
        }

        // если title будет пустым, то будет поиск всех категорий без фильтрации названия

        List<Category> list = categoryService.findByTitleOrUserId(categorySearchValues.getTitle(), categorySearchValues.getUserId());

        return ResponseEntity.ok(list);
    }


    /**
     * Поиск категории по ID методом POST
     */
    @PostMapping("/searchById") // ID передаем в тело метода - безопасно
    public ResponseEntity<Category> findById(@RequestBody Long id) {
        Optional<Category> category = categoryService.findById(id);
        try {
            if (category.isPresent()) { // если объект найден
                return ResponseEntity.ok(category.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Поиск категорий по ID методом GET
     */
    @GetMapping("/find/{id}") // id передаем в поисковую строку - это небезопасно
    public ResponseEntity<Category> find(@PathVariable("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        try {
            if (category.isPresent()) { // если объект найден
                return ResponseEntity.ok(category.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity("id = " + id + " not found", HttpStatus.NOT_ACCEPTABLE);


    }
}

